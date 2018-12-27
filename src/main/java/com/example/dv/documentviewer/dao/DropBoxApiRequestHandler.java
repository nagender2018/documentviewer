package com.example.dv.documentviewer.dao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.config.DocumentViewerConfiguration;
import com.example.dv.documentviewer.service.ApiConnection;
import com.example.dv.documentviewer.util.DocumentViewerConstants;
@Component
public class DropBoxApiRequestHandler implements ApiRequestHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(DropBoxApiRequestHandler.class.getName());
	
	@Autowired
	private ApiConnection dropBoxApiConnection; 
	
	@Autowired
	private DocumentViewerConfiguration configuration;
	
	@Override
	public String retrieveContentsOfFolder(String folderPath) {
		String apiUrl=configuration.getDropBoxApiListUrl();
		String parameters = "{\"path\": \"" + folderPath + "\",\"recursive\": false,\"include_media_info\": false,\"include_deleted\": false,\"include_has_explicit_shared_members\": false}";
		return processApiRequest(apiUrl, parameters,null);
	}

	@Override
	public String retrieveContentsOfFile(String filePath) {
		String parameters = "{\"path\": \"" + filePath + "\"}";
		String apiUrl=configuration.getDropBoxApiDownloadUrl();
		return processApiRequest(apiUrl, parameters,null); 
	}
	
	@Override
	public String uploadFile(String folderPath, MultipartFile file) {
		LOGGER.debug("file.getOriginalFilename() "+file.getOriginalFilename());
		String fileName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/")+1);
		LOGGER.debug("File to upload   "+ fileName);
		String parameters = "{\"path\": \""+folderPath+"/"+fileName+"\",\"mode\": \"add\",\"autorename\": true,\"mute\": false,\"strict_conflict\": false}";
		String apiUrl=configuration.getDropBoxApiUploadUrl();
		return processApiRequest(apiUrl, parameters,file);
	}
	
	private String processApiRequest(String apiUrl,String parameters,MultipartFile file) {
		LOGGER.info("API call to Dropbox with apiUrl :: "+ apiUrl +"  parameters :: "+parameters);
		StringBuffer responseBuffer = new StringBuffer(); 
		String requestType = apiUrl.substring(apiUrl.lastIndexOf("/")+1);
		try { 
			HttpURLConnection conn = dropBoxApiConnection.getConnection(apiUrl);
			if(requestType.equalsIgnoreCase(DocumentViewerConstants.API_REQUEST_TYPE_DOWNLOAD)) {
				conn.addRequestProperty ("Dropbox-API-Arg", parameters);
			}else if(requestType.equalsIgnoreCase(DocumentViewerConstants.API_REQUEST_TYPE_LIST)) {
			    try(DataOutputStream writer = new DataOutputStream(conn.getOutputStream())){
					writer.writeBytes(parameters);
					writer.flush();
				}
			}else if(requestType.equalsIgnoreCase(DocumentViewerConstants.API_REQUEST_TYPE_UPLOAD)) {
				conn.setRequestProperty("Content-Type", "application/octet-stream");
				conn.addRequestProperty ("Dropbox-API-Arg", parameters);
				try(DataOutputStream writer = new DataOutputStream(conn.getOutputStream())){
		            writer.write(file.getBytes());
					writer.flush();
				}
		        
			}
	        
	        if (conn.getResponseCode() != 200) {
	        	LOGGER.debug(conn.getResponseMessage());
	            responseBuffer.append("{ERR : "
	                    + conn.getResponseCode() +", Message : " +conn.getResponseMessage()+"}");
	            return responseBuffer.toString();
	        }

	        readResponse(responseBuffer, conn);
	        
	        conn.disconnect();
	      } catch (MalformedURLException e) {
	    	  LOGGER.error("Error :: "+e);
	    	responseBuffer.append("{ERR : "+e.getMessage()+"}") ;
	      } catch (IOException e) {
	    	  LOGGER.error("Error :: "+e);
	        responseBuffer.append("{ERR : "+e.getMessage() +"}");
	      }
		
		return responseBuffer.toString();
	}

	private void readResponse(StringBuffer responseBuffer, HttpURLConnection conn) throws IOException {
		String output;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
		    (conn.getInputStream())))){
			LOGGER.debug("Dropbox API Response :: ");
		    while ((output = br.readLine()) != null) {
		        responseBuffer.append(output);
		    }
		}
	}

}

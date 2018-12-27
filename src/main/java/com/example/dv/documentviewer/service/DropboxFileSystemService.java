package com.example.dv.documentviewer.service;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.dao.DropBoxApiRequestHandler;
import com.example.dv.documentviewer.domain.ApiResult;
import com.example.dv.documentviewer.domain.Entry;
import com.example.dv.documentviewer.exception.InvalidApiRequestException;
import com.example.dv.documentviewer.util.DocumentViewerConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class DropboxFileSystemService implements FileSystemServcie {

	private static final Logger LOGGER = LogManager.getLogger(DropboxFileSystemService.class.getName());
	
	@Autowired
	private DropBoxApiRequestHandler dropBoxConnectorDaoImpl;
	
	@Override
	public ApiResult retriveContentsOfFolder(String folderPath) {
		String output = dropBoxConnectorDaoImpl.retrieveContentsOfFolder(folderPath);
		return convertJsonToObject(output);
		
	}

	@Override
	public String retrieveContentsOfFile(String filePath) {
		return dropBoxConnectorDaoImpl.retrieveContentsOfFile(filePath);
	}

	@Override
	public ApiResult uploadFile(String folderPath, MultipartFile file) throws InvalidApiRequestException {
		if(folderPath.isEmpty() || file.isEmpty() || getFileSizeMegaBytes(file)>DocumentViewerConstants.FILE_SIZE_IN_MB){
			throw new InvalidApiRequestException(HttpStatus.BAD_REQUEST,"Invalid input");
		}
		return convertJsonToObject(dropBoxConnectorDaoImpl.uploadFile(folderPath, file));
	}
	
	private double getFileSizeMegaBytes(MultipartFile file) {
		return (double) file.getSize() / (1024 * 1024) ;
	}
	
	public ApiResult convertJsonToObject(String resultJson) {
		LOGGER.debug("convertJsonToObject resultJson :: "+ resultJson);
		ObjectMapper objectMapper = new ObjectMapper();
		ApiResult apiResult = new ApiResult();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		try {

		    JsonNode jsonNode = objectMapper.readValue(resultJson, JsonNode.class);
		    JsonNode entriesArray = jsonNode.get("entries");
		    if(entriesArray!=null) {
		    for (JsonNode entry : entriesArray) {
		    	Entry ent = new Entry();
		    	if(entry.get("id")!=null) {
		    		ent.setId(entry.get("id").asText());
		    	}
		    	if(entry.get("name")!=null) {
		    		ent.setName(entry.get("name").asText());
		    	}
		    	if(entry.get("path_lower")!=null) {
		    		ent.setPath(entry.get("path_lower").asText());
		    	}
		    	if(entry.get(".tag")!=null) {
		    		ent.setType(entry.get(".tag").asText());
		    	}
		    	if(entry.get("size")!=null) {
		    		ent.setSize(Integer.parseInt(entry.get("size").asText()));
		    	}
		    	
		    	entries.add(ent);
			}
		  }else {
			  Entry ent = new Entry();
		    	if(jsonNode.get("id")!=null) {
		    		ent.setId(jsonNode.get("id").asText());
		    	}
		    	if(jsonNode.get("name")!=null) {
		    		ent.setName(jsonNode.get("name").asText());
		    	}
		    	if(jsonNode.get("path_lower")!=null) {
		    		ent.setPath(jsonNode.get("path_lower").asText());
		    	}
		    	if(jsonNode.get("size")!=null) {
		    		ent.setSize(Integer.parseInt(jsonNode.get("size").asText()));
		    	}
		    	
		    	entries.add(ent);
		  }
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		apiResult.setEntries(entries);
		return apiResult;	
	}
	
}

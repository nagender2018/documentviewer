package com.example.dv.documentviewer.service;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dv.documentviewer.config.DocumentViewerConfiguration;
import com.example.dv.documentviewer.dao.DropBoxApiRequestHandler;
import com.example.dv.documentviewer.util.DocumentViewerConstants;

@Component
public class DropBoxApiConnection implements ApiConnection {
	
	private static final Logger LOGGER = LogManager.getLogger(DropBoxApiConnection.class.getName());
	
	@Autowired
	private DocumentViewerConfiguration configuration;
	
	@Override
	public HttpURLConnection getConnection(String apiUrl) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(apiUrl);
			LOGGER.info("apiUrl >>> "+ apiUrl);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.addRequestProperty("Authorization", "Bearer "+configuration.getDropBoxApiAuthToken());
	        conn.setRequestMethod("POST");
	        String requestType = apiUrl.substring(apiUrl.lastIndexOf("/")+1);
	        if(requestType.equalsIgnoreCase(DocumentViewerConstants.API_REQUEST_TYPE_LIST)) {
	        	conn.setRequestProperty("Accept", "application/json");        
        		conn.setRequestProperty("Content-Type", "application/json");
	        }
	        conn.setDoOutput(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}

}

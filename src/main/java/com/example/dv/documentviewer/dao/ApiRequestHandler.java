package com.example.dv.documentviewer.dao;

import org.springframework.web.multipart.MultipartFile;

public interface ApiRequestHandler {
	public String retrieveContentsOfFolder(String folderPath);
	public String retrieveContentsOfFile(String filePath);
	public String uploadFile(String folderPath, MultipartFile file);
}

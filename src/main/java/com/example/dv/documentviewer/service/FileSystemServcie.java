package com.example.dv.documentviewer.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.domain.ApiResult;

public interface FileSystemServcie {
	public ApiResult retriveContentsOfFolder(String folderPath);
	
	public String retrieveContentsOfFile(String filePath);

	public ApiResult uploadFile(String folderPath, MultipartFile file);

}

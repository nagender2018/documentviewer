package com.example.dv.documentviewer.controller;

import java.nio.charset.Charset;


import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.domain.ApiResult;
import com.example.dv.documentviewer.service.DropboxFileSystemService;

@RestController
@RequestMapping("/api/files")
public class DocumentViewerController {
	
	private static final Logger LOGGER = LogManager.getLogger(DocumentViewerController.class.getName());
	
	@Autowired
	private DropboxFileSystemService dropboxFileSystemServiceImpl;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<ApiResult> retriveContents(@RequestParam(value = "folderPath", defaultValue = "") String folderPath) {
		ApiResult  result = dropboxFileSystemServiceImpl.retriveContentsOfFolder(folderPath);
		LOGGER.info("result.getEntries().size() :: "+result.getEntries().size());
		return new ResponseEntity<ApiResult>(result,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void retriveContents(@RequestParam("filePath") String filePath,
			HttpServletResponse response) throws Exception{
		String content = dropboxFileSystemServiceImpl.retrieveContentsOfFile(filePath);
		String fileName= filePath.substring(filePath.lastIndexOf("/")+1);
		response.setHeader("Content-Disposition", "attachment; filename=\"" +  fileName + "\"");

		response.setContentType(response.getContentType());

		response.getOutputStream().write(content.getBytes(Charset.forName("utf8")));
		
		response.flushBuffer();

	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<ApiResult> uploadFile(@RequestParam("folderPath") String folderPath,@RequestParam("file") MultipartFile file) {
		ApiResult result =dropboxFileSystemServiceImpl.uploadFile(folderPath,file);
		return new ResponseEntity<ApiResult>(result,HttpStatus.OK);
	}
	
}

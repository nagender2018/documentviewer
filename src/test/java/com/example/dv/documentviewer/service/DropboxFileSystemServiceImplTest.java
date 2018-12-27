package com.example.dv.documentviewer.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.dao.DropBoxApiRequestHandler;
import com.example.dv.documentviewer.domain.ApiResult;

public class DropboxFileSystemServiceImplTest  {
	
	@Mock
	DropBoxApiRequestHandler dropBoxApiRequestHandler;
	
	@InjectMocks
	DropboxFileSystemService dropboxFileSystemService;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testRetriveContentsOfFolder() {
		when(dropBoxApiRequestHandler.retrieveContentsOfFolder(anyString())).thenReturn("{\"entries\": [{\".tag\": \"folder\", \"name\": \"Cricket\", \"path_lower\": \"/sports/cricket\", \"path_display\": \"/Sports/Cricket\", \"id\": \"id:oWTYseNXd_AAAAAAAAAADg\"}, {\".tag\": \"folder\", \"name\": \"Football\", \"path_lower\": \"/sports/football\", \"path_display\": \"/Sports/Football\", \"id\": \"id:oWTYseNXd_AAAAAAAAAADw\"}]}");
		ApiResult apiResult = dropboxFileSystemService.retriveContentsOfFolder("/Sports");
		assertEquals(2,apiResult.getEntries().size());
		assertEquals("/sports/cricket",apiResult.getEntries().get(0).getPath());
	}
	
	@Test
	public void testRretrieveContentsOfFile() {
		when(dropBoxApiRequestHandler.retrieveContentsOfFile(anyString())).thenReturn("some text");
		String fileContent = dropboxFileSystemService.retrieveContentsOfFile("/Sports");
		assertEquals("some text", fileContent);
	}
	
	@Test
	public void testUploadFile() {
		MultipartFile mockMulti =  new MockMultipartFile("file", "fileName121.txt", null, "uploaded".getBytes());
		when(dropBoxApiRequestHandler.uploadFile("/sports", mockMulti)).thenReturn("{\"name\": \"fileName121.txt\", \"path_lower\": \"/sports/filename121.txt\", \"path_display\": \"/Sports/fileName121.txt\", \"id\": \"id:oWTYseNXd_AAAAAAAAAAFw\", \"size\": 14}");
		ApiResult apiResult  = dropboxFileSystemService.uploadFile("/sports",mockMulti);
		assertEquals(1,apiResult.getEntries().size());
		assertEquals("/sports/filename121.txt",apiResult.getEntries().get(0).getPath());
	}
	
	
	
	
}

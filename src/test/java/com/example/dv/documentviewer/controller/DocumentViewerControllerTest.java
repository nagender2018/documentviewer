package com.example.dv.documentviewer.controller;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.example.dv.documentviewer.domain.ApiResult;
import com.example.dv.documentviewer.domain.Entry;
import com.example.dv.documentviewer.service.DropboxFileSystemService;


public class DocumentViewerControllerTest {

	@InjectMocks
	DocumentViewerController documentViewerController;
	
	@Mock
	DropboxFileSystemService dropboxFileSystemServiceImpl;
	

	MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(documentViewerController).setControllerAdvice(new DocViewerExceptionHandler()).build();
	}
	
	@Test
	public void testRetriveContents() throws Exception {
		ApiResult result = new ApiResult();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();
		entry1.setId("1");
		entry1.setName("sports");
		entry1.setPath("/sports");
		entry1.setType("folder");
				
		entries.add(entry1);
		result.setEntries(entries);
		when(dropboxFileSystemServiceImpl.retriveContentsOfFolder(anyString())).thenReturn(result);
		mockMvc.perform(get("/api/files/list?folderPath=/sports"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.entries[0].id", is("1")))
	        .andExpect(jsonPath("$.entries[0].name", is("sports")))
	        .andExpect(jsonPath("$.entries[0].path", is("/sports")))
	        .andExpect(jsonPath("$.entries[0].type", is("folder")));
	}
	
	@Test
	public void testRetriveContentsNoPath() throws Exception {
		ApiResult result = new ApiResult();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();
		entry1.setId("1");
		entry1.setName("sports");
		entry1.setPath("/sports");
		entry1.setType("folder");
				
		entries.add(entry1); 
		result.setEntries(entries);
		when(dropboxFileSystemServiceImpl.retriveContentsOfFolder(anyString())).thenReturn(result);
		
		mockMvc.perform(get("/api/files/list"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.entries[0].id", is("1")))
			.andExpect(jsonPath("$.entries[0].name", is("sports")))
			.andExpect(jsonPath("$.entries[0].path", is("/sports")))
			.andExpect(jsonPath("$.entries[0].type", is("folder")));
	}
	
	@Test
	public void testRetrieveContentsOfFile() throws Exception{
		when(dropboxFileSystemServiceImpl.retrieveContentsOfFile(anyString())).thenReturn("some text");
		ResultActions result = mockMvc.perform(get("/api/files/download?filePath=/sports/test.txt"));
		result.andExpect(status().isOk());
		assertEquals(result.andReturn().getResponse().getContentAsString(),"some text");
	}
	
	@Test
	public void testUploadFile() throws Exception{
		ApiResult result = new ApiResult();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		Entry entry1 = new Entry();
		entry1.setId("1");
		entry1.setName("fileName121.txt");
		entry1.setPath("/sports/filename121.txt");
		entry1.setSize(14);
				
		entries.add(entry1); 
		result.setEntries(entries);
		MultipartFile multi =  new MockMultipartFile("file", "fileName121.txt", "text/plain", "uploaded".getBytes());
		MockMultipartFile mockMulti =  new MockMultipartFile("file", "fileName121.txt", "text/plain", "uploaded".getBytes());
		when(dropboxFileSystemServiceImpl.uploadFile("/sports", multi)).thenReturn(result);
	    MvcResult res = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/files/upload?folderPath=/sports").file(mockMulti).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(200, res.getResponse().getStatus());
        assertNotNull(res.getResponse().getContentAsString());
	}
}

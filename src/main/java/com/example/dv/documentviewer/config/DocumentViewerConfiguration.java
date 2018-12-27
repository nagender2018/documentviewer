package com.example.dv.documentviewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentViewerConfiguration {
	@Value("${dropbox.api.list.url}")
	private String dropBoxApiListUrl;
	
	@Value("${dropbox.api.upload.url}")
	private String dropBoxApiUploadUrl;
	
	@Value("${dropbox.api.download.url}")
	private String dropBoxApiDownloadUrl;
	
	@Value("${dropbox.api.auth.token}")
	private String dropBoxApiAuthToken;
	
	public String getDropBoxApiListUrl() {
		return dropBoxApiListUrl;
	}

	public void setDropBoxApiListUrl(String dropBoxApiListUrl) {
		this.dropBoxApiListUrl = dropBoxApiListUrl;
	}

	public String getDropBoxApiUploadUrl() {
		return dropBoxApiUploadUrl;
	}

	public void setDropBoxApiUploadUrl(String dropBoxApiUploadUrl) {
		this.dropBoxApiUploadUrl = dropBoxApiUploadUrl;
	}

	public String getDropBoxApiDownloadUrl() {
		return dropBoxApiDownloadUrl;
	}

	public void setDropBoxApiDownloadUrl(String dropBoxApiDownloadUrl) {
		this.dropBoxApiDownloadUrl = dropBoxApiDownloadUrl;
	}

	public String getDropBoxApiAuthToken() {
		return dropBoxApiAuthToken;
	}

	public void setDropBoxApiAuthToken(String dropBoxApiAuthToken) {
		this.dropBoxApiAuthToken = dropBoxApiAuthToken;
	}
	
}

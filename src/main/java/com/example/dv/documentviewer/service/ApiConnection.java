package com.example.dv.documentviewer.service;

import java.net.HttpURLConnection;

public interface ApiConnection {
	public HttpURLConnection getConnection(String apiUrl);
}

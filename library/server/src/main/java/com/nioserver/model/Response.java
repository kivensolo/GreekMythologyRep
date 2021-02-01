package com.nioserver.model;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Response {
    public int code;
	public String message;
	public InputStream data;
	public Map<String, List<String>> headers;
	public Request request;
}

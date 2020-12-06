package com.zeke.local.nioserver.http;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by hy on 2015/11/6.
 */
public class HttpResponse {
    public int code;
	public String message;
	public InputStream data;
	public Map<String, List<String>> headers;
	public HttpRequest request;
}

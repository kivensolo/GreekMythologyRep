package com.nioserver.model;

import android.text.TextUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
	public String method = "get";
	public String schema = "http";
	public String host;
	public int port = -1;
	public String path;
	// querie params map of url.
	public LinkedHashMap<String, String> queries;
	// the fragment part of path, from '#' to end.
	public String fragment;

	// post only members
	public ArrayList<String> header;
	public LinkedHashMap<String, String> form;
	public byte[] body;

	// timeout settings(ms)
	public int connectTimeout = -1;
	public int readTimeout = -1;

	@Override
	public String toString() {
		final StringBuilder urlBuilder = new StringBuilder();

		Request request = this;
		urlBuilder.append(request.schema)
			.append("://");

		request.buildHostString(urlBuilder);

		if (!TextUtils.isEmpty(request.path)) {
			urlBuilder.append(request.path);
		}

		if (request.queries != null && !request.queries.isEmpty()) {
			urlBuilder.append("?");
			buildQueryString(urlBuilder, request.queries);
		}

		if (!TextUtils.isEmpty(request.fragment)) {
			urlBuilder.append("#")
				.append(request.fragment);
		}


		String url = urlBuilder.toString();
		return url;
	}

	public StringBuilder buildHostString(StringBuilder urlBuilder) {
		urlBuilder.append(host);
		if (port > 0) {
			urlBuilder.append(":").append(port);
		}
		return urlBuilder;
	}

	public String getHostString() {
		return buildHostString(new StringBuilder()).toString();
	}

	public static StringBuilder buildQueryString(StringBuilder urlBuilder, LinkedHashMap<String, String> params) {
		int idx = 0;
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (idx > 0) {
					urlBuilder.append("&");
				}
				++idx;
				urlBuilder.append(URLEncoder.encode(entry.getKey()));
				if (entry.getValue() != null) {
					urlBuilder.append("=")
						.append(URLEncoder.encode(entry.getValue()));
				}
			}
		}
		return urlBuilder;
	}

	public String getQueryString() {
		return buildQueryString(new StringBuilder(), queries).toString();
	}

	public String getFormParams() {
		return buildQueryString(new StringBuilder(), form).toString();
	}

	public Request addQueryString(String key, String val) {
		if (queries == null) {
			queries = new LinkedHashMap<String, String>();
		}
		queries.put(key, val);
		return this;
	}

	public String getQueryString(String key) {
		if (queries == null) {
			return null;
		}
		return queries.get(key);
	}

	public String removeQueryString(String key) {
		if (queries == null) {
			return null;
		}
		return queries.remove(key);
	}

	public boolean hasQueryKey(String key) {
		if (queries == null) {
			return false;
		}
		return queries.containsKey(key);
	}

	public Request addHeaderParam(String key, String val) {
		if (header == null) {
			header = new ArrayList<String>();
		}
		header.add(key);
		header.add(val);
		return this;
	}

	public String getHeaderParam(String key) {
		if (header == null || header.isEmpty()) {
			return null;
		}
		for (int i = 0, headerSize = header.size(); i < headerSize; i += 2) {
			String val = header.get(i);
			if (key.equals(val)) {
				return header.get(i + 1);
			}
		}
		return null;
	}

	public ArrayList<String> getHeaderParam() {
		if (header == null || header.isEmpty()) {
			return null;
		}
		return  header;
	}

	public Request addFormParam(String key, String val) {
		if (form == null) {
			form = new LinkedHashMap<String, String>();
		}
		form.put(key, val);
		return this;
	}

	public String getFormParam(String key) {
		if (form == null) {
			return null;
		}
		return form.get(key);
	}

	public String removeFormParam(String key) {
		if (form == null) {
			return null;
		}
		return form.remove(key);
	}

	public boolean hasFormKey(String key) {
		if (form == null) {
			return false;
		}
		return form.containsKey(key);
	}

	public Request makeClone() {
		Request newRequest = makeCloneNoQueryString();
		if (queries != null) {
			newRequest.queries = (LinkedHashMap<String, String>) queries.clone();
		}
		return newRequest;
	}

	public Request makeCloneNoQueryString() {
		Request newRequest = new Request();
		newRequest.method = method;
		newRequest.schema = schema;
		newRequest.host = host;
		newRequest.port = port;
		newRequest.path = path;
		newRequest.fragment = fragment;
		if (header != null) {
			newRequest.header = (ArrayList<String>) header.clone();
		}
		if (form!= null) {
			newRequest.form = (LinkedHashMap<String, String>) form.clone();
		}
		newRequest.body = body;
		newRequest.connectTimeout = connectTimeout;
		newRequest.readTimeout = readTimeout;
		return newRequest;
	}
}

package com.longqin.system.util;

import java.io.Serializable;

public class ResponseData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private Object data;

	public ResponseData(int code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResponseData(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResponseData() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
package com.longqin.business.config;

import java.io.Serializable;

/**
 * @Description 统一请求返回结果
 * @Author longqin
 * @Time: 2023年10月20日
 */
public class BaseResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	
	public BaseResponse(){
		super();
	}
	
	public BaseResponse(int code,String msg){
		this.code=code;
		this.msg=msg;
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
}

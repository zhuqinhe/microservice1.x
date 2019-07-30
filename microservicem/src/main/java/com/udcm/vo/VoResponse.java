package com.udcm.vo;

import com.udcm.constants.StatusCode;



public class VoResponse<T> extends Response{
	private T vo;


	public VoResponse () {
	}
	
	public VoResponse (int resultCode) {
		this.setResultCode(resultCode);
		if(resultCode == StatusCode.UI.UI_0) {
			this.setDescription("SUCCESS");
		} else if(resultCode == StatusCode.UI.UI_1) {
			this.setDescription("FAIL");
		} else if(resultCode == StatusCode.UI.UI_20001) {
			this.setDescription("Invalid Auth Token");
		} else if(resultCode == StatusCode.UI.UI_20002) {
			this.setDescription("Auth Token Timeout");
		} else if(resultCode == StatusCode.UI.UI_20003) {
			this.setDescription("Invalid Captcha");
		} else if(resultCode == StatusCode.UI.UI_20004) {
			this.setDescription("Captcha Timeout");
		} else if(resultCode == StatusCode.UI.UI_20005) {
			this.setDescription("Unauthorized");
		} else if(resultCode == StatusCode.UI.UI_20006) {
			this.setDescription("Partially Authorized Content");
		} else if(resultCode == StatusCode.UI.UI_20008) {
			this.setDescription("Invalid User");
		} else if(resultCode == StatusCode.UI.UI_20009) {
			this.setDescription("Abnormal Operation");
		} else if(resultCode == StatusCode.UI.UI_20010) {
			this.setDescription("Wrong Arguments");
		} else if(resultCode == StatusCode.UI.UI_40001) {
			this.setDescription("Missing Required Arguments");
		} else if(resultCode == StatusCode.UI.UI_40002) {
			this.setDescription("Invalid Arguments");
		}
	}

	public T getVo() {
		return vo;
	}

	public void setVo(T vo) {
		this.vo = vo;
	}

}

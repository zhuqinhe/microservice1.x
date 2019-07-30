/**
 * 
 */
package com.udcm.vo;

import com.udcm.constants.StatusCode;





/**
 * @author Raul	
 * 2017年8月25日
 */

public class Response {
	
	public Response(){}
	public Response(int resultCode){
		this.resultCode = resultCode;
		if(resultCode == StatusCode.UI.UI_0) {
			this.setDescription("SUCCESS");
		} else if(resultCode == StatusCode.UI.UI_1) {
			this.setDescription("FAIL");
		}
	}

	//状态码
	private int resultCode;
	
	//状态描述
	private String description;
    
	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	   //可以考虑把结果写入request中，不是以ModelAndViewr返回controller的操作结果     response不好取
	  //ResultUntils.getInstance().set(this.resultCode);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

package com.reminder.constants;

public enum ResponseCode {

	SUCCESS(0, "Success"), //成功
	ERROR(-1, "服务内部异常"), //失败
	ILLEGAL_PARAMETER(-2, "参数非法");//参数非法
	

	// 成员变量
	private int code;
	private String name;

	// 构造方法
	ResponseCode(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 普通方法
	public static String getName(int code) {
		for (ResponseCode c : ResponseCode.values()) {
			if (c.getCode() == code) {
				return c.name;
			}
		}
		return "";
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}

}

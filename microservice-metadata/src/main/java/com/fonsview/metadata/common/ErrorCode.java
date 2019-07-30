package com.fonsview.metadata.common;

public enum ErrorCode {

	SUCCESS(0, "Success"), ERROR(-1, "failed"), ILLEGAL_PARAMETER(-1, "参数非法");

	// 成员变量
	private int code;
	private String name;

	// 构造方法
	ErrorCode(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 普通方法
	public static String getName(int code) {
		for (ErrorCode c : ErrorCode.values()) {
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

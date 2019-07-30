package com.fonsview.sync.exception;

import com.fonsview.sync.common.ErrorCode;

public class CommonException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorCode code = ErrorCode.SUCCESS;

	private String message = "";

	public CommonException(ErrorCode code) {
		this.code = code;
	}

	public CommonException(ErrorCode code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ErrorCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}

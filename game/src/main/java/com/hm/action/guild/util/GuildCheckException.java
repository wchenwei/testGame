package com.hm.action.guild.util;

//阵营中的检验异常
public class GuildCheckException extends Exception{
	private static final long serialVersionUID = 1L;
	private int errorCode;
	public int getErrorCode() {
		return errorCode;
	}
	public GuildCheckException(int errorCode) {
		this.errorCode = errorCode;
	}
}

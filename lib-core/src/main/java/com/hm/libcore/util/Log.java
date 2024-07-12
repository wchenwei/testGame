package com.hm.libcore.util;

import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

// ************************************************************************
/**
 * 日志类
 * @Author: siyunlong
 * @Version: V1.00
 * @Create Date: 2015-12-1上午9:14:50
 */
public class Log {

	// ************************************************************************
	// fields
	private transient static final Logger Log;

	// ************************************************************************
	// static init

	// ------------------------------------------------------------------------
	static {
		Log = Logger.getAnonymousLogger();

		Log.setLevel(Level.ALL);

		/*
		 * for( Handler handler : Log.getHandlers() ) { Log.removeHandler(
		 * handler ); }
		 */
	}

	// ************************************************************************
	// properties

	// ------------------------------------------------------------------------
	public static Logger Instance() {
		return Log;
	}

	// ************************************************************************
	// public interface

	// ------------------------------------------------------------------------
	public static void Error(String format, Object... args) {
		Error(String.format(format, args));
	}

	// ------------------------------------------------------------------------
	public static void Error(String message) {
		LogMessage(Level.SEVERE, message);
	}

	// ------------------------------------------------------------------------
	public static void Warning(String format, Object... args) {
		Warning(String.format(format, args));
	}

	// ------------------------------------------------------------------------
	public static void Warning(String message) {
		LogMessage(Level.WARNING, message);
	}

	// ------------------------------------------------------------------------
	public static void Info(String format, Object... args) {
		LogMessage(Level.INFO, String.format(format, args));
	}

	// ------------------------------------------------------------------------
	public static void Info(String message) {
//		LogMessage(Level.INFO, message);
		System.out.println("info:"+message);
	}

	// ------------------------------------------------------------------------
	public static void Debug(String format, Object... args) {
		Debug(String.format(format, args));
	}

	// ------------------------------------------------------------------------
	public static void Debug(String message) {
//		Debug(message, false);
//		System.out.println("debug:"+message);
	}

	// ------------------------------------------------------------------------
	public static void Debug(String message, boolean realCloudForced) {
		if (false == realCloudForced) {
			return;
		}

		LogMessage(Level.FINE, message);
	}

	// ------------------------------------------------------------------------
	public static void Exception(String msg, Throwable e) {
		Log.log(Level.SEVERE, msg, e);
	}

	// ------------------------------------------------------------------------
	public static void ExceptionInfo(String msg, Throwable e) {
		Log.log(Level.INFO, msg, e);
	}

	// ------------------------------------------------------------------------
	public static String GetExecutedMethodName() {
		StackTraceElement stackElement = Thread.currentThread().getStackTrace()[2];

		return String.format(
				"%s::%s",
				stackElement.getClassName().substring(
						stackElement.getClassName().lastIndexOf('.') + 1),
				stackElement.getMethodName());
	}

	// ************************************************************************
	// internal methods

	// ------------------------------------------------------------------------
	private static void LogMessage(Level level, String message) {
		Log.logp(level, "", "", message);
	}

}

package com.panfeng.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件名生成工具
 * 
 * @author WangLM
 *
 */
/* {filename} 会替换成原文件名,配置这项需要注意中文乱码问题 */
/*
 * /* /*
 */
/* */
/*  */
/*  */
/* */
/*  */
/*  */
public class PathFormatUtils {
	private static final String TIME = "time";
	private static final String FULL_YEAR = "yyyy";
	private static final String YEAR = "yy";
	private static final String MONTH = "mm";
	private static final String DAY = "dd";
	private static final String HOUR = "hh";
	private static final String MINUTE = "ii";
	private static final String SECOND = "ss";
	private static final String RAND = "rand";

	private static Date currentDate = null;

	/**
	 * 根据输入的表达式生成文件名
	 * <p>
	 * {rand:6} 会替换成随机数,后面的数字是随机数的位数
	 * <p>
	 * {time} 会替换成时间戳
	 * <p>
	 * {yyyy} 会替换成四位年份
	 * <p>
	 * {yy} 会替换成两位年份
	 * <p>
	 * {mm} 会替换成两位月份
	 * <p>
	 * {dd} 会替换成两位日期
	 * <p>
	 * {hh} 会替换成两位小时
	 * <p>
	 * {ii} 会替换成两位分钟
	 * <p>
	 * {ss} 会替换成两位秒
	 * <p>
	 * 
	 * @param input
	 * @return
	 */
	public static String parse(String input) {

		Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		PathFormatUtils.currentDate = new Date();

		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {

			matcher.appendReplacement(sb,
					PathFormatUtils.getString(matcher.group(1)));

		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	/**
	 * 格式化路径, 把windows路径替换成标准路径
	 * 
	 * @param input
	 *            待格式化的路径
	 * @return 格式化后的路径
	 */
	public static String format(String input) {

		return input.replace("\\", "/");

	}

	/**
	 * 根据输入表达式生成文件名，或者消除文件名内特殊字符
	 * <p>
	 * 如果 input内包含{filename} 属性则根据参数filename
	 * <p>
	 * 进行去特殊字符返回去掉特殊字符串的文件名
	 * <p>
	 * 
	 * @param input
	 * @param filename
	 * @return
	 */
	public static String parse(String input, String filename) {

		Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		String matchStr = null;

		PathFormatUtils.currentDate = new Date();

		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {

			matchStr = matcher.group(1);
			if (matchStr.indexOf("filename") != -1) {
				filename = filename.replace("$", "\\$").replaceAll(
						"[\\/:*?\"<>|]", "");
				matcher.appendReplacement(sb, filename);
			} else {
				matcher.appendReplacement(sb,
						PathFormatUtils.getString(matchStr));
			}

		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	private static String getString(String pattern) {

		pattern = pattern.toLowerCase();

		// time 处理
		if (pattern.indexOf(PathFormatUtils.TIME) != -1) {
			return PathFormatUtils.getTimestamp();
		} else if (pattern.indexOf(PathFormatUtils.FULL_YEAR) != -1) {
			return PathFormatUtils.getFullYear();
		} else if (pattern.indexOf(PathFormatUtils.YEAR) != -1) {
			return PathFormatUtils.getYear();
		} else if (pattern.indexOf(PathFormatUtils.MONTH) != -1) {
			return PathFormatUtils.getMonth();
		} else if (pattern.indexOf(PathFormatUtils.DAY) != -1) {
			return PathFormatUtils.getDay();
		} else if (pattern.indexOf(PathFormatUtils.HOUR) != -1) {
			return PathFormatUtils.getHour();
		} else if (pattern.indexOf(PathFormatUtils.MINUTE) != -1) {
			return PathFormatUtils.getMinute();
		} else if (pattern.indexOf(PathFormatUtils.SECOND) != -1) {
			return PathFormatUtils.getSecond();
		} else if (pattern.indexOf(PathFormatUtils.RAND) != -1) {
			return PathFormatUtils.getRandom(pattern);
		}

		return pattern;

	}

	private static String getTimestamp() {
		return System.currentTimeMillis() + "";
	}

	private static String getFullYear() {
		return new SimpleDateFormat("yyyy").format(PathFormatUtils.currentDate);
	}

	private static String getYear() {
		return new SimpleDateFormat("yy").format(PathFormatUtils.currentDate);
	}

	private static String getMonth() {
		return new SimpleDateFormat("MM").format(PathFormatUtils.currentDate);
	}

	private static String getDay() {
		return new SimpleDateFormat("dd").format(PathFormatUtils.currentDate);
	}

	private static String getHour() {
		return new SimpleDateFormat("HH").format(PathFormatUtils.currentDate);
	}

	private static String getMinute() {
		return new SimpleDateFormat("mm").format(PathFormatUtils.currentDate);
	}

	private static String getSecond() {
		return new SimpleDateFormat("ss").format(PathFormatUtils.currentDate);
	}

	private static String getRandom(String pattern) {

		int length = 0;
		pattern = pattern.split(":")[1].trim();

		length = Integer.parseInt(pattern);

		return (Math.random() + "").replace(".", "").substring(0, length);

	}

	public static void main(String[] args) {
		System.out.println(parse("{filename}", "123*.mp4"));
		System.out.println(parse("{time}{rand:6}_{ss}.mp4"));
	}

}
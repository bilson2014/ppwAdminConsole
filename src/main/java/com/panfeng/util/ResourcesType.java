package com.panfeng.util;

/**
 * 资源文件类型枚举
 * 
 * @author Wang,LM
 *
 */
public enum ResourcesType {

	INDENT_MEDIA, INDENT_IMAGE;

	public String getName() {
		return PathFormatUtils.parse("{time}{rand:6}");
	}

	public String getPath() {
		String result = "";
		switch (this) {
		case INDENT_MEDIA:
			result = Constants.FILE_PROFIX + Constants.INDENT_RESOURCE_PATH;
			break;
		case INDENT_IMAGE:
			result = Constants.FILE_PROFIX + Constants.INDENT_RESOURCE_PATH;
			break;
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(ResourcesType.INDENT_MEDIA.getName()+ResourcesType.INDENT_MEDIA.getPath());
	}

}

package com.panfeng.util;

import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.PathFormatUtils;

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
}

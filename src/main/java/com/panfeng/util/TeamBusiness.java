package com.panfeng.util;

import java.util.HashMap;
import java.util.Map;

public class TeamBusiness {
 
	/**
	 * 0 广告 1 宣传片 2 真人秀 3 纪录片 4 病毒视频 5 电视栏目 6 三维动画 7 MG动画 8 体育赛事 9 专题片 10 VR拍摄
	 * 11 产品拍摄 12 微电影 13 航拍 14 活动视频 15 后期制作
	 */
	private  static Map<Integer,String> TAGS = new HashMap<>();
	static{
		TAGS.put(0, "广告");
		TAGS.put(1, "宣传片");
		TAGS.put(2, "真人秀");
		TAGS.put(3, " 纪录片");
		TAGS.put(4, "病毒视频");
		TAGS.put(5, "电视栏目");
		TAGS.put(6, "三维动画");
		TAGS.put(7, "MG动画");
		TAGS.put(8, "体育赛事");
		TAGS.put(9, "专题片");
		TAGS.put(10, "VR拍摄");
		TAGS.put(11, "产品拍摄");
		TAGS.put(12, "微电影");
		TAGS.put(13, "航拍");
		TAGS.put(14, "活动视频");
		TAGS.put(15, "后期制作");
		TAGS.put(16, "TVC");
		TAGS.put(17, "MV");
		TAGS.put(18, "包装");
	}
	
	public static String get(int id){
		return TAGS.get(id);
	}
	
	public static Map<Integer, String> getMap(){
		return TAGS;
	}
}

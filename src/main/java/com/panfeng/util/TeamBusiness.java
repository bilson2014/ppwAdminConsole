package com.panfeng.util;

public enum TeamBusiness {

	/**
	0  广告
	1  宣传片
	2  真人秀
	3  纪录片
	4  病毒视频
	5  电视栏目
	6  三维动画
	7  MG动画
	8  体育赛事
	9  专题片
	10 VR拍摄
	11 产品拍摄
	12 微电影
	13 航拍
	14 活动视频
	15 后期制作
 */
	GUANGGAO("广告",0),
	XUANCHUANPIAN("宣传片",1),
	ZHENRENXIU("真人秀",2),
	JILUPIAN("纪录片",3),
	BINGDUSHIPIN("病毒视频",4),
	DIANSHILANMU("电视栏目",5),
	SANWEIDONGHUA("三维动画",6),
	MGDONGHUA("MG动画",7),
	TIYUSAISHI("体育赛事",8),
	ZHUANTIPIAN("专题片",9),
	VRPAISHEI("VR拍摄",10),
	CHANPINPAISHEI("产品拍摄",11),
	WEIDIANYING("微电影",12),
	HANGPAI("航拍",13),
	HUODONGSHIPIN("活动视频",14),
	HOUQIZHIZUO("后期制作",15);
	
	private String name;
	private int index;

	private TeamBusiness(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}


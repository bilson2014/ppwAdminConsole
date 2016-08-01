package com.panfeng.resource.model;

import java.util.List;

import com.panfeng.domain.BaseObject;

/**
 * 项目业务类
 * 
 * @author GY
 */
public class Product extends BaseObject {

	private static final long serialVersionUID = 1070045570754608438L;
	
	public  static int MASTERWORK_LEVEL_0 = 0; // 普通项目
	
	public  static int MASTERWORK_LEVEL_1 = 1;	// 代表作
	
	public  static int MASTERWORK_LEVEL_2 = 2;

	private long productId = 0l;

	private int showType = 1; // 默认开启图文显示

	private String videoUrl = null; // 视频连接

	private String videoDescription = null; // 视频说明

	private String pDescription = null; // 项目介绍

	private String productName = null; // 项目名称

	private String picHDUrl = null; // 缩略图

	private String picLDUrl = null; // 海报

	private int productType; // 项目类别

	private String productTypeName = null; // 项目类别名称

	private String uploadDate = null; // 上传时间
	
	private int recommend = 0; // 推荐值
	
	private long supportCount = 0; // 点赞
	
	private String videoLength = "0:00"; // 视频长度

	private int flag = 0; // 产品状态位

	private int visible = 0; // 产品是否可见 (0:可见,1:不可见)

	private String hret = ""; // 外链(优酷/土豆等url)

	private Long teamId = null;

	private String teamName = null;

	private String teamPhotoUrl = null; // 团队照片

	private String teamDescription = null; // 团队介绍

	private long serviceId = 0l; // 服务编号

	private double servicePrice = 0.0d; // 服务真实价格

	private double serviceRealPrice = 0.0d; // 折扣后价格

	private long mcoms = 0l; // 服务-影片长度

	private String itemName = null; // 类型名称

	private String tags = null; // 标签字段

	private String sessionId = null;
	
	private String creationTime = null; // 创作时间
	
	private Integer masterWork = null; // 推荐作品，0为正常 1 为推荐 
	
	// 冗余字段
	private List<String> tagsArray = null;

	public List<String> getTagsArray() {
		return tagsArray;
	}

	public void setTagsArray(List<String> tagsArray) {
		this.tagsArray = tagsArray;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getMasterWork() {
		return masterWork;
	}

	public void setMasterWork(Integer masterWork) {
		this.masterWork = masterWork;
	}

	public long getServiceId() {
		return serviceId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getMcoms() {
		return mcoms;
	}

	public void setMcoms(long mcoms) {
		this.mcoms = mcoms;
	}

	public double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public double getServiceRealPrice() {
		return serviceRealPrice;
	}

	public void setServiceRealPrice(double serviceRealPrice) {
		this.serviceRealPrice = serviceRealPrice;
	}

	public String getTeamPhotoUrl() {
		return teamPhotoUrl;
	}

	public void setTeamPhotoUrl(String teamPhotoUrl) {
		this.teamPhotoUrl = teamPhotoUrl;
	}

	public String getTeamDescription() {
		return teamDescription;
	}

	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoDescription() {
		return videoDescription;
	}

	public void setVideoDescription(String videoDescription) {
		this.videoDescription = videoDescription;
	}

	public String getpDescription() {
		return pDescription;
	}

	public void setpDescription(String pDescription) {
		this.pDescription = pDescription;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPicHDUrl() {
		return picHDUrl;
	}

	public void setPicHDUrl(String picHDUrl) {
		this.picHDUrl = picHDUrl;
	}

	public String getPicLDUrl() {
		return picLDUrl;
	}

	public void setPicLDUrl(String picLDUrl) {
		this.picLDUrl = picLDUrl;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public long getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(long supportCount) {
		this.supportCount = supportCount;
	}

	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getVisible() {
		return visible;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getHret() {
		return hret;
	}

	public void setHret(String hret) {
		this.hret = hret;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

}

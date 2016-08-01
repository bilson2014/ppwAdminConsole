package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.Product;
import com.panfeng.resource.view.ProductView;

public interface ProductService {

	/**
	 * 分页检索 product
	 * @param product
	 * @return list of product
	 */
	public List<Product> listWithPagination(final ProductView view);
	
	/**
	 * 保存后返回自动生成的ID
	 * @param product
	 * @return productId
	 */
	public long save(final Product product);
	
	
	/**
	 * 删除后返回信息列表，便于删除文件
	 * @param ids 编号数组
	 * @return 团队列表
	 */
	public List<Product> delete(final long[] ids);
	
	public long update(final Product product);
	
	/**
	 * 获取 product总个数
	 * @return product的总个数
	 */
	public long maxSize(final ProductView view);
	
	/**
	 * 根据 ID 获取 product 信息
	 * @param id product ID
	 * @return product
	 */
	public Product findProductById(final long productId);

	/**
	 * 保存 路径
	 * @param product
	 */
	public long saveFileUrl(Product product);

	/**
	 * 获取所有 产品
	 */
	public List<Product> all();

	// ---------------------  前端 展示 功能 -----------------------------
	public List<Product> listWithCondition(final ProductView view);

	// 获取 产品信息 以及 所对应的 team 信息
	public Product loadProduct(final Integer productId);

	/**
	 * 获取已审核的作品
	 * @param teamId
	 * @return
	 */
	public List<Product> loadProductByTeam(final long teamId);
	
	public List<Product> loadProductByTeamOrder(final long teamId);

	// 加载首页 更多视频
	public List<Product> loadData(final ProductView view);

	/**
	 * 根据 行业分类 查询总数
	 * @param view 产品类型
	 * @return 个数
	 */
	public long conditionSize(final ProductView view);

	/**
	 * 获取 已审核的作品
	 * @param teamId 供应商ID
	 * @return 已审核作品集合
	 */
	public List<Product> loadProductByProviderId(final long teamId);

	/**
	 * 更新 视频基本信息，除了 推荐值等外
	 */
	public long updateProductInfo(final Product product);

	/**
	 * 获取单个作品ID
	 * @param teamId 供应商唯一编号
	 * @return 作品ID
	 */
	public Product loadSingleProduct(final long teamId);

	/**
	 * 获取PC端主页视频
	 * @param view 包含上界及下界
	 * @return 视频列表
	 */
	public List<Product> loadProductByCommend();

	// add by wliming, 2016/02/24 18:54 begin
	// -> 增加信息模板的更新方法
	public void updateVideoDescription(final Product product);
	// add by wliming, 2016/02/24 18:54 end

	/**
	 * 查询BD产品营销页面产品信息
	 * @return 产品列表
	 */
	public List<Product> loadSalesProduct();
	
	/**
	 * 查询推荐值大于0的产品集合
	 * @return 产品集合
	 */
	public Map<Long,Product> getProductByRecommend();

	public Product getMasterWork(long teamId);
}

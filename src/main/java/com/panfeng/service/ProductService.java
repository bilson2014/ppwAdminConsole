package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.resource.model.Product;
import com.panfeng.resource.view.ProductView;

public interface ProductService {

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
	 * 获取单个作品ID
	 * @param teamId 供应商唯一编号
	 * @return 作品ID
	 */
	public Product loadSingleProduct(final long teamId);

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

	public List<Product> loadActivityProducts();

	/**
	 * 历史视频转化临时SQL语句
	 * @return
	 */
	public List<Product> selectFilmToConvert();
}

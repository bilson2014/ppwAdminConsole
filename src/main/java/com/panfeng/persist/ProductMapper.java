package com.panfeng.persist;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.Product;
import com.panfeng.resource.view.ProductView;

public interface ProductMapper {

	public List<Product> listWithPagination(final ProductView view);
	
	public long save(final Product product);
	
	public long update(final Product product);
	
	public long delete(@Param("productId") final long productId);
	
	public long maxSize(final ProductView view);

	/**
	 * 根据 ID数组 获取 team列表
	 * @param ids ID数组
	 * @return team列表
	 */
	public List<Product> findProductByArray(@Param("ids") final long[] ids);
	
	public List<Product> findProductByIds(@Param("ids") final long[] ids);

	public Product findProductById(@Param("productId") final long productId);

	public long updateFileUrl(final Product product);

	public List<Product> all();
	
	// ------------------ 以下是 前端 展示 内容 ----------------------------
	public List<Product> listWithCondition(final ProductView view);

	public Product loadProduct(@Param("productId") final Integer productId);

	public List<Product> loadProductByTeam(@Param("teamId") final long teamId);
	
	public List<Product> loadProductByTeamOrder(@Param("teamId") final long teamId);

	public List<Product> loadData(final ProductView view);
	
	/**
	 * 根据 teamId 删除作品
	 * @param teamId 团队唯一编号
	 */
	public long deleteByTeamId(@Param("teamId") final long teamId);

	/**
	 * 根据 产品类型 查询总数
	 * @param view 包含产品类型
	 * @return 个数
	 */
	public long conditionSize(final ProductView view);

	/**
	 * 更新 视频基本信息，除了 推荐值等外
	 */
	public long updateProductInfo(final Product product);

	/**
	 * 获取单个作品ID
	 * @param teamId 供应商唯一编号
	 * @return 作品ID
	 */
	public Product loadSingleProduct(@Param("teamId") final long teamId);

	/**
	 * 获取供应商视频列表
	 * @param teamId
	 * @return
	 */
	public List<Product> loadProductByProviderId(@Param("teamId") final long teamId);

	/**
	 * 加载PC端视频列表
	 * @param view 包含上界与下界
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
	@MapKey(value = "productId")
	public Map<Long, Product> getProductByRecommend();
	
	/**
	 * 获取代表作 
	 * @param productId
	 * @return
	 */
	public Product getMasterWork(@Param("teamId")long teamId);
	/**
	 * 查询推荐值大于0的分页集合
	 */
	public List<Product> searchPageRecommendList(ProductView view);

	public long maxRecommendSize(ProductView view);

	/**
	 * 修改作品推荐值
	 */
	public long updateRecommend(Product product);
	/**
	 * 修改作品可见性
	 */
	public long updateProductVisibility(Product product);
	
	/**
	 * 历史视频转化临时SQL语句
	 * @return
	 */
	public List<Product> selectFilmToConvert();

}

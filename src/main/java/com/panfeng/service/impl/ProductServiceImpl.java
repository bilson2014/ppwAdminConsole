package com.panfeng.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.ProductMapper;
import com.panfeng.persist.ServiceMapper;
import com.panfeng.persist.TeamMapper;
import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.ProductView;
import com.panfeng.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private final ProductMapper mapper = null;

	@Autowired
	private final ServiceMapper scMapper = null;

	@Autowired
	private final TeamMapper teamMapper = null;

	/*@Override
	public List<Product> listWithPagination(final ProductView view) {

		final List<Product> list = mapper.listWithPagination(view);
		return list;
	}*/

	/*@Override
	public long save(final Product product) {

		mapper.save(product);
		return product.getProductId();
	}*/

	/*@Transactional
	public List<Product> delete(final long[] ids) {
		final List<Product> originalList = mapper.findProductByArray(ids);
		if (ids.length > 0) {
			for (long id : ids) {
				mapper.delete(id);
				scMapper.deleteByProduct(id);
			}
		}
		return originalList;
	}*/

	/*@Override
	public long update(final Product product) {

		final long ret = mapper.update(product);
		return ret;
	}*/

	/*@Override
	public long maxSize(final ProductView view) {

		final long total = mapper.maxSize(view);
		return total;
	}*/

	/*@Override
	public Product findProductById(final long productId) {

		final Product product = mapper.findProductById(productId);
		return product;
	}*/

	/*@Override
	public long saveFileUrl(final Product product) {

		final long ret = mapper.updateFileUrl(product);
		return ret;
	}*/

/*	@Override
	public List<Product> all() {

		final List<Product> list = mapper.all();
		return list;
	}*/

	@Override
	public List<Product> listWithCondition(final ProductView view) {

		final List<Product> list = mapper.listWithCondition(view);
		return mergeService(list);
	}

	@Override
	public Product loadProduct(final Integer productId) {

		final Product product = mapper.loadProduct(productId);
		com.panfeng.resource.model.Service service = scMapper.loadSingleService(productId);
		if (product != null) {
			if (service != null) {
				product.setServiceId(service.getServiceId());
				product.setServicePrice(service.getServicePrice());
				product.setServiceRealPrice(service.getServiceRealPrice());
				product.setMcoms(service.getMcoms());
				product.setPriceDetail(service.getPriceDetail());
			}
			if (product.getTeamId() != null && !"".equals(product.getTeamId())) {
				Team team = teamMapper.findTeamById(product.getTeamId());
				if (team != null) {
					product.setTeamDescription(team.getTeamDescription());
					product.setTeamName(team.getTeamName());
					product.setTeamPhotoUrl(team.getTeamPhotoUrl());
				}
			}

		}
		return product;
	}

	@Override
	public List<Product> loadProductByTeam(final long teamId) {

		final List<Product> list = mapper.loadProductByTeam(teamId);
		return mergeService(list);
	}

	@Override
	public List<Product> loadProductByTeamOrder(final long teamId) {
		final List<Product> list = mapper.loadProductByTeamOrder(teamId);
		return list;
	}

	@Override
	public List<Product> loadData(final ProductView view) {

		final List<Product> list = mapper.loadData(view);
		return list;
	}

	// 添加 service 信息
	public List<Product> mergeService(final List<Product> list) {
		// 装配 服务价格
		for (final Product product : list) {
			final com.panfeng.resource.model.Service service = scMapper.loadSingleService(product.getProductId());
			if (service != null) {
				product.setServiceId(service.getServiceId());
				product.setServicePrice(service.getServicePrice());
				product.setServiceRealPrice(service.getServiceRealPrice());
			}
		}
		return list;
	}

	public long conditionSize(final ProductView view) {

		final long size = mapper.conditionSize(view);
		return size;
	}

	/*@Override
	public List<Product> loadProductByProviderId(final long teamId) {

		final List<Product> list = mapper.loadProductByProviderId(teamId);
		return list;
	}
*/
/*	@Override
	public long updateProductInfo(final Product product) {

		final long ret = mapper.updateProductInfo(product);
		return ret;
	}*/

	@Override
	public Product loadSingleProduct(long teamId) {

		final Product product = mapper.loadSingleProduct(teamId);
		return product;
	}

	/*@Override
	public List<Product> loadProductByCommend() {

		final List<Product> list = mapper.loadProductByCommend();
		return list;
	}
*/
	/*@Override
	public void updateVideoDescription(final Product product) {
		mapper.updateVideoDescription(product);
	}*/

	@Override
	public List<Product> loadSalesProduct() {

		final List<Product> list = mapper.loadSalesProduct();
		for (final Product product : list) {
			com.panfeng.resource.model.Service service = scMapper.loadSingleService(product.getProductId());
			product.setServiceId(service.getServiceId());
			product.setServiceRealPrice(service.getServiceRealPrice());
			product.setServicePrice(service.getServicePrice());
		}
		return list;
	}

	@Override
	public Map<Long, Product> getProductByRecommend() {

		final Map<Long, Product> map = mapper.getProductByRecommend();
		return map;
	}

	/*@Override
	public Product getMasterWork(long teamId) {
		return mapper.getMasterWork(teamId);
	}*/

	public List<Product> loadActivityProducts() {
		String product_ids = GlobalConstant.ACTIVITY_PRODUCT_IDS;
		String[] ids = product_ids.split("\\|");
		long[] lids = new long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			lids[i] = Long.parseLong(ids[i]);
		}
		return mapper.findProductByIds(lids);
	}


	/**
	 * 查找分页的作品，分页
	 */
	/*@Override
	public List<Product> searchPageRecommendList(ProductView view) {
		final List<Product> list = mapper.searchPageRecommendList(view);
		return list;
	}*/

	/*@Override
	public long maxRecommendSize(ProductView view) {
		final long total = mapper.maxRecommendSize(view);
		return total;
	}*/
	/**
	 * 修改作品的推荐值
	 */
	/*@Override				
	public boolean updateRecommend(Product product) {
		long l = mapper.updateRecommend(product);
		return l>=0;
	}*/

	/**
	 * 修改作品可见性
	 */
	/*@Override
	public boolean updateProductVisibility(Product product) {
		long l = mapper.updateProductVisibility(product);
		return l>0?true:false;
	}*/
}

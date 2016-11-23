package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.ProductModule;

public interface ProductModuleMapper {

	List<ProductModule> list();

	long save(ProductModule productModule);

	int update(ProductModule productModule);

	void delete(@Param("id") final long id);

	String getchild(@Param("id") final long id);

	String getPic(@Param("id") final long id);

	List<ProductModule> findListByIds(@Param("ids")Long[] ids);

}

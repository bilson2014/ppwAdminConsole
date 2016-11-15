package com.panfeng.persist;

import java.util.List;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.Pagination;

public interface NewsMapper {

	List<News> listWithPagination(Pagination pagination);

}

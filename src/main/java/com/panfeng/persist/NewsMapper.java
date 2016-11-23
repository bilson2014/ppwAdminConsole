package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.News;
import com.panfeng.resource.view.Pagination;

public interface NewsMapper {

	List<News> listWithPagination(Pagination pagination);

	long maxSize(Pagination pagination);

	int getMaxIndex();

	void save(News news);

	void update(News news);

	News getNewsById(@Param("id") final int id);

	void delete(@Param("id") int id);

	void promoteAllAboveByIndex(@Param("sortIndex") Integer sortIndex);

	int downSortBySort(@Param("sortIndex") int sortIndex);

	int upSortByNewsId(@Param("id") final int id);

	int upSortBySort(@Param("sortIndex") int sortIndex);

	int downSortByNewsId(@Param("id") final int id);

	List<News> RecommendNews();

	News info(@Param("newId") Integer newId);

}

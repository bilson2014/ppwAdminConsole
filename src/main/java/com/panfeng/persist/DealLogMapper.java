package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.DealLog;

public interface DealLogMapper {


	long save(DealLog dealLog);

	long delete(@Param("dealId") long dealId);

	long update(DealLog dealLog);

	DealLog findDealById(@Param("dealId") long dealId);

	DealLog findDealByBillNo(@Param("billNo") String billNo);

	List<DealLog> findAll();
}

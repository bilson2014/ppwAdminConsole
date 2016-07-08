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

	List<DealLog> findDealByProjectId(@Param("projectId") long projectId);

	List<DealLog> findDealByUserId(@Param("projectId") long projectId, @Param("userId") long userId);

	List<DealLog> findAll();

	long orderNumber(@Param("projectId") long projectId);
	
	/**
	 * 根据用户来监测是否有为支付订单
	 * @param userType
	 * @param userId
	 * @return
	 */
	long notPayNumber(@Param("userType") String userType,@Param("userId") long userId);
	
	/**
	 * 根据项目来监测是否有为支付订单
	 * @param projectId
	 * @return
	 */
	long notPayNumberByProjectId(@Param("projectId") long projectId);

}

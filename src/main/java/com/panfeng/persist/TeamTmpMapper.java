package com.panfeng.persist;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.panfeng.resource.model.TeamTmp;
import com.panfeng.resource.view.MailView;

public interface TeamTmpMapper {

	void delTeamMapperByTeamId(TeamTmp tmp);

	void addTeamTmp(TeamTmp tmp);

	List<TeamTmp> listWithPagination(MailView view);

	long maxSize(MailView view);

	TeamTmp getTeamTmpById(@Param("id") final Integer id);

	long updateTeamTmpCheck(TeamTmp teamTmp);

	List<TeamTmp> getTeamTmpByTeamId(@Param("teamId") final Integer teamId);

	/**
	 * 是否含有供应商最新审核信息
	 */
	List<TeamTmp> doesHaveLatestEnableTmpByTeamId(@Param("teamId") final Long teamId);
	
}

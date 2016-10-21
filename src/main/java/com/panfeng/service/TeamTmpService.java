package com.panfeng.service;

import java.util.List;

import com.panfeng.resource.model.DIffBean;
import com.panfeng.resource.model.TeamTmp;
import com.panfeng.resource.view.MailView;

public interface TeamTmpService {

	List<TeamTmp> listWithPagination(MailView view);

	long maxSize(MailView view);

	TeamTmp getTeamTmpById(Integer id);

	boolean updateTeamTmpCheck(TeamTmp teamTmp);

	void updateTeamTmp(TeamTmp teamTmp);

	List<DIffBean> findDiffTeam(Integer teamId);

}

package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.facade.team.entity.DIffBean;
import com.paipianwang.pat.facade.team.entity.PmsTeamTmp;
import com.paipianwang.pat.facade.team.service.PmsTeamTmpFacade;

/**
 * 供应商审核
 */
@RestController
@RequestMapping("/portal")
public class TeamTmpController extends BaseController {

	@Autowired
	private final PmsTeamTmpFacade pmsTeamTmpFacade = null;

	@RequestMapping(value = "/teamTmp-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("teamTmp-list", model);
	}

	@RequestMapping(value = "/teamTmp/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsTeamTmp> list(final PageParam pageParam) {

		final long page = pageParam.getPage();
		final long rows = pageParam.getRows();
		pageParam.setBegin((page - 1) * rows);
		pageParam.setLimit(rows);
		final DataGrid<PmsTeamTmp> dataGrid = pmsTeamTmpFacade.listWithPagination(pageParam);
		return dataGrid;
	}

	@RequestMapping(value = "/teamTmp/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void update(PmsTeamTmp teamTmp, HttpServletRequest reqeust) {
		pmsTeamTmpFacade.updateTeamTmp(teamTmp);
	}

	/**
	 * 查询team和teamTmp的差异字段
	 */
	@RequestMapping(value = "/teamTmp/find/diff/{teamId}")
	public List<DIffBean> findDiffTeam(@PathVariable("teamId") Integer teamId, HttpServletRequest reqeust) {
		return pmsTeamTmpFacade.findDiffTeam(teamId);
	}
}

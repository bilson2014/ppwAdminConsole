package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.panfeng.persist.TeamMapper;
import com.panfeng.persist.TeamTmpMapper;
import com.panfeng.resource.model.DIffBean;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.TeamTmp;
import com.panfeng.resource.view.Pagination;
import com.panfeng.service.TeamTmpService;
import com.panfeng.util.Constants;

@Service
public class TeamTmpServiceImpl implements TeamTmpService{
	
	@Autowired
	private TeamTmpMapper teamTmpMapper;
	@Autowired
	private TeamMapper teamMapper;

	@Override
	public List<TeamTmp> listWithPagination(Pagination view) {
		final List<TeamTmp> list = teamTmpMapper.listWithPagination(view);
		return list;
	}

	@Override
	public long maxSize(Pagination view) {
		final long total = teamTmpMapper.maxSize(view);
		return total;
	}

	@Override
	public TeamTmp getTeamTmpById(Integer id) {
		return teamTmpMapper.getTeamTmpById(id);
	}

	@Override
	public boolean updateTeamTmpCheck(TeamTmp teamTmp) {
		return teamTmpMapper.updateTeamTmpCheck(teamTmp)>0?true:false;
	}

	@Override
	public void updateTeamTmp(TeamTmp teamTmp) {
		//1.修改teamTmp审核状态和审核信息
		teamTmpMapper.updateTeamTmpCheck(teamTmp);
		//2.如果审核通过,修改team表
		if(teamTmp.getCheckStatus() == 1){
			TeamTmp tmp = teamTmpMapper.getTeamTmpById(teamTmp.getId());
			Team team = teamMapper.findTeamById(tmp.getTeamId());
			team = moveInfoToTeam(team,tmp);
			teamMapper.update(team);
		}
	}

	private Team moveInfoToTeam(Team team, TeamTmp tmp) {
		team.setAddress(tmp.getAddress());
		team.setBusiness(tmp.getBusiness());
		team.setBusinessDesc(tmp.getBusinessDescription());
		team.setTeamCity(tmp.getTeamCity());
		team.setTeamProvince(tmp.getTeamProvince());
		team.setTeamName(tmp.getTeamName());
		team.setLinkman(tmp.getLinkMan());
		team.setWebchat(tmp.getWebchat());
		team.setQq(tmp.getQq());
		team.setEmail(tmp.getEmail());
		team.setPriceRange(tmp.getPriceRange());
		team.setInfoResource(tmp.getInfoResource());
		team.setTeamDescription(tmp.getTeamDescription());
		team.setScale(tmp.getScale());
		team.setDemand(tmp.getDemand());
		team.setEstablishDate(tmp.getEstablishDate());
		team.setOfficialSite(tmp.getOfficialSite());
		team.setTeamPhotoUrl(tmp.getTeamPhotoUrl());
		return team;
	}

	@Override
	public List<DIffBean> findDiffTeam(Integer teamId) {
		List<DIffBean> list = new ArrayList<DIffBean>();
		List<TeamTmp> tmpList = teamTmpMapper.getTeamTmpByTeamId(teamId);
		if(null!=tmpList && tmpList.size()>0){
			TeamTmp tmp = tmpList.get(0);
			Team team = teamMapper.findTeamById(teamId);
			list = findDiffProperty(tmp,team);
		}
		return list;
	}

	private List<DIffBean> findDiffProperty(TeamTmp tmp, Team team) {
		List<DIffBean> list = new ArrayList<DIffBean>();
		String tmp_team = null == tmp.getTeamName()?"":tmp.getTeamName();
		String _team = null == team.getTeamName()?"":team.getTeamName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamName");
			bean.setPropertyName("公司名称");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getLinkMan()?"":tmp.getLinkMan();
		_team = null == team.getLinkman()?"":team.getLinkman();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("linkman");
			bean.setPropertyName("联系人");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		
		tmp_team = null == tmp.getWebchat()?"":tmp.getWebchat();
		_team = null == team.getWebchat()?"":team.getWebchat();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("webchat");
			bean.setPropertyName("微信");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		
		tmp_team = null == tmp.getQq()?"":tmp.getQq();
		_team = null == team.getQq()?"":team.getQq();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("qq");
			bean.setPropertyName("QQ");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getEmail()?"":tmp.getEmail();
		_team = null == team.getEmail()?"":team.getEmail();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("email");
			bean.setPropertyName("邮箱");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getAddress()?"":tmp.getAddress();
		_team = null == team.getAddress()?"":team.getAddress();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("address");
			bean.setPropertyName("地址");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamProvinceName()?"":tmp.getTeamProvinceName();
		_team = null == team.getTeamProvinceName()?"":team.getTeamProvinceName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamProvince");
			bean.setPropertyName("省份");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamCityName()?"":tmp.getTeamCityName();
		_team = null == team.getTeamCityName()?"":team.getTeamCityName();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamCity");
			bean.setPropertyName("城市");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = tmp.getPriceRange().toString();
		_team = String.valueOf(team.getPriceRange());
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("priceRange");
			bean.setPropertyName("价格区间");
			bean.setOldValue(Constants.PRICE_RANGE_MAP.get(_team));
			bean.setNewValue(Constants.PRICE_RANGE_MAP.get(tmp_team));
			list.add(bean);
		}
		tmp_team = tmp.getInfoResource().toString();
		 _team = String.valueOf(team.getInfoResource());
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("infoResource");
			bean.setPropertyName("获知渠道");
			bean.setOldValue(Constants.INFO_RESOURCE_MAP.get(_team));
			bean.setNewValue(Constants.INFO_RESOURCE_MAP.get(tmp_team));
			list.add(bean);
		}
		tmp_team = null == tmp.getBusiness()?"":tmp.getBusiness();
		_team = null == team.getBusiness()?"":team.getBusiness();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("business");
			bean.setPropertyName("业务范围");
			String[] _teamArray = _team.split(",");
			String[] tmp_teamArray = tmp_team.split(",");
			tmp_team = "";
			_team = "";
			for(String s : _teamArray){
				_team += Constants.BUSINESS_MAP.get(s)+",";
			}
			for(String s : tmp_teamArray){
				tmp_team += Constants.BUSINESS_MAP.get(s)+",";
			}
			bean.setOldValue(_team.substring(0, _team.lastIndexOf(",")));
			bean.setNewValue(tmp_team.substring(0, tmp_team.lastIndexOf(",")));
			list.add(bean);
		}
		tmp_team = null == tmp.getTeamDescription()?"":tmp.getTeamDescription();
		_team = null == team.getTeamDescription()?"":team.getTeamDescription();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamDescription");
			bean.setPropertyName("公司简介");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getScale()?"":tmp.getScale();
		_team = null == team.getScale()?"":team.getScale();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("scale");
			bean.setPropertyName("公司规模");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getDemand()?"":tmp.getDemand();
		_team = null == team.getDemand()?"":team.getDemand();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("demand");
			bean.setPropertyName("客户要求");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getEstablishDate()?"":tmp.getEstablishDate();
		_team = null == team.getEstablishDate()?"":team.getEstablishDate();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("establishDate");
			bean.setPropertyName("成立时间");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		tmp_team = null == tmp.getOfficialSite()?"":tmp.getOfficialSite();
		_team = null == team.getOfficialSite()?"":team.getOfficialSite();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("officialSite");
			bean.setPropertyName("公司官网");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		 tmp_team = null == tmp.getBusinessDescription()?"":tmp.getBusinessDescription();
		 _team = null == team.getBusinessDesc()?"":team.getBusinessDesc();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamName");
			bean.setPropertyName("公司名称");
			bean.setOldValue(_team);
			bean.setNewValue(tmp_team);
			list.add(bean);
		}
		
		tmp_team = null == tmp.getTeamPhotoUrl()?"":tmp.getTeamPhotoUrl();
		 _team = null == team.getTeamPhotoUrl()?"":team.getTeamPhotoUrl();
		if(tmp_team.compareTo(_team) != 0){
			DIffBean bean = new DIffBean();
			bean.setProperty("teamPhotoUrl");
			bean.setPropertyName("公司Logo");
			bean.setOldValue("更换了新的Logo");
			bean.setNewValue("~");
			list.add(bean);
		}
		return list;
	}
}

package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.TeamMapper;
import com.panfeng.persist.TeamTmpMapper;
import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.model.TeamTmp;
import com.panfeng.resource.view.TeamView;
import com.panfeng.service.TeamService;
import com.panfeng.util.TeamBusiness;
import com.panfeng.util.ValidateUtil;

@Service
public class TeamServiceImpl implements TeamService {
	private static Logger logger = LoggerFactory.getLogger("error");
	@Autowired
	private final TeamMapper mapper = null;
	@Autowired
	private final TeamTmpMapper teamTmpMapper = null;

	public List<Team> listWithPagination(final TeamView view) {

		final List<Team> list = mapper.listWithPagination(view);
		return list;
	}

	@Override
	public long save(final Team team) {
		mapper.save(team);
		return team.getTeamId();
	}

	@Transactional
	public List<Team> delete(final long[] ids) {

		final List<Team> lists = mapper.findTeamByArray(ids);

		for (long id : ids) {
			final long ret = mapper.delete(id);
			if (ret > -1)
				continue;
			else
				new RuntimeException("delete team error ...");
		}

		return lists;
	}

	@Override
	public long update(final Team team) {

		return mapper.update(team);
	}

	@Override
	public long maxSize(final TeamView view) {

		final long size = mapper.maxSize(view);
		return size;
	}

	@Override
	public Team findTeamById(final long id) {

		final Team team = mapper.findTeamById(id);
		return team;
	}

	@Override
	public long saveTeamPhotoUrl(final Team team) {

		final long ret = mapper.saveTeamPhotoUrl(team);
		return ret;
	}

	@Override
	public List<Team> getAll() {

		final List<Team> list = mapper.getAll();
		return list;
	}

	@Override
	public Team doLogin(final String phoneNumber) {

		final List<Team> team = mapper.checkTeam(phoneNumber);
		if (team != null) {
			if (team.size() == 1) {
				return team.get(0);
			} else {
				logger.error("数据库中存在多个相同手机号：" + phoneNumber);
			}
		} else {
			logger.error("数据库中不存在该供应商 手机号：" + phoneNumber);
		}
		return null;
	}

	@Override
	public long checkExist(final Team team) {

		final TeamView view = new TeamView();
		if (team.getPhoneNumber() != null && !"".equals(team.getPhoneNumber())) {

			view.setPhoneNumber(team.getPhoneNumber());
		}

		if (team.getLoginName() != null && !"".equals(team.getLoginName())) {

			view.setLoginName(team.getLoginName());
		}

		final long count = mapper.checkExist(view);
		return count;
	}

	@Override
	public Team register(final Team original) {

		final long ret = mapper.save(original);
		if (ret == 1)
			return original;
		else
			return null;
	}

	@Override
	public long recover(final Team original) {

		final long ret = mapper.recover(original);
		return ret;
	}

	@Override
	public long updateTeamInfomation(final Team team) {

		final long ret = mapper.updateTeamInfomation(team);
		return ret;
	}

	@Override
	public long updatePasswordByLoginName(final Team team) {

		final long ret = mapper.updatePasswordByLoginName(team);
		return ret;
	}

	@Override
	public long updateTeamStatus(final long teamId) {

		final long ret = mapper.updateTeamStatus(teamId);
		return ret;
	}

	@Override
	public List<Team> findTeamByName(final Team team) {

		return mapper.findTeamByNameOrContact(team);
	}

	@Override
	public List<Team> verificationTeamExist(final Team team) {

		final List<Team> list = mapper.verificationTeamExist(team);
		return list;
	}

	@Override
	public long updateUniqueId(final Team provider) {

		final long ret = mapper.updateUniqueId(provider);
		return ret;
	}

	@Override
	public BaseMsg bind(Team provider) {
		BaseMsg baseMsg = null;
		List<Team> teams = mapper.verificationTeamExist(provider);
		if (ValidateUtil.isValid(teams)) { // 要么绑定，要么新建
			// 绑定
			switch (provider.getThirdLoginType()) {
			case Team.LTYPE_QQ:
				for (Team team : teams) {
					// 检测所偶有QqUnique相同的team，切QqUnique与绑定QQ相同
					if (provider.getQqUnique().equals(team.getQqUnique())) {
						if (!ValidateUtil.isValid(team.getPhoneNumber())) {
							// 允许绑定
							team.setQqUnique(provider.getQqUnique());
							team.setPhoneNumber(provider.getPhoneNumber());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else if (team.getPhoneNumber().equals(provider.getPhoneNumber())) {
							// 已经绑定过相同的qq
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else {
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定失败", null);
							// 该QQ 号已经绑定过其他手机---》肯定不会出现
						}
					}
				}
				break;
			case Team.LTYPE_WECHAT:
				for (Team team : teams) {
					// 检测所偶有wechatUnique相同的team，切wechatUnique与绑定微信相同
					if (provider.getWechatUnique().equals(team.getWechatUnique())) {
						if (!ValidateUtil.isValid(team.getPhoneNumber())) {
							// 允许绑定
							team.setWechatUnique(provider.getWechatUnique());
							team.setPhoneNumber(provider.getPhoneNumber());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else if (team.getPhoneNumber().equals(provider.getPhoneNumber())) {
							// 已经绑定过相同的qq
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else {
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定失败", null);
							// 该QQ 号已经绑定过其他手机---》肯定不会出现
						}
					}
				}
				break;
			case Team.LTYPE_WEIBO:
				for (Team team : teams) {
					// 检测所偶有wbUnique相同的team，切wbUnique与绑定微博相同
					if (provider.getWbUnique().equals(team.getWbUnique())) {
						if (!ValidateUtil.isValid(team.getPhoneNumber())) {
							// 允许绑定
							team.setWbUnique(provider.getWbUnique());
							team.setPhoneNumber(provider.getPhoneNumber());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else if (team.getPhoneNumber().equals(provider.getPhoneNumber())) {
							// 已经绑定过相同的qq
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						} else {
							baseMsg = new BaseMsg(BaseMsg.ERROR, "绑定失败", null);
							// 该QQ 号已经绑定过其他手机---》肯定不会出现
						}
					}
				}
				break;
			}
		} else {
			// 新建
			String phoneNumber = provider.getPhoneNumber();
			final List<Team> teamsPhone = mapper.checkTeam(phoneNumber);
			if (teamsPhone.size() < 2) {
				if (teamsPhone != null && teamsPhone.size() > 0) {
					Team team = teamsPhone.get(0);
					switch (provider.getThirdLoginType()) {
					case Team.LTYPE_QQ:
						if (ValidateUtil.isValid(team.getQqUnique())) {
							// 数据库中存在QqUnique，此手机号已经被绑定过
							baseMsg = new BaseMsg(BaseMsg.ERROR, "绑定失败，改手机号已经被绑定过了。", null);
						} else {
							// 更新
							team.setQqUnique(provider.getQqUnique());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						}
						break;
					case Team.LTYPE_WECHAT:
						if (ValidateUtil.isValid(team.getWechatUnique())) {
							// 数据库中存在QqUnique，此手机号已经被绑定过
							baseMsg = new BaseMsg(BaseMsg.ERROR, "绑定失败，改手机号已经被绑定过了。", null);
						} else {
							// 更新
							team.setWechatUnique(provider.getWechatUnique());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						}
						break;
					case Team.LTYPE_WEIBO:
						if (ValidateUtil.isValid(team.getWbUnique())) {
							// 数据库中存在QqUnique，此手机号已经被绑定过
							baseMsg = new BaseMsg(BaseMsg.ERROR, "绑定失败，改手机号已经被绑定过了。", null);
						} else {
							// 更新
							team.setWbUnique(provider.getWbUnique());
							mapper.updateUniqueId(team);
							baseMsg = new BaseMsg(BaseMsg.NORMAL, "绑定成功", team);
						}
						break;
					}
				} else {
					// 默认新建时用三方登录名作为公司名
					provider.setTeamName(provider.getPhoneNumber());
					provider.setLoginName(provider.getLinkman());
					baseMsg = new BaseMsg(BaseMsg.WARNING, "引导流程", provider);
				}
			} else {
				logger.error("数据库中存在多个相同手机号：" + phoneNumber);
			}
		}
		return baseMsg;
	}

	@Override
	public Team findTeamByLoginNameAndPwd(Team original) {
		final Team team = mapper.findTeamByLoginNameAndPwd(original);
		return team;
	}

	@Override
	public long updateTeamAccount(Team original) {
		return mapper.updateTeamAccount(original);
	}

	@Override
	@Transactional
	public boolean setMasterWork(Product product) {
		// 重置所有代表作初始值
		mapper.resetMasterWork(product.getTeamId());
		// 更新当前作品master
		mapper.setMasterWork(product);
		return true;
	}

	public Map<String, Object> thirdStatus(Team t) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qq", "0");
		map.put("wechat", "0");
		map.put("wb", "0");
		Team team = mapper.findTeamById(t.getTeamId());
		if (null != team) {
			if (ValidateUtil.isValid(team.getQqUnique())) {
				map.put("qq", "1");
			}
			if (ValidateUtil.isValid(team.getWechatUnique())) {
				map.put("wechat", "1");
			}
			if (ValidateUtil.isValid(team.getWbUnique())) {
				map.put("wb", "1");
			}
		}
		return map;
	}

	@Override
	public boolean teamInfoBind(Team t) {
		// 查询第三方是不是存在绑定
		List<Team> list = mapper.verificationTeamExistByThirdLogin(t);
		if (list.size() > 0) {
			return false;// 已经存在绑定
		} else {
			Team team = mapper.findTeamById(t.getTeamId());
			if (t.getThirdLoginType().equals("qq")) {
				team.setQqUnique(t.getUniqueId());
			} else if (t.getThirdLoginType().equals("weibo")) {
				team.setWbUnique(t.getUniqueId());
			} else if (t.getThirdLoginType().equals("wechat")) {
				team.setWechatUnique(t.getUniqueId());
			}
			if (!ValidateUtil.isValid(team.getTeamName())) {
				team.setTeamName(t.getTeamName());
			}
			mapper.updateUniqueId(team);
			return true;
		}
	}

	@Override
	public boolean teamInfoUnBind(Team team) {
		mapper.unBindThird(team);
		return true;
	}

	@Override
	public long modifyTeamPhone(Team team) {
		return mapper.modifyTeamPhone(team);
	}

	@Override
	public Team getTeamInfo(Long teamId) {
		return mapper.getTeamInfo(teamId);
	}

	@Override
	public List<String> getTags(final List<Integer> ids) {
		List<String> tags = new ArrayList<>();
		for (int i = 0; i < ids.size(); i++) {
			tags.add(TeamBusiness.get(ids.get(i)));
		}
		return tags;
	}

	@Override
	public long updateTeamDescription(Team team) {
		return  mapper.updateTeamDescription(team);
	}

	@Override
	public void dealTeamTmp(Team team) {
		TeamTmp tmp = new TeamTmp();
		tmp.setCheckStatus(0);
		if(null != team.getAddress()){
			tmp.setAddress(team.getAddress());
		}
		if(null != team.getBusiness()){
			tmp.setBusiness(team.getBusiness());
		}
		if(null != team.getBusinessDesc()){
			tmp.setBusinessDescription(team.getBusinessDesc());
		}
		if(null != team.getEmail()){
			tmp.setEmail(team.getEmail());
		}
		if(null != team.getEstablishDate()){
			tmp.setEstablishDate(team.getEstablishDate());
		}
		tmp.setInfoResource(team.getInfoResource());
		if(null != team.getLinkman()){
			tmp.setLinkMan(team.getLinkman());
		}
		if(null != team.getOfficialSite()){
			tmp.setOfficialSite(team.getOfficialSite());
		}
		tmp.setPriceRange(team.getPriceRange());
		
		if(null != team.getQq()){
			tmp.setQq(team.getQq());
		}
		if(null != team.getScale()){
			tmp.setScale(team.getScale());
		}
		tmp.setStatus(true);
		if(null != team.getTeamCity()){
			tmp.setTeamCity(team.getTeamCity());
		}
		if(null != team.getTeamDescription()){
			tmp.setTeamDescription(team.getTeamDescription());
		}
		tmp.setTeamId(team.getTeamId());
		if(null != team.getTeamName()){
			tmp.setTeamName(team.getTeamName());
		}
		if(null != team.getTeamProvince()){
			tmp.setTeamProvince(team.getTeamProvince());
		}
		if(null != team.getWebchat()){
			tmp.setWebchat(team.getWebchat());
		}
		if(null != team.getDemand()){
			tmp.setDemand(team.getDemand());
		}
		if(null != team.getTeamPhotoUrl()){
			tmp.setTeamPhotoUrl(team.getTeamPhotoUrl());
		}
		//删除其他tmp
		teamTmpMapper.delTeamMapperByTeamId(tmp);
		//增加一条记录
		teamTmpMapper.addTeamTmp(tmp);
	}

	@Override
	public Team findLatestTeamById(Long teamId) {
		//查询是否有待审核的供应商
		List<TeamTmp> list = teamTmpMapper.doesHaveLatestEnableTmpByTeamId(teamId);
		if(null != list && list.size() > 0){
			//返回供应商最新信息
			Team team = mapper.getLatestTmpByTeamId(teamId);
			return team;
		}else{
			//返回team信息
			return mapper.findTeamById(teamId);
		}
	}

	
	@Override
	public boolean moveUp(long teamId) {
		Team team = mapper.findTeamById(teamId);
		int index = team.getRecommendSort();
		//1.降低上一个的排序
		int flag1 = mapper.downSortByRecommendSort(index);
		//2.提升当前id的排序
		int flag2 =mapper.upSortByTeamId(teamId);
		return flag1>0 && flag2>0;
	}

	@Override
	public boolean moveDown(long teamId) {
		Team team = mapper.findTeamById(teamId);
		int index = team.getRecommendSort();
		//1.提升上一个的排序
		int flag1 = mapper.upSortByRecommendSort(index);
		//2.降低当前id的排序
		int flag2 =mapper.downSortByTeamId(teamId);
		return flag1>0 && flag2>0;
	}

	@Override
	public boolean delRecommend(long teamId) {
		Team team = mapper.findTeamById(teamId);
		int index = team.getRecommendSort();
		//1.删除当前供应商（不推荐而已）
		int flag1 = mapper.updateRecommendByTeamId(false,teamId);
		//2.提升index之下的所有排序
		int flag2 = mapper.upAllAboveIndex(index);
		return flag1>0 && flag2>=0;
	}

	@Override
	public List<Team> getAllNoRecommend() {
		return mapper.getAllNoRecommend();
	}

	@Override
	public boolean addRecommend(long teamId) {
		return mapper.addRecommend(teamId)>0;
	}

	@Override
	public List<Team> teamRecommendList() {
		return mapper.teamRecommendList();
	}
	
}

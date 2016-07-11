package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.TeamMapper;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.TeamView;
import com.panfeng.service.TeamService;
import com.panfeng.util.ValidateUtil;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private final TeamMapper mapper = null;

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

		final Team team = mapper.checkTeam(phoneNumber);
		return team;
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
			Team team = mapper.checkTeam(phoneNumber);
			if (team != null) {
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
				provider.setTeamName(provider.getLinkman());
				provider.setLoginName(provider.getLinkman());
				mapper.save(provider);
				baseMsg = new BaseMsg(BaseMsg.WARNING, "引导流程", provider);
			}
		}
		return baseMsg;
	}

}

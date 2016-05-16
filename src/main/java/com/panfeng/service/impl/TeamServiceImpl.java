package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.panfeng.persist.TeamMapper;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.TeamView;
import com.panfeng.service.TeamService;

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
		
		for(long id : ids){
			final long ret = mapper.delete(id);
			if(ret > -1)
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
	public Team doLogin(final Team original) {
		
		final Team team = mapper.checkTeam(original);
		return team;
	}

	@Override
	public long checkExist(final Team team) {
		
		final TeamView view = new TeamView();
		if(team.getPhoneNumber() != null && !"".equals(team.getPhoneNumber())){
			
			view.setPhoneNumber(team.getPhoneNumber());
		}
		
		if(team.getLoginName() != null && !"".equals(team.getLoginName())){
			
			view.setLoginName(team.getLoginName());
		}
		
		final long count = mapper.checkExist(view);
		return count;
	}

	@Override
	public Team register(final Team original) {
		
		final long ret = mapper.save(original);
		if(ret == 1)
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

}

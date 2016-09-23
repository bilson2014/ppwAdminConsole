package com.panfeng.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.dao.SessionInfoDao;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Progress;
import com.panfeng.service.SessionInfoService;
import com.panfeng.util.RedisUtils;
import com.panfeng.util.ValidateUtil;

@Service(value = "sessionInfoService")
public class SessionInfoServiceImpl implements SessionInfoService {

	@Autowired
	private final SessionInfoDao dao = null;

	public boolean addSession(final HttpServletRequest request, final Map<String, Object> map) {

		// 检验是否存在
		if (!dao.exitSession(request)) {
			if (ValidateUtil.isValid(map)) {
				final Map<String, String> destMap = RedisUtils.mapToJson(map);
				return dao.addSession(request, destMap);
			}
		}

		return false;
	}

	public Map<String, Object> getSessionWithAllFields(final HttpServletRequest request) {

		final Map<String, String> result = dao.getSessionWithAllFields(request);
		final Map<String, Object> map = new HashMap<String, Object>();
		if (ValidateUtil.isValid(result)) {
			final String infoStr = result.get(GlobalConstant.SESSION_INFO);
			final String processStr = result.get(GlobalConstant.PROCESS_STATUS);

			if (ValidateUtil.isValid(infoStr)) {
				final SessionInfo info = RedisUtils.fromJson(infoStr, SessionInfo.class);
				map.put(GlobalConstant.SESSION_INFO, info);
			}

			if (ValidateUtil.isValid(processStr)) {
				final Progress progress = RedisUtils.fromJson(processStr, Progress.class);
				map.put(GlobalConstant.PROCESS_STATUS, progress);
			}

		}
		return map;
	}

	public Object getSessionWithField(final HttpServletRequest request, final String field) {

		final String str = dao.getSessionWithField(request, field);
		if (ValidateUtil.isValid(str)) {
			if (GlobalConstant.SESSION_INFO.equals(field)) {
				final SessionInfo info = RedisUtils.fromJson(str, SessionInfo.class);
				return info;
			}

			if (GlobalConstant.PROCESS_STATUS.equals(field)) {
				final Progress progress = RedisUtils.fromJson(str, Progress.class);
				return progress;
			}
		}

		return null;
	}

	public void removeSession(final HttpServletRequest request) {

		dao.removeSession(request);
	}

	public void updateSession(final HttpServletRequest request, final String filed, final String value) {

		dao.updateSession(request, filed, value);
	}

}

package com.panfeng.resource.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.entity.PmsTree;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;
import com.panfeng.resource.view.RightView;

/**
 * 权限相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class RightController extends BaseController {

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;

	@RequestMapping("/right-list")
	public ModelAndView view() {

		return new ModelAndView("right-list");
	}

	@RequestMapping("/right/tree")
	public List<PmsTree> rightTree() {

		final List<PmsTree> list = pmsRightFacade.resourceTree();
		return list;
	}

	@RequestMapping("/right/list")
	public List<PmsRight> list(final RightView view) {

		List<PmsRight> list = pmsRightFacade.findAllRights();
		return list;
	}

	@RequestMapping(value = "/right/save", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long save(@RequestBody final PmsRight right) {

		final long ret = pmsRightFacade.save(right);
		return ret;
	}

	@RequestMapping(value = "/right/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long update(@RequestBody final PmsRight right) {

		final long ret = pmsRightFacade.update(right);
		return ret;
	}

	@RequestMapping("/right/delete")
	public long delete(final long[] ids) {

		final long ret = pmsRightFacade.deleteByIds(ids);
		return ret;
	}

	@RequestMapping("/right/menu")
	public List<PmsTree> menu(final HttpServletRequest request) {

		final SessionInfo info = (SessionInfo) request.getSession().getAttribute(PmsConstant.SESSION_INFO);

		if (info != null) {
			if (PmsConstant.ROLE_EMPLOYEE.equals(info.getSessionType())) {
				final List<PmsTree> list = pmsRightFacade.menu(info);
				return list;
			}
		}

		return null;
	}
}

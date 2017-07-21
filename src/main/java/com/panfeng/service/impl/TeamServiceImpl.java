package com.panfeng.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paipianwang.pat.common.util.Constants;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.poi.util.PoiReportUtils;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.panfeng.domain.BaseMsg;
import com.panfeng.persist.TeamMapper;
import com.panfeng.resource.model.Team;
import com.panfeng.service.TeamService;

@Service
public class TeamServiceImpl implements TeamService {
	private static Logger logger = LoggerFactory.getLogger("error");
	@Autowired
	private final TeamMapper mapper = null;

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

	private static String[] header = { "团队名称", "审核状态", "审核意见", "业务", "技能", "产品线", "团队性质", "价格范围", "所在省", "所在市", "通讯地址",
			"成立时间", "联系人" };
	/**
	 * 供应商导出
	 */
	@Override
	public void generateReport(List<PmsTeam> list, OutputStream os) {
		// 创建文档
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		// 创建一个新的页
		XSSFSheet sheet = xssfWorkbook.createSheet("供应商列表信息");
		// 生成头部信息
		PoiReportUtils.generateHeader(new ArrayList<String>(Arrays.asList(header)), xssfWorkbook, sheet);

		// 生成数据信息
		this.generateTeamContent(list, xssfWorkbook, sheet);

		try {
			xssfWorkbook.write(os);
			xssfWorkbook.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 导出各行数据
	 * @param list
	 * @param workbook
	 * @param sheet
	 */
	private void generateTeamContent(List<PmsTeam> list, XSSFWorkbook workbook, XSSFSheet sheet) {
		if(ValidateUtil.isValid(list)){
			for(int i=0;i<list.size();i++){
				PmsTeam team=list.get(i);
				XSSFRow xssfRow=sheet.createRow(i+1);
				
				// 样式
				XSSFCellStyle cs = PoiReportUtils.getLeftCellStyle(workbook);
				
				XSSFCell xssfCell=xssfRow.createCell(0);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellType(XSSFCell.CELL_TYPE_STRING);
				xssfCell.setCellValue(team.getTeamName());
				
				String flag="";//审核状态
				switch(team.getFlag()){
					case 0:
						flag="审核中";
						break;
					case 1:
						flag="审核通过";
						break;
					case 2:
						flag="未审核通过";
						break;
					case 3:
						flag="幽灵模式";
						break;
				}
				xssfCell=xssfRow.createCell(1);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(flag);
				
				xssfCell=xssfRow.createCell(2);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getRecommendation());
				
				xssfCell=xssfRow.createCell(3);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getBusiness());
				
				xssfCell=xssfRow.createCell(4);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getSkill());
				
				xssfCell=xssfRow.createCell(5);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getProductLineName());
				
				//团队性质
				String teamNature="";
				switch(team.getTeamNature()){
				case 0:
					teamNature="公司";
					break;
				case 1:
					teamNature="工作室";
					break;
				}
				xssfCell=xssfRow.createCell(6);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(teamNature);
				
				//价格区间
				String priceRange="";
				switch (team.getPriceRange()) {
				case 0:
					priceRange="看情况";
					break;
				case 1:
					priceRange=">= 1W";
					break;
				case 2:
					priceRange=">= 2W";
					break;
				case 3:
					priceRange=">= 3W";
					break;
				case 4:
					priceRange=">= 5W";
					break;
				case 5:
					priceRange=">= 10W";
					break;
				

				default:
					break;
				}
				xssfCell=xssfRow.createCell(7);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(priceRange);
				
				xssfCell=xssfRow.createCell(8);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getTeamProvinceName());
				
				xssfCell=xssfRow.createCell(9);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getTeamCityName());
				
				xssfCell=xssfRow.createCell(10);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getAddress());
				
				xssfCell=xssfRow.createCell(11);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getEstablishDate());
				
				xssfCell=xssfRow.createCell(12);
				xssfCell.setCellStyle(cs);
				xssfCell.setCellValue(team.getLinkman());
				
			}
		}
		
	}
}

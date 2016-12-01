package com.panfeng.resource.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.ResourceToken;
import com.panfeng.resource.model.Solr;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.SolrView;
import com.panfeng.service.SolrService;

/**
 * 搜索相关
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class SolrController extends BaseController {

	@Autowired
	private final SolrService service = null;
	
	@RequestMapping("/solr-list")
	public ModelAndView SolrView(){
		
		return new ModelAndView("solr-list");
	}
	
	@RequestMapping("/solr")
	public DataGrid<Solr> query(@RequestBody final SolrView view){
		
		String condition = view.getCondition();
		//condition = HanlpUtil.segment(condition);

		final SolrQuery query = new SolrQuery();
		query.set("defType", "dismax");
		query.set("qf", "productName^4 tags^3 teamName^2 pDescription^1");
		query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
		query.setFields("teamId,productId,productName,productType,itemName,teamName,orignalPrice,price,picLDUrl,length,pDescription,tags");
		query.setStart(Integer.parseInt(String.valueOf(view.getBegin())));
		query.setRows(Integer.parseInt(String.valueOf(view.getLimit())));
		final String priceFq = view.getPriceFq(); // 视频区间筛选条件
		final String lengthFq = view.getLengthFq(); // 时长区间筛选条件
		final String itemFq = view.getItemFq(); // 视频类型筛选条件
		
		// 如果价格区间为空，则设置为全部
		if(priceFq == null || "".equals(priceFq)){
			view.setPriceFq("[0 TO *]");
		}
		
		// 如果时长区间为空，则设置为全部
		if(lengthFq == null || "".equals(lengthFq)){
			view.setLengthFq("[0 TO *]");
		}
		
		// 如果视频类型为空，则设置为全部
		if(itemFq == null || "".equals(itemFq)){
			view.setItemFq("*");
		}
		
		query.addFilterQuery("price:" + view.getPriceFq());
		query.addFilterQuery("length:" + view.getLengthFq());
		query.addFilterQuery("productType:" + view.getItemFq());
		
		// 开启高亮
		query.setHighlight(true);
		query.addHighlightField("productName,pDescription");
		query.set("hl.highlightMultiTerm", true);
		query.setHighlightFragsize(30);
		query.setHighlightSimplePre("<font color=\"red\">");
		query.setHighlightSimplePost("</font>");
		
		// 设置排序规则-按照价格升序排列
		//query.setSort("productName", ORDER.desc);
		final List<Solr> list = service.queryDocs(GlobalConstant.SOLR_URL, query);
		long total = 0l;
		if(list != null && !list.isEmpty()){
			final Solr solr = list.get(0);
			if(solr != null ){
				total = solr.getTotal();
			}
		}
		
		DataGrid<Solr> dataGrid = new DataGrid<Solr>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}
	
	@RequestMapping("/solr/query")
	public List<Solr> search(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
			
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			
			//condition = HanlpUtil.segment(condition);

			final SolrQuery query = new SolrQuery();
			query.set("qf", "productName^10 tags^6  teamName^4 pDescription^1");
			query.set("q.alt", "*:*");
			query.set("defType", "edismax");
			
			if("*".equals(condition)){
				// 查询全部
				query.setQuery("*:*");
			}else {
				
				//query.set("defType", "dismax");
				// query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
				query.setQuery(condition);
			}
			query.setFields("teamId,productId,productName,productType,itemName,teamName,orignalPrice,price,picLDUrl,length,pDescription,tags");
			query.setStart(Integer.parseInt(String.valueOf(view.getBegin())));
			query.setRows(Integer.parseInt(String.valueOf(view.getLimit())));
			// 如果价格区间为空，则设置为全部
			if(view.getPriceFq() == null || "".equals(view.getPriceFq())){
				view.setPriceFq("[0 TO *]");
			}
			
			// 如果时长区间为空，则设置为全部
			if(view.getLengthFq() == null || "".equals(view.getLengthFq())){
				view.setLengthFq("[0 TO *]");
			}
			
			// 如果视频类型为空，则设置为全部
			if(view.getItemFq() == null || "".equals(view.getItemFq())){
				view.setItemFq("*");
			}
			
			query.addFilterQuery("price:" + view.getPriceFq());
			query.addFilterQuery("length:" + view.getLengthFq());
			query.addFilterQuery("productType:" + view.getItemFq());
			// 设置排序规则-按照价格升序排列
			//query.setSort("productName", ORDER.desc);
			
			// 开启高亮
			query.setHighlight(true);
			query.addHighlightField("productName,pDescription");
			query.set("hl.highlightMultiTerm", true);
			query.setHighlightFragsize(30);
			query.setHighlightSimplePre("<font color=\"red\">");
			query.setHighlightSimplePost("</font>");
			
			final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
			
			return list;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping("/solr/phone/query")
	public List<Solr> phoneSearch(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
			
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			//condition = HanlpUtil.segment(condition);
			
			final SolrQuery query = new SolrQuery();
			/*query.set("defType", "dismax");*/
			query.set("qf", "productName^2 tags^1.5 teamName^1.2 pDescription^1");
			if("*".equals(condition)){
				// 查询全部
				query.setQuery("*:*");
			}else {
				query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
			}
			query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,pDescription,tags");
			query.setStart(0);
			query.setRows(9999);
			// 如果价格区间为空，则设置为全部
			if(view.getPriceFq() == null || "".equals(view.getPriceFq())){
				view.setPriceFq("[0 TO *]");
			}
			
			// 如果时长区间为空，则设置为全部
			if(view.getLengthFq() == null || "".equals(view.getLengthFq())){
				view.setLengthFq("[0 TO *]");
			}
			
			// 如果视频类型为空，则设置为全部
			if(view.getItemFq() == null || "".equals(view.getItemFq())){
				view.setItemFq("*");
			}
			
			query.addFilterQuery("price:" + view.getPriceFq());
			query.addFilterQuery("length:" + view.getLengthFq());
			query.addFilterQuery("productType:" + view.getItemFq());
			
			// 设置排序规则-按照价格升序排列
			//query.setSort("productName", ORDER.desc);
			if(view.getSequence() != null && !"".equals(view.getSequence())){
				if(view.getSortord() == 0){ // 升序
					query.setSort(view.getSequence(), ORDER.asc);
				}else { // 降序
					query.setSort(view.getSequence(), ORDER.desc);
				}
			}
			
			// 开启高亮
			query.setHighlight(true);
			query.addHighlightField("productName,pDescription");
			query.set("hl.highlightMultiTerm", true);
			query.setHighlightFragsize(30);
			query.setHighlightSimplePre("<font color=\"red\">");
			query.setHighlightSimplePost("</font>");
			
			final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping("/solr/suggest/{token}")
	public List<Solr> suggest(@PathVariable("token") final String token,final HttpServletRequest request){
		
		if(token != null && !"".equals(token)){
			try {
				final ResourceToken resourceToken = (ResourceToken) request.getAttribute("searchResourceToken"); // 访问资源库令牌
				final String word = URLDecoder.decode(token, "UTF-8");
				final SolrQuery query = new SolrQuery();
				query.set("qt", "/suggest");
				query.set("q", word);
				query.set("spellcheck.build", "true");
				List<String> list = service.suggestDocs(resourceToken.getSolrUrl(), query);
				final List<Solr> sList = new ArrayList<Solr>();
				if (list != null){
					
					for (final String string : list) {
						final Solr solr = new Solr();
						solr.setCondition(string);
						sList.add(solr);
					}
				}
				return sList;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	// 播放页获取team更多作品
	@RequestMapping(value = "/product/more", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Solr> teamMoreProduct(@RequestBody final Team team, final HttpServletRequest request) {
		final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
		final SolrQuery query = new SolrQuery();
		query.set("qf", "productName^4 tags^3 teamName^2 pDescription^1");
		query.setQuery("*:*");
		query.setFields(
				"teamId,productId,productName,productType,itemName,teamName,orignalPrice,price,picLDUrl,length,pDescription,tags");
		query.setStart(0);
		query.setRows(Integer.MAX_VALUE);
		final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
		// 移除非team的作品
		for (int i = 0; i < list.size(); i++) {
			if (Long.parseLong(list.get(i).getTeamId()) != team.getTeamId()) {
				list.remove(i);
			}
		}
		return list;
	}
	
	/**
	 * 播放界面获取更多推荐作品
	 * 根据tags来搜索
	 */
	@RequestMapping(value = "/tags/search", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Solr> getMoreProduct(@RequestBody final SolrView solrView, final HttpServletRequest request) {
		final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
		final SolrQuery query = new SolrQuery();
		// query.set("qf", "tags^10");
		query.set("q.alt", "*:*");
		query.set("defType", "edismax");
		if(StringUtils.isNotBlank(solrView.getCondition())){
			query.setQuery("tags:(" + solrView.getCondition() + ")^30");
		}else{
			return null;
		}
		query.setFields(
				"productId,productName,orignalPrice,price,picLDUrl,tags");
		query.setStart((int)solrView.getBegin());
		query.setRows((int)solrView.getLimit());
		final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
		return list;
	}
}

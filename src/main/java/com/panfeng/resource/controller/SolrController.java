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

import com.panfeng.domain.ResourceToken;
import com.panfeng.resource.model.Solr;
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
	
	@RequestMapping("/solr/query")
	public List<Solr> search(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
			String condition = view.getCondition();
			
			// TODO 对查询的词进行权重的分配
			
			if(StringUtils.isNotBlank(condition))
				condition = URLDecoder.decode(condition, "UTF-8");
			
			final SolrQuery query = new SolrQuery();
			query.set("defType", "edismax");
			query.set("qf", "productName^2.3 tags teamName^0.4");
			query.set("q.alt", "*:*");
			
			query.setQuery(condition);
			query.set("pf", "productName tags teamName");
			query.set("tie", "0.1");
			query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,tags");
			
			query.setStart(Integer.parseInt(String.valueOf(view.getBegin())));
			query.setRows(Integer.parseInt(String.valueOf(view.getLimit())));
			
			// 如果价格区间为空，则设置为全部
			if(view.getPriceFq() != null && !"".equals(view.getPriceFq())){
				query.addFilterQuery("price:" + view.getPriceFq());
			}
			
			// 如果时长区间为空，则设置为全部
			if(view.getLengthFq() != null && !"".equals(view.getLengthFq())){
				query.addFilterQuery("length:" + view.getLengthFq());
			}
			
			// 如果标签为空，则设置为全部
			if(view.getTagsFq() != null && !"".equals(view.getTagsFq().trim())){
				// 按空格及,分词
				String tags = URLDecoder.decode(view.getTagsFq(), "UTF-8");
				tags = tags.replaceAll("(\\s*)(,|，)(\\s*)", " ");
				String[] tagArr = tags.split("\\s+");
				if(tagArr != null) {
					for (final String tag : tagArr) {
						query.addFilterQuery("tags:" + tag);
					}
				}
			}
			
			// 开启高亮
			query.setHighlight(true);
			query.addHighlightField("productName");
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
			
			final SolrQuery query = new SolrQuery();
			query.set("defType", "edismax");
			query.set("qf", "productName^2.3 tags teamName^0.4");
			query.set("q.alt", "*:*");
			if("*".equals(condition)){
				// 查询全部
				condition = "";
			}
			query.setQuery(condition);
			query.set("pf", "productName tags teamName");
			query.set("tie", "0.1");
			query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,pDescription,tags");
			query.setStart(0);
			query.setRows(9999);
			// 如果价格区间不为空，则设置为全部
			if(view.getPriceFq() != null && !"".equals(view.getPriceFq())){
				query.addFilterQuery("price:" + view.getPriceFq());
			}
			
			// 如果时长区间不为空，则设置为全部
			if(view.getLengthFq() != null && !"".equals(view.getLengthFq())){
				query.addFilterQuery("length:" + view.getLengthFq());
			}
			
			// 如果视频类型不为空，则设置为全部
			// TODO 手机端改版之后，需要将 视频类型 换成 标签类型
			if(view.getItemFq() != null || !"".equals(view.getItemFq())){
				query.addFilterQuery("productType:" + view.getItemFq());
			}
			
			if(view.getSequence() != null && !"".equals(view.getSequence())){
				if(view.getSortord() == 0){ // 升序
					query.setSort(view.getSequence(), ORDER.asc);
				}else { // 降序
					query.setSort(view.getSequence(), ORDER.desc);
				}
			}
			
			// 开启高亮
			query.setHighlight(true);
			// TODO 手机端改版之后，高亮部分需要将 pDescription 换成 tags
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
	
	// 获取team更多作品
	@RequestMapping(value = "/product/more", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Solr> teamMoreProduct(@RequestBody final SolrView view, final HttpServletRequest request) {
		final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
		try {
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			final SolrQuery query = new SolrQuery();
			query.set("defType", "edismax");
			query.set("q.alt", "*:*");
			query.set("qf", "teamName");
			if(StringUtils.isNotBlank(condition)) {
				query.setQuery(condition);
			}else {
				return null;
			}
			query.setSort("creationTime", ORDER.desc);
			query.set("pf", "teamName");
			query.set("tie", "0.1");
			query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,tags,creationTime,pDescription");
			query.setStart(Integer.parseInt(String.valueOf(view.getBegin())));
			query.setRows(Integer.parseInt(String.valueOf(view.getLimit())));
			
			final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 播放界面获取更多推荐作品
	 * 根据tags来搜索
	 */
	@RequestMapping(value = "/tags/search", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Solr> getMoreProduct(@RequestBody final SolrView solrView, final HttpServletRequest request) {
		final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
		
		final SolrQuery query = new SolrQuery();
		query.set("defType", "edismax");
		query.set("q.alt", "*:*");
		query.set("qf", "productName^2.3 tags");
		if(StringUtils.isNotBlank(solrView.getCondition())){
			query.setQuery(solrView.getCondition());
		}else{
			// 没有标签，则相关视频推荐为空
			return null;
		}
		query.set("pf", "productName tags");
		query.set("tie", "0.1");
		query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,tags");
		query.setStart((int)solrView.getBegin());
		query.setRows((int)solrView.getLimit());
		
		final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
		return list;
	}
}

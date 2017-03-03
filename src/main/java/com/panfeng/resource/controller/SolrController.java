package com.panfeng.resource.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.ResourceToken;
import com.panfeng.resource.model.NewsSolr;
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
	
	private final Logger logger = LoggerFactory.getLogger(SolrController.class);
	
	// 权重数据
	final int[] weightArr = new int[]{10,8,6};
	
	@RequestMapping("/solr/query")
	public List<Solr> search(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
			String condition = view.getCondition();
			
			if(StringUtils.isNotBlank(condition)){
				condition = URLDecoder.decode(condition, "UTF-8");
				view.setCondition(condition);
			}
			
			// 组装 行业 和 类型 业务逻辑
			condition = mergeQConcition(view);
			
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
			
			// 开启高亮
			query.setHighlight(true);
			query.set("hl.highlightMultiTerm", true);
			query.addHighlightField("productName");
			// query.setParam("hl.fl", "productName,tags");
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
	
	
	@RequestMapping("/solr/query/news")
	public List<NewsSolr> searchNews(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			String condition = view.getCondition();
			
			if(StringUtils.isNotBlank(condition)){
				condition = URLDecoder.decode(condition, "UTF-8");
				view.setCondition(condition);
			}
			
			final SolrQuery query = new SolrQuery();
			query.set("defType", "edismax");
			query.set("qf", "tags");
			query.set("q.alt", "*:*");
			
			query.setQuery(condition);
			query.set("pf", "tags");
			query.set("tie", "0.1");
			query.setFields("id title tags creationTime discription picLDUrl recommend");
			
			// 设置推荐区间
			if(view.getRecomendFq() != null && !"".equals(view.getRecomendFq())){
				query.addFilterQuery("recommend:" + view.getRecomendFq());
			}
			
			query.setStart(Integer.parseInt(String.valueOf(view.getBegin())));
			query.setRows(Integer.parseInt(String.valueOf(view.getLimit())));
			
			final List<NewsSolr> list = service.queryNewDocs(GlobalConstant.SOLR_NEWS_URL, query);
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// 整合行业 和 类型，作为q值出现
	private String mergeQConcition(SolrView view) throws UnsupportedEncodingException {
		String q = view.getCondition();
		String industry = view.getIndustry();
		String genre = view.getGenre();
		StringBuffer sb = new StringBuffer();
		
		if(!StringUtils.isNotBlank(q))
			q = "*";
		else {
			String cdn = q.trim().replaceAll("(\\s*)(,|，|\\s+)(\\s*)", ",");
			logger.error("q=" + cdn);
		}
		
		sb.append(q);
		
		// 类型
		if(StringUtils.isNotBlank(genre)) {
			genre = URLDecoder.decode(genre, "UTF-8");
			// 行业不为空时，那么条件应该有 AND
			sb.append(" AND (");
			
			// 按空格及,分词
			genre = genre.replaceAll("(\\s*)(,|，)(\\s*)", " ");
			String[] tagArr = genre.split("\\s+");
			if(tagArr != null) {
				for (int i = 0;i < tagArr.length; i++) {
					if(StringUtils.isNotBlank(tagArr[i])) {
						sb.append("tags:" + "\""+ tagArr[i] +"\"");
						if(i < tagArr.length - 1)
							sb.append(" OR ");
					}
					
				}
			}
			
			sb.append(" )");
		}
		
		// 行业
		if(StringUtils.isNotBlank(industry)) {
			industry = URLDecoder.decode(industry, "UTF-8");
			// 行业不为空时，那么条件应该有 AND
			sb.append(" AND (");
			
			// 按空格及,分词
			industry = industry.replaceAll("(\\s*)(,|，)(\\s*)", " ");
			String[] tagArr = industry.split("\\s+");
			if(tagArr != null) {
				for (int i = 0;i < tagArr.length; i++) {
					if(StringUtils.isNotBlank(tagArr[i])) {
						sb.append("tags:" + "\""+ tagArr[i] +"\"");
						// 如果从相关性推荐过来的，那么应该添加权重
						if(view.isMore()) {
							if(i < 3){
								sb.append("^" + weightArr[i]);
							}
						}
							
						if(i < tagArr.length - 1)
							sb.append(" OR ");
					}
				}
			}
			
			sb.append(" )");
		}
		
		return sb.toString();
	}

	@RequestMapping("/solr/phone/query")
	public List<Solr> phoneSearch(@RequestBody final SolrView view,final HttpServletRequest request){
		
		try {
			final ResourceToken token = (ResourceToken) request.getAttribute("resourceToken"); // 访问资源库令牌
			
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			logger.error("phone search keywords : " + condition);
			
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
			if(view.getItemFq() != null && !"".equals(view.getItemFq())){
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
		String condition = solrView.getCondition();
		
		if(StringUtils.isNotBlank(condition)){
			// 如果有标签的话，那么判断condition按照标签搜索
			try {
				// 解码
				condition = URLDecoder.decode(condition, "UTF-8");
				// 分析标签优先级顺序，按顺序权重依次降低
				condition = ReweightingByTags(condition);
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			query.setQuery(condition);
		}else{
			// 没有标签，则相关视频推荐为空
			return null;
		}
		query.set("pf", "tags^2.3 productName");
		query.set("tie", "0.1");
		query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,tags");
		query.setStart((int)solrView.getBegin());
		query.setRows((int)solrView.getLimit());
		
		final List<Solr> list = service.queryDocs(token.getSolrUrl(), query);
		return list;
	}

	// 分析标签优先级顺序，按顺序权重依次降低
	private String ReweightingByTags(String condition) {
		
		// 按照'，'、','、空格 分词
		condition = condition.replaceAll("(\\s*)(,|，)(\\s*)", " ");
		String[] tagArr = condition.split("\\s+");
		StringBuffer sb = new StringBuffer();
		
		if(tagArr != null) {
			for(int i = 0;i < tagArr.length;i ++) {
				sb.append("tags:" + "\""+ tagArr[i] +"\"");
				if(i < 3){
					// 如果标签数量小于三个，那么不做处理
					sb.append("^");
					sb.append(weightArr[i]);
				}
				if(i < tagArr.length - 1)
					sb.append(" OR ");
			}
		}
		return sb.toString();
	}
}

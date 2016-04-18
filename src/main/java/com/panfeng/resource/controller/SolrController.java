package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.resource.model.Solr;
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
	
	private static final Logger logger = LoggerFactory.getLogger("error");
	
	private static String SOLR_URL = null;
	
	public SolrController() {
		if(SOLR_URL == null || "".equals(SOLR_URL)){
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties"); 
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				SOLR_URL = propertis.getProperty("solr.url");
			} catch (IOException e) {
				logger.error("Solr load Properties fail ...");
				e.printStackTrace();
			}
		}
	}

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
		final List<Solr> list = service.queryDocs(SOLR_URL, query);
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
	public List<Solr> search(@RequestBody final SolrView view){
		
		try {
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			//condition = HanlpUtil.segment(condition);

			final SolrQuery query = new SolrQuery();
			query.set("defType", "dismax");
			query.set("qf", "productName^4 tags^3 teamName^2 pDescription^1");
			
			query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
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
			
			final List<Solr> list = service.queryDocs(SOLR_URL, query);
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping("/solr/phone/query")
	public List<Solr> phoneSearch(@RequestBody final SolrView view){
		
		try {
			String condition = URLDecoder.decode(view.getCondition(), "UTF-8");
			//condition = HanlpUtil.segment(condition);
			
			final SolrQuery query = new SolrQuery();
			query.set("defType", "dismax");
			query.set("qf", "productName^2 tags^1.5 teamName^1.2 pDescription^1");
			
			query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
			query.setFields("teamId,productId,productName,orignalPrice,price,picLDUrl,pDescription,tags");
			query.setStart(0);
			query.setRows(30);
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
			
			final List<Solr> list = service.queryDocs(SOLR_URL, query);
			return list;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@RequestMapping("/solr/suggest/{token}")
	public List<Solr> suggest(@PathVariable("token") final String token){
		
		if(token != null && !"".equals(token)){
			try {
				final String word = URLDecoder.decode(token, "UTF-8");
				final SolrQuery query = new SolrQuery();
				query.set("qt", "/suggest");
				query.set("q", word);
				query.set("spellcheck.build", "true");
				List<String> list = service.suggestDocs(SOLR_URL, query);
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
}

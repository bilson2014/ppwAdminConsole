package com.panfeng.test;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.resource.model.Solr;
import com.panfeng.service.SolrService;
import com.panfeng.util.HanlpUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SolrTest {

	final static String SOLR_URL = "http://localhost:7001/solr/db";
	
	@Autowired
	private final SolrService service = null;
	
	@Test
	public void test(){
		final SolrQuery query = new SolrQuery();
		query.setQuery("productName:快乐 pDescription:快乐");
		query.setFields("productName,pDescription,itemName,teamName");
		query.setStart(0);
		query.setRows(10);
		query.setSort("productName", ORDER.desc);
		final List<Solr> list = service.queryDocs(SOLR_URL, query);
		for (final Solr product : list) {
			System.out.println(product.getProductName());
		}
	}
	
	@Test
	public void test1(){
		HttpSolrClient client = new HttpSolrClient(SOLR_URL);
		client.setConnectionTimeout(5000);
		
		SolrQuery query = new SolrQuery();
		query.setQuery("productName:快乐 pDescription:快乐");
		query.setFields("productName,pDescription,itemName");
		query.setStart(0);
		query.setRows(10);
		query.setSort("productName", ORDER.desc);
		
		try {
			QueryResponse response = client.query(query);
			SolrDocumentList list = response.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			List<Solr> lists = binder.getBeans(Solr.class, list);
			for (final Solr product : lists) {
				System.err.println("name : " + product.getProductName());
				System.err.println("description : " + product.getpDescription());
				System.err.println("typeName : " + product.getItemName());
				System.out.println("------------------------ " + "Result End" + " ------------------------");
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3(){
		HttpSolrClient client = new HttpSolrClient(SOLR_URL);
		client.setConnectionTimeout(5000);
		
		SolrQuery query = new SolrQuery();
		query.set("qt", "/suggest");
		query.set("q", "中国");
		query.set("spellcheck.build", "true");
		try {
			QueryResponse response = client.query(query);
			final SpellCheckResponse  sr = response.getSpellCheckResponse();
			if(sr != null){
				List<Suggestion> list = sr.getSuggestions();
				for (final Suggestion suggestion : list) {
					final List<String> ls = suggestion.getAlternatives();
					for (final String string : ls) {
						System.err.println(string + ", ");
					}
				}
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4(){
		HttpSolrClient client = new HttpSolrClient(SOLR_URL);
		client.setConnectionTimeout(5000);
		
		SolrQuery query = new SolrQuery();
		query.set("q", "suggestion:中果");
		query.set("qt", "/spell");
		try {
			QueryResponse qr = client.query(query);
			SpellCheckResponse scr = qr.getSpellCheckResponse();
			if(scr != null){
				String col = scr.getCollatedResult();
				System.err.println(col);
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6(){
		HttpSolrClient client = new HttpSolrClient(SOLR_URL);
		client.setConnectionTimeout(5000);
		String demo = "家居生活";
		demo = HanlpUtil.segment(demo);
		SolrQuery query = new SolrQuery();
		query.setQuery("productName:"+ demo +" pDescription:" + demo);
		query.setFields("productName,pDescription");
		query.setStart(0);
		query.setRows(80);
		//query.setSort("productName", ORDER.desc);
		
		try {
			QueryResponse response = client.query(query);
			SolrDocumentList list = response.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			List<Solr> lists = binder.getBeans(Solr.class, list);
			for (final Solr product : lists) {
				System.err.println("name : " + product.getProductName());
				System.err.println("description : " + product.getpDescription());
				System.out.println("------------------------ " + "Result End" + " ------------------------");
			}
			System.out.println(lists.size());
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * facet 分组
	 */
	@Test
	public void test7(){
		HttpSolrClient client = new HttpSolrClient(SOLR_URL);
		client.setConnectionTimeout(5000);
		String condition = "宣传片";
		
		final SolrQuery query = new SolrQuery();
		query.set("defType", "dismax");
		query.set("qf", "productName^4 tags^3 teamName^2 pDescription^1");
		
		query.setQuery("productName:" + condition + " tags:" + condition + " teamName:" + condition + " pDescription:" + condition);
		query.setFields("teamId,productId,productName,productType,itemName,teamName,orignalPrice,price,picLDUrl,length,pDescription,tags");
		query.setStart(0);
		query.setRows(20);
		
		query.set("facet", "on");
		query.set("facet.field", "itemName");
		query.set("facet.mincount", "1");
		query.set("facet.limit", "22");
		
		final List<Solr> list = service.queryDocs(SOLR_URL, query);
	}
}

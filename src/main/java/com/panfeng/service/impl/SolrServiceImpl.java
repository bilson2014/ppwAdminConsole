package com.panfeng.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import com.panfeng.domain.BaseObject;
import com.panfeng.resource.model.Solr;
import com.panfeng.service.SolrService;

@Service
public class SolrServiceImpl implements SolrService {

	@SuppressWarnings("resource")
	public long addDocs(final String solrUrl,final Collection<? extends BaseObject> docs) {
		try {
			HttpSolrClient client = new HttpSolrClient(solrUrl);
			if(docs != null && docs.size() > 0){
				client.setRequestWriter(new BinaryRequestWriter());
				UpdateResponse response = client.addBeans(docs.iterator());
				client.commit(true,true);
				client.optimize(true, true);
				return response.getResponse().size();
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	@SuppressWarnings("resource")
	public long delDocs(final String solrUrl,final List<String> ids) {
		try {
			final HttpSolrClient client = new HttpSolrClient(solrUrl);
			final UpdateResponse response = client.deleteById(ids);
			final long size = response.getResponse().size();
			client.commit(true,true);
			client.optimize(true, true);
			return size;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	@SuppressWarnings("resource")
	public List<Solr> queryDocs(final String solrUrl,
			final SolrQuery query) {
		final HttpSolrClient client = new HttpSolrClient(solrUrl);
		client.setConnectionTimeout(5000);
		
		try {
			QueryResponse response = client.query(query);
			final long numFound = response.getResults().getNumFound();
			SolrDocumentList list = response.getResults();
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			
			FacetField ff = response.getFacetField("itemName");
			List<Count> counts = null;
			if(ff != null){
				counts = ff.getValues();
				if(counts != null ){
					for (final Count count : counts) {
						System.err.println(count.getName() + " " + count.getCount());
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				final SolrDocument document = list.get(i);
				document.setField("total", numFound); // 设置总数
				if(null != map){
					final List<String> pNameList = map.get(document.getFieldValue("productId")).get("productName");
					if(pNameList != null && !pNameList.isEmpty()){
						document.setField("productName", pNameList.get(0));
					}
					
					List<String> pDescriptionList = map.get(document.getFieldValue("productId")).get("pDescription");
					if(pDescriptionList != null && !pDescriptionList.isEmpty()){
						document.setField("pDescription", pDescriptionList.get(0) + "...");
					}
				}
				
				list.set(i, document);
			}
			
			DocumentObjectBinder binder = new DocumentObjectBinder();
			List<Solr> lists = binder.getBeans(Solr.class, list);
			return lists;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除索引
	 */
	@SuppressWarnings("resource")
	public void deleteDoc(final long productId,final String solrUrl) {
		final HttpSolrClient client = new HttpSolrClient(solrUrl);
		client.setConnectionTimeout(3000);
		
		try {
			client.deleteById(String.valueOf(productId));
			client.optimize();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public List<String> suggestDocs(final String solrUrl,final SolrQuery query) {
		final HttpSolrClient client = new HttpSolrClient(solrUrl);
		client.setConnectionTimeout(2000);
		
		try {
			QueryResponse response = client.query(query);
			final SpellCheckResponse  sr = response.getSpellCheckResponse();
			if(sr != null){
				List<Suggestion> list = sr.getSuggestions();
				for (final Suggestion suggestion : list) {
					final List<String> ls = suggestion.getAlternatives();
					return ls;
				}
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

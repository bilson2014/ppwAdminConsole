package com.panfeng.logger.keywords;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paipianwang.pat.common.constant.PmsConstant;
import com.panfeng.resource.view.SolrView;

/**
 * 关键词工具类
 * @author Jack
 *
 */
public class KeywordUtils {

	private final static Logger logger = LoggerFactory.getLogger(KeywordUtils.class);
	
	// 整合行业 和 类型，作为q值出现
	public static String mergeQConcition(SolrView view) throws UnsupportedEncodingException {
		String q = view.getCondition();
		String industry = view.getIndustry();
		String genre = view.getGenre();
		StringBuffer sb = new StringBuffer();
		
		if(!StringUtils.isNotBlank(q))
			q = "*";
		else {
			String cdn = q.trim().replaceAll("(\\s*)(,|，|\\s+)(\\s*)", ",");
			if(!"*".equals(q)) {
				logger.error("q=" + cdn);
			}
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
								sb.append("^" + PmsConstant.WEIGHT_ARR[i]);
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
}

package com.panfeng.util;

import java.util.List;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;

public class HanlpUtil {

	public static String segment(final String text){
		if(text != null && !"".equals(text)){
			List<Term> list = BasicTokenizer.segment(text);
			CoreStopWordDictionary.apply(list);
			final StringBuffer sb = new StringBuffer();
			if(list != null && !list.isEmpty()){
				for (final Term term : list) {
					sb.append(term);
					//sb.append(" ");
				}
				return sb.toString();
			}
			return text;
		}
		return null;
	}
}

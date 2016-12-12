package com.panfeng.test;

import java.util.List;

import org.junit.Test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;

public class HanlpTest {

	@Test
	public void test1(){
		final String str = "重载不是重任";
		List<Pinyin> pinyinList = HanLP.convertToPinyinList(str);
		
		System.out.println("数字音调：");
		for (final Pinyin pinyin : pinyinList) {
			System.err.println(pinyin + ", ");
		}
		
		System.out.println("符号音调");
		for (final Pinyin pinyin : pinyinList) {
			System.err.printf("%s",pinyin.getPinyinWithToneMark());
		}
	}
	
	@Test
	public void test2(){
		final String document = "算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。\n" +
			    "算法可以宽泛的分为三类，\n" +
			    "一，有限的确定性算法，这类算法在有限的一段时间内终止。他们可能要花很长时间来执行指定的任务，但仍将在一定的时间内终止。这类算法得出的结果常取决于输入值。\n" +
			    "二，有限的非确定算法，这类算法在有限的时间内终止。然而，对于一个（或一些）给定的数值，算法的结果并不是唯一的或确定的。\n" +
			    "三，无限的算法，是那些由于没有定义终止定义条件，或定义的条件无法由输入的数据满足而不终止运行的算法。通常，无限算法的产生是由于未能确定的定义终止条件。";
		
		List<String> sentenceList = HanLP.extractSummary(document, 3);
		for (final String string : sentenceList) {
			System.out.println(string);
		}
	}
	
	@Test
	public void test3(){
		Suggester suggester = new Suggester();
		String[] titleArray =
		(
		        "中国建设银行\n" +
		        "中国人寿保险\n" +
		        "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
		        "微信据看见立刻jam，生命浪费了推广\n" +
		        "微信2.0版校园推广影片微信宣传片”"
		).split("\\n");
		for (String title : titleArray)
		{
		    suggester.addSentence(title);
		}

		System.out.println(suggester.suggest("威信腿光", 1));       // 语义
		System.out.println(suggester.suggest("危险", 1));   // 字符
		System.out.println(suggester.suggest("马云", 1));      // 拼音
	}
	
	@Test
	public void test4() {
		String text = "小区居民有的反对喂养流浪猫，而有的居民却赞成喂养这些小宝贝";
		List<Term> list = BasicTokenizer.segment(text);
		//System.err.println(list);
		CoreStopWordDictionary.apply(list);
		//System.err.println(list);
		for (final Term term : list) {
			System.err.println(term);
		}
		//final String arr = list.toString();
		//System.err.println(arr);
	}
	
}

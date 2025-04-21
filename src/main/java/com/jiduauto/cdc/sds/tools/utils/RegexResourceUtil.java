package com.jiduauto.cdc.sds.tools.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则资源文件工具类
 *    暂时先由外部来进行资源加载，后期增加从模块读入正则的功能
 *
 * @author terry
 */
public class RegexResourceUtil {

	//key为slot值，value是domain,intent,slotname,slotNormalizedValue的组合
	private  ConcurrentHashMap<String,String> slotListInMemory = new ConcurrentHashMap<String,String>();

	//key为slot Regex值，value是domain,intent,slotname,slotNormalizedValue的组合
	private  ConcurrentHashMap<String,List<String>> slotRegexListInMemory = new ConcurrentHashMap<String,List<String>>();

	private  ConcurrentHashMap<String,String> slotRegexResultListInMemory = new ConcurrentHashMap<String,String>();

	//将数据库中的数据加载到cache中，slot分两种，一种是直接的表达，一种是正则，这里先简单粗暴的以中括号来判断是一个正则；
	public  void addAPatternToMemory(List<String> slotSynonymListIncludePanttern,String domain,String intent,String slotName,String slotNormalizedValue){
		//循环同义词列表
		for(String slotSynonymItem : slotSynonymListIncludePanttern ){
			addAPatternToMemorySingle(slotSynonymItem, domain, intent,slotName,slotNormalizedValue);
		}
	}

	public  void addAPatternToMemorySingle(String slotSynonym,String domain,String intent,String slotName,String slotNormalizedValue) {
		try{
			String inMemoryKeyPrefix = String.format("%s,%s,%s",domain,intent,slotName);
			//通过同义词特殊标识来标明
			if(slotSynonym.startsWith("RegExHex:")){
				//只添加，没有校验
				//以RegEx64:开头的同义词，后面是以base64编码的正则。
				String regEx = new String(hexStringToBytes(slotSynonym.substring(9)));
				//缓存加载一次，同时会验证正则是否正确
				RegexCache.get(regEx);
				if(slotRegexListInMemory.containsKey(inMemoryKeyPrefix) == false){
					slotRegexListInMemory.put(inMemoryKeyPrefix,new ArrayList<String>());
				}
				//放入正则
				slotRegexListInMemory.get(inMemoryKeyPrefix).add(regEx);
				slotRegexResultListInMemory.put(inMemoryKeyPrefix +"," + regEx, slotNormalizedValue);
			}
			//以原始值对待
			else{
				slotListInMemory.put(inMemoryKeyPrefix +"," + slotSynonym, slotNormalizedValue);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	//以domain,intent,slotName,slotValue为一个维度进行检索，返回值为归一化后数据
	public  String normalizeAnValue(String domain,String intent,String slotName,String slotValue){
		//先从默认值中取；
		String defaultKey = String.format("%s,%s,%s,%s", domain,intent,slotName,slotValue);
		if(slotListInMemory.containsKey(defaultKey)){
			return slotListInMemory.get(defaultKey);
		}
		else{
		    //用正则匹配
			String regexKey = String.format("%s,%s,%s", domain,intent,slotName);
			if(slotRegexListInMemory.containsKey(regexKey)){
				List<String> slotRegexList = slotRegexListInMemory.get(regexKey);
				for(String regexItem : slotRegexList){
					Pattern pattern = RegexCache.get(regexItem);
					Matcher match = pattern.matcher(slotValue);
					if (match.find()) {
						return slotRegexResultListInMemory.get(regexKey+","+regexItem);
					}
				}
			}
		}
        //都没匹配，返回原始值
		return slotValue;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * @param bytes 字节数组
	 * @return 十六进制字符串
	 */
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	/**
	 * 将十六进制字符串转换为字节数组
	 * @param hexString 十六进制字符串
	 * @return 字节数组
	 */
	private static byte[] hexStringToBytes(String hexString) {
		int len = hexString.length();
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i< len; i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16)<< 4)
					+ Character.digit(hexString.charAt(i+1), 16));
		}
		return bytes;
	}

    
    public static void main(String[] args) throws Exception {
		List<String> slotList = new ArrayList<>();
		slotList.addAll(Arrays.asList("左,最左侧,左边,左侧".split(",")));
		
		slotList.add("RegExHex:"+bytesToHexString("打?开{0,2}(下|一下|了)?".getBytes(StandardCharsets.UTF_8)));
		String domain ="carControl";
		String intent ="TURN_SIGNAL";
		String slotName ="direction";
		String slotNormalizedValue ="LEFT";
		RegexResourceUtil util = new RegexResourceUtil();
		util.addAPatternToMemory(slotList,domain,intent,slotName,slotNormalizedValue);
		//data prepare
		slotList = new ArrayList<>();
		slotList.addAll(Arrays.asList("右,右侧,右边".split(",")));


		slotList.add("RegExHex:"+bytesToHexString("[最]右[\\u4e00-\\u9fa5]{1,10}".getBytes(StandardCharsets.UTF_8)));
		domain ="carControl";
		intent ="TURN_SIGNAL";
		slotName ="direction";
		slotNormalizedValue ="RIGHT";
		util.addAPatternToMemory(slotList,domain,intent,slotName,slotNormalizedValue);
//
//		for(String slotValue : slotList){
//			System.out.println(util.normalizeAnValue(domain,intent,slotName,slotValue));
//		}
		System.out.println(util.normalizeAnValue(domain,intent,slotName,"一下"));
//		System.out.println(RegexResourceUtil.normalizeAnValue(domain,intent,slotName,"最左"));
//		System.out.println(RegexResourceUtil.normalizeAnValue(domain,intent,slotName,"左侧"));
//		System.out.println(RegexResourceUtil.normalizeAnValue(domain,intent,slotName,"左"));
	}
}

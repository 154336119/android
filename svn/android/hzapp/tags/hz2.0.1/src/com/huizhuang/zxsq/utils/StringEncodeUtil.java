package com.huizhuang.zxsq.utils;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @ClassName: StringEncodeUtil
 * @Description: 字符类型转换工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3  上午09:41:55
 */
public class StringEncodeUtil {

	private static final String NULL_STRING = "null";

	/**
	 * HEX编码 将形如0x12 0x2A 0x01 转换为122A01
	 * 
	 * @param data
	 *            待转换数据
	 * @return 转换后数据
	 */
	public static String hexEncode(byte[] data) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			String tmp = Integer.toHexString(data[i] & 0xff);
			if (tmp.length() < 2) {
				buffer.append('0');
			}
			buffer.append(tmp);
		}
		String retStr = buffer.toString().toUpperCase(Locale.getDefault());
		return retStr;
	}

	/**
	 * HEX解码 将形如122A01 转换为0x12 0x2A 0x01
	 * 
	 * @param data
	 *            待转换数据
	 * @return 转换后数据
	 */
	public static byte[] hexDecode(String data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < data.length(); i += 2) {
			String onebyte = data.substring(i, i + 2);
			int b = Integer.parseInt(onebyte, 16) & 0xff;
			out.write(b);
		}
		return out.toByteArray();
	}

	/**
	 * 获取异或编码
	 * 
	 * @param data
	 *            数据
	 * @param offset
	 *            开始偏移量
	 * @param length
	 *            长度
	 * @return 异或值
	 */
	public static byte getXorCheck(byte[] data, int offset, int length) {
		byte check = 0;
		for (int i = 0; i < length; i++) {
			check ^= data[offset + i];
		}
		return check;
	}

	/**
	 * 将两个byte转换为整数
	 * 
	 * @param data
	 *            待转换byte数组
	 * @param offset
	 *            数据偏移量
	 * @return 转换后整数值
	 */
	public static int convertTwoBytesToInt(byte[] data, int offset) {
		if (data.length - offset < 2) {
			return -1;
		}
		int value = data[offset];
		if (value < 0) {
			value += 256;
		}
		value = value << 8;
		value += data[offset + 1];
		if (data[offset + 1] < 0) {
			value += 256;
		}
		return value;
	}

	/**
	 * 将字符串中的特殊字符以空格代替
	 * 
	 * @param s
	 *            待转化字符串
	 * @return 转换后字符串
	 */
	public static String removeSpecialCharacters(String s) {
		return s.replaceAll("[\\n\\r\\t]", " ");
	}

	/**
	 * 判断字符串是否为有效字符串（不为空字符串）
	 * 
	 * @param data
	 *            待判断字符串
	 * @return true:有效字符串 false:无效字符串
	 */
	public static boolean isValid(String data) {
		return !(null == data || "".equals(data) || NULL_STRING.equals(data));
	}
	
	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	
	/**
	 * 字符串转布尔值
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
	
	public static double get2Double(double a){  
	    DecimalFormat df=new DecimalFormat("0.00");  
	    return Double.valueOf(df.format(a).toString());  
	}  
}

package com.huizhuang.zxsq.utils;

import java.math.BigDecimal;

/*
 * 计算两个经纬度坐标点之间的距离
 */
public class DistanceUtil {
	private static double EARTH_RADIUS = 6378.137;
	private static double rad(double d){
		return d*Math.PI/180.0;
	}
	public static double getMapDiatance(double startLat,double startLong,
			double endLat,double endLong){
		double radStartLat = rad(startLat);
		double radStartLong = rad(startLong);
		double radEndLat = rad(endLat);
		double radEndLong = rad(endLong);
		double a = radStartLat-radEndLat;
		double b = radStartLong - radEndLong;
		double s = 2* Math.asin(Math.sqrt(Math.pow(Math.sin(a/2), 2)
				+Math.cos(radStartLat)*Math.cos(radEndLat)
				*Math.pow(Math.sin(b/2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s*10000)/10000;
		return s;
	}
	/**
	 * 
	 * @param distance 单位： 米
	 * @return <1km 直接显示m  否则保留一位小数，并显示km
	 */
	public static String getDiatance(double distance){
		if(distance < 1000 ){
			return String.valueOf((int)distance)+"m";
		}else if(distance >= 1000 ){
			distance = distance/1000;
			BigDecimal bd = new BigDecimal(distance);
			bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
			distance = bd.doubleValue();
			return String.valueOf(distance)+"km";
		}
		return null;
	}
}

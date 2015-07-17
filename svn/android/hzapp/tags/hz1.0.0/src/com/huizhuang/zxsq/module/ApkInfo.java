package com.huizhuang.zxsq.module;

import java.io.Serializable;

import com.huizhuang.zxsq.module.base.BaseBean;

/**
 * @ClassName: ApkInfo
 * @Description: apk更新信息
 * @author lim
 * @date 2013-7-30 上午02:28:27
 * 
 */
public class ApkInfo implements Serializable, BaseBean {

	private static final long serialVersionUID = 1L;

	private String downloadUrl;// 下载地址
	private String apkName; // apk名字
	private String apkVersion; // apk版本
	private String apkSize; // apk文件大小
	private int apkCode; // apk版本号(更新必备)
	private String apkLog; // apk更日志־
	private boolean isForceUpdate;// 是否强制更新
	private String forceUpdateVersionCode;

	public ApkInfo() {
	}

	public ApkInfo(String downloadUrl, String apkVersion, String apkSize,
			int apkCode, String apkName, String apkLog, boolean isForceUpdate) {
		super();
		this.downloadUrl = downloadUrl;
		this.apkVersion = apkVersion;
		this.apkSize = apkSize;
		this.apkCode = apkCode;
		this.apkName = apkName;
		this.apkLog = apkLog;
		this.isForceUpdate = isForceUpdate;
	}

	/**
	 * @return the downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * @param downloadUrl
	 *            the downloadUrl to set
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * @return the apkVersion
	 */
	public String getApkVersion() {
		return apkVersion;
	}

	/**
	 * @param apkVersion
	 *            the apkVersion to set
	 */
	public void setApkVersion(String apkVersion) {
		this.apkVersion = apkVersion;
	}

	/**
	 * @return the apkSize
	 */
	public String getApkSize() {
		return apkSize;
	}

	/**
	 * @param apkSize
	 *            the apkSize to set
	 */
	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}

	/**
	 * @return the apkLog
	 */
	public String getApkLog() {
		return apkLog;
	}

	/**
	 * @param apkLog
	 *            the apkLog to set
	 */
	public void setApkLog(String apkLog) {
		this.apkLog = apkLog;
	}

	/**
	 * @return the apkName
	 */
	public String getApkName() {
		return apkName;
	}

	/**
	 * @param apkName
	 *            the apkName to set
	 */
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	/**
	 * @return the apkCode
	 */
	public int getApkCode() {
		return apkCode;
	}

	/**
	 * @param apkCode
	 *            the apkCode to set
	 */
	public void setApkCode(int apkCode) {
		this.apkCode = apkCode;
	}

	public boolean isForceUpdate() {
		return isForceUpdate;
	}

	public void setForceUpdate(boolean isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public String getForceUpdateVersionCode() {
		return forceUpdateVersionCode;
	}

	public void setForceUpdateVersionCode(String forceUpdateVersionCode) {
		this.forceUpdateVersionCode = forceUpdateVersionCode;
	}

	@Override
	public String toString() {
		return "ApkInfo [downloadUrl=" + downloadUrl + ", apkName=" + apkName
				+ ", apkVersion=" + apkVersion + ", apkSize=" + apkSize
				+ ", apkCode=" + apkCode + ", apkLog=" + apkLog
				+ ", isForceUpdate=" + isForceUpdate
				+ ", forceUpdateVersionCode=" + forceUpdateVersionCode + "]";
	}
	
	

}

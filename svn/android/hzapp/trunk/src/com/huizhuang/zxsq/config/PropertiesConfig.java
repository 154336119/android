package com.huizhuang.zxsq.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import android.content.Context;
import android.text.TextUtils;

/**
 * @ClassName: PropertiesConfig
 * @Package com.huizhuang.zxsq.config
 * @Description: poperties配置管理
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月25日  上午11:04:21
 */
public class PropertiesConfig {

	/** assets中配置信息文件 */
	private static final String ASSETS_PATH = "/assets/config.properties";
	/** 软件Files文件夹中配置信息文件 */
	private static final String FILES_PATH = "config.properties";
	private static final String LOAD_FLAG = "assetsload";
	private static PropertiesConfig instance;
	private Context mContext;
	private Properties mProperties;
	
	private PropertiesConfig(Context context) {
		this.mContext = context;
	}

	public static PropertiesConfig getInstance(Context context) {
		if (instance == null) {
			instance = new PropertiesConfig(context);
		}
		return instance;
	}

	public void initConfig() {
		Properties props = new Properties();
		InputStream in = PropertiesConfig.class.getResourceAsStream(ASSETS_PATH);
		try {
			if (in != null) {
				props.load(in);
				saveProperties(props);
				Enumeration<?> e = props.propertyNames();
				if (e.hasMoreElements()) {
					while (e.hasMoreElements()) {
						String s = (String) e.nextElement();
						getProperties().setProperty(s, props.getProperty(s));
					}
				}
			}
			setBoolean(LOAD_FLAG, true);
		} catch (Exception e) {
		}
	}

	public Boolean isInitConfig() {
		return getBoolean(LOAD_FLAG, false);
	}
	
	/**
	 * 返回配置关于配置实体
	 * 
	 * @return 返回配置实体
	 */
	private Properties getProperties() {
		if (mProperties == null) {
			mProperties = new Properties();
			try {
				InputStream in = mContext.openFileInput(FILES_PATH);
				mProperties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mProperties;
	}

	private void saveProperties(Properties p) {
		OutputStream out;
		try {
			out = mContext.openFileOutput(FILES_PATH, Context.MODE_PRIVATE);
			p.store(out, null);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setConfig(String key, String value) {
		if (!TextUtils.isEmpty(value)) {
			Properties props = getProperties();
			props.setProperty(key, value);
			saveProperties(props);
		}
	}
	
	public void setString(String key, String value) {
		setConfig(key, value);
	}

	public void setInt(String key, int value) {
		setString(key, String.valueOf(value));
	}

	public void setBoolean(String key, Boolean value) {
		setString(key, String.valueOf(value));
	}

	public void setByte(String key, byte[] value) {
		setString(key, String.valueOf(value));
	}

	public void setShort(String key, short value) {
		setString(key, String.valueOf(value));
	}

	public void setLong(String key, long value) {
		setString(key, String.valueOf(value));
	}

	public void setFloat(String key, float value) {
		setString(key, String.valueOf(value));
	}

	public void setDouble(String key, double value) {
		setString(key, String.valueOf(value));
	}

	public String getConfig(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}

	public String getString(String key, String defaultValue) {
		return getConfig(key, defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		try {
			return Integer.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;

	}

	public boolean getBoolean(String key, Boolean defaultValue) {
		try {
			return Boolean.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public byte[] getByte(String key, byte[] defaultValue) {
		try {
			return getString(key, "").getBytes();
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public short getShort(String key, Short defaultValue) {
		try {
			return Short.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public long getLong(String key, Long defaultValue) {
		try {
			return Long.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public float getFloat(String key, Float defaultValue) {
		try {
			return Float.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public double getDouble(String key, Double defaultValue) {
		try {
			return Double.valueOf(getString(key, ""));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	public String getString(int resID, String defaultValue) {
		return getString(this.mContext.getString(resID), defaultValue);
	}

	public int getInt(int resID, int defaultValue) {
		return getInt(this.mContext.getString(resID), defaultValue);
	}

	public boolean getBoolean(int resID, Boolean defaultValue) {
		return getBoolean(this.mContext.getString(resID), defaultValue);
	}

	public byte[] getByte(int resID, byte[] defaultValue) {
		return getByte(this.mContext.getString(resID), defaultValue);
	}

	public short getShort(int resID, Short defaultValue) {
		return getShort(this.mContext.getString(resID), defaultValue);
	}

	public long getLong(int resID, Long defaultValue) {

		return getLong(this.mContext.getString(resID), defaultValue);
	}

	public float getFloat(int resID, Float defaultValue) {
		return getFloat(this.mContext.getString(resID), defaultValue);
	}

	public double getDouble(int resID, Double defaultValue) {
		return getDouble(this.mContext.getString(resID), defaultValue);
	}

	public void setString(int resID, String value) {
		setString(this.mContext.getString(resID), value);
	}

	public void setInt(int resID, int value) {
		setInt(this.mContext.getString(resID), value);
	}

	public void setBoolean(int resID, Boolean value) {
		setBoolean(this.mContext.getString(resID), value);
	}

	public void setByte(int resID, byte[] value) {
		setByte(this.mContext.getString(resID), value);
	}

	public void setShort(int resID, short value) {
		setShort(this.mContext.getString(resID), value);
	}

	public void setLong(int resID, long value) {
		setLong(this.mContext.getString(resID), value);
	}

	public void setFloat(int resID, float value) {
		setFloat(this.mContext.getString(resID), value);
	}

	public void setDouble(int resID, double value) {
		setDouble(this.mContext.getString(resID), value);
	}

	public void remove(String key) {
		Properties props = getProperties();
		props.remove(key);
		saveProperties(props);
	}

	public void remove(String... keys) {
		Properties props = getProperties();
		for (String key : keys) {
			props.remove(key);
		}
		saveProperties(props);
	}

	public void clear() {
		Properties props = getProperties();
		props.clear();
		saveProperties(props);
	}
}

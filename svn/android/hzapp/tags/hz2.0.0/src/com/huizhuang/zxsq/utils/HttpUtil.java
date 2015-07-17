package com.huizhuang.zxsq.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;


/**
 * @ClassName: HttpUtil
 * @Description: http检查工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月3日  下午4:01:20
 */
public class HttpUtil {
	
	private static final int STRING_BUFFER_LENGTH = 100;
	
	private static String defaultEncodingCharset = HTTP.DEFAULT_CONTENT_CHARSET;
    private static TrustManager[] trustAllCerts;
    
    public static String[] supportCharset = new String[]{
	    	"UTF-8",
            "GB2312",
            "GBK",
            "GB18030",
            "US-ASCII",
            "ASCII",
            "ISO-2022-KR",
            "ISO-8859-1",
            "ISO-8859-2",
            "ISO-2022-JP",
            "ISO-2022-JP-2"
    };
    
	private HttpUtil() {
		
	}

	/**
	 * 是否支持断点续传
	 * @param response
	 * @return
	 */
	public static boolean isSupportRange(final HttpResponse response) {
		if (response == null)
			return false;
		Header header = response.getFirstHeader("Accept-Ranges");
		if (header != null) {
			return "bytes".equals(header.getValue());
		}
		header = response.getFirstHeader("Content-Range");
		if (header != null) {
			String value = header.getValue();
			return value != null && value.startsWith("bytes");
		}
		return false;
	}

	/**
	 * 获取文件名称
	 * @param response
	 * @return
	 */
	public static String getFileNameFromHttpResponse(final HttpResponse response) {
		if (response == null)
			return null;
		String result = null;
		Header header = response.getFirstHeader("Content-Disposition");
		if (header != null) {
			for (HeaderElement element : header.getElements()) {
				NameValuePair fileNamePair = element.getParameterByName("filename");
				if (fileNamePair != null) {
					result = fileNamePair.getValue();
					// try to get correct encoding str
					result = toCharset(result, HTTP.UTF_8, result.length());
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 获取编码方式
	 * @param response
	 * @return
	 */
	public static String getCharsetFromHttpResponse(final HttpResponse response) {
		if (response == null)
			return null;
		String result = null;
		Header header = response.getEntity().getContentType();
		if (header != null) {
			for (HeaderElement element : header.getElements()) {
				NameValuePair charsetPair = element.getParameterByName("charset");
				if (charsetPair != null) {
					result = charsetPair.getValue();
					break;
				}
			}
		}

		boolean isSupportedCharset = false;
		if (!TextUtils.isEmpty(result)) {
			try {
				isSupportedCharset = Charset.isSupported(result);
			} catch (Throwable e) {
			}
		}

		return isSupportedCharset ? result : null;
	}

	/**
	 * 字符串大小
	 * @param str
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static long sizeOfString(final String str, String charset) throws UnsupportedEncodingException {
		if (TextUtils.isEmpty(str)) {
			return 0;
		}
		int len = str.length();
		if (len < STRING_BUFFER_LENGTH) {
			return str.getBytes(charset).length;
		}
		long size = 0;
		for (int i = 0; i < len; i += STRING_BUFFER_LENGTH) {
			int end = i + STRING_BUFFER_LENGTH;
			end = end < len ? end : len;
			String temp = str.substring(i, end);
			size += temp.getBytes(charset).length;
		}
		return size;
	}

	/**
	 * 受信的连接
	 */
	public static void trustAllSSLForHttpsURLConnection() {
		// Create a trust manager that does not validate certificate chains
		if (trustAllCerts == null) {
			trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} };
		}
		// Install the all-trusting trust manager
		final SSLContext sslContext;
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	}

	/**
	 * 字符编码
	 * @param str
	 * @param charset
	 * @param judgeCharsetLength
	 * @return
	 */
	public static String toCharset(final String str, final String charset, int judgeCharsetLength) {
		try {
			String oldCharset = getEncoding(str, judgeCharsetLength);
			return new String(str.getBytes(oldCharset), charset);
		} catch (Throwable e) {
			e.printStackTrace();
			return str;
		}
	}

	/**
	 * 获取字符集
	 * @param str
	 * @param judgeCharsetLength
	 * @return
	 */
	public static String getEncoding(final String str, int judgeCharsetLength) {
		String encode = defaultEncodingCharset;
		for (String charset : supportCharset) {
			if (isCharset(str, charset, judgeCharsetLength)) {
				encode = charset;
				break;
			}
		}
		return encode;
	}

	public static boolean isCharset(final String str, final String charset, int judgeCharsetLength) {
		try {
			String temp = str.length() > judgeCharsetLength ? str.substring(0, judgeCharsetLength) : str;
			return temp.equals(new String(temp.getBytes(charset), charset));
		} catch (Throwable e) {
			return false;
		}
	}
}

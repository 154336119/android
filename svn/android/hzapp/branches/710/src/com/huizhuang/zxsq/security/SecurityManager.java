package com.huizhuang.zxsq.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.huizhuang.zxsq.utils.StringEncodeUtil;

/**
 * 安全管理类
 * 
 * @author huxp
 * 
 */
public class SecurityManager {

	private static final int ONE_READ_LENGTH = 8192;
	private static final String ENCODING_TYPE = "UTF-8";
	private static final String ROOT_KEY = "risetek colorful";
	private static final String RSA_ALGORITHM = "RSA";
	private static final String DES = "DES";
	private static final String MODE = "DES/ECB/PKCS5Padding";
	private static final String DEFAULT_KEY = "ZLXTYLSD";

	private static SecurityManager mSelf;
	private byte[] mMainKey;

	private SecurityManager() {
		mMainKey = null;
	}

	/**
	 * 获取安全管理类
	 * 
	 * @return
	 */
	public static SecurityManager getInstance() {
		if (null == mSelf) {
			mSelf = new SecurityManager();
		}
		return mSelf;
	}

	private String getFormatData(int uid) {
		byte[] temp = new byte[4];
		temp[0] = (byte) ((uid >> 24) & 0xFF);
		temp[1] = (byte) ((uid >> 16) & 0xFF);
		temp[2] = (byte) ((uid >> 8) & 0xFF);
		temp[3] = (byte) (uid & 0xFF);
		return StringEncodeUtil.hexEncode(temp);
	}

	/**
	 * 密钥初始化
	 * 
	 * @param uid
	 *            用户ID
	 */
	public void init(int uid) {
		try {
			byte[] data = getFormatData(uid).getBytes();
			byte[] rootKey = ROOT_KEY.getBytes();
			for (int i = 0; i < rootKey.length; i++) {
				rootKey[i] += rootKey[i] + i;
			}
			byte[] mKey1 = Security.triDesECBDecrypt(data, rootKey);
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) (data[i] ^ mKey1[i]);
			}
			byte[] mKey2 = Security.triDesECBEncrypt(data, rootKey);
			mMainKey = new byte[16];
			System.arraycopy(mKey1, 0, mMainKey, 0, mKey1.length);
			System.arraycopy(mKey2, 0, mMainKey, mKey1.length, mKey2.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对数据3DES加密
	 * 
	 * @param plainData
	 *            明文
	 * @return 密文
	 * @throws Exception
	 *             加密失败
	 */
	public String encryData(String plainData) throws Exception {
		if (null == mMainKey) {
			throw new RuntimeException("未初始化安全管理！");
		}
		try {
			String p = StringEncodeUtil.hexEncode(plainData.getBytes(ENCODING_TYPE));
			return Security.encryptSection(mMainKey, p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("加密数据失败！");
		}
	}

	/**
	 * 对数据3DES解密
	 * 
	 * @param data
	 *            密文
	 * @return 明文
	 * @throws Exception
	 *             解密失败，密文长度对正确等
	 */
	public String decryData(String data) throws Exception {
		if (null == mMainKey) {
			throw new RuntimeException("未初始化安全管理！");
		}
		if (data.length() % 8 != 0) {
			throw new Exception("解密数据长度不正确！");
		}
		try {
			String s = Security.decryptSection(mMainKey, data);
			return new String(StringEncodeUtil.hexDecode(s), ENCODING_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("解密数据失败!");
		}
	}

	/**
	 * 计算MD5摘要
	 * 
	 * @param data
	 *            待计算摘要的数据
	 * @return 摘要
	 * @throws Exception
	 *             计算摘要失败
	 */
	public static byte[] md5(byte[] data) throws Exception {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(data);
			return algorithm.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("没有MD5算法");
		}
	}

	/**
	 * md5加密
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryMD5(String data) throws NoSuchAlgorithmException {
		 if (data == null || data.trim().length() < 1) {
	            return null;
	        }
	        try {
	            return getMD5(data.getBytes("UTF-8"));
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException(e.getMessage(), e);
	        }
	}

	private static String getMD5(byte[] source) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md5.digest(source)) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

	/**
	 * 计算多文件MD5摘要
	 * 
	 * @param path
	 *            文件路径
	 * @param files
	 *            待计算文件名数组
	 * @return MD5摘要
	 * @throws IOException
	 *             读取文件失败
	 * @throws Exception
	 *             计算摘要失败
	 */
	public static byte[] md5(String path, String[] files) throws IOException,
			Exception {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			for (int i = 0; i < files.length; i++) {
				String fileName;
				if (path.endsWith("/")) {
					fileName = path + files[i];
				} else {
					fileName = path + "/" + files[i];
				}
				FileInputStream input = null;
				File file = new File(fileName);
				if (file.exists() && file.isFile()) {
					try {
						input = new FileInputStream(fileName);
						int available = input.available();
						while (available > 0) {
							byte[] data;
							if (available > ONE_READ_LENGTH) {
								data = new byte[ONE_READ_LENGTH];
							} else {
								data = new byte[available];
							}
							available -= data.length;
							input.read(data);
							algorithm.update(data);
						}

					} catch (Exception e) {
						e.printStackTrace();
						throw new IOException(e.getMessage());
					} finally {
						if (null != input) {
							input.close();
						}
					}
				}
			}
			return algorithm.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("没有MD5算法");
		}
	}

	/**
	 * des加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @param transformation
	 *            变换模式(包括变换模式，填充方式等比如"DES/ECB/NoPadding")
	 * @return 密文
	 * @throws Exception
	 *             加密失败
	 */
	public static byte[] desEncrypt(byte[] data, byte[] key,
			String transformation) throws Exception {
		try {
			return Security.desEncrypt(data, key, transformation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("DES加密失败！");
		}
	}

	/**
	 * des解密
	 * 
	 * @param data
	 *            带解密密文
	 * @param key
	 *            密钥
	 * @param transformation
	 *            变换模式(包括变换模式，填充方式等比如"DES/ECB/NoPadding"
	 * @return 明文
	 * @throws Exception
	 *             解密失败
	 */
	public static byte[] desDecrypt(byte[] data, byte[] key,
			String transformation) throws Exception {
		try {
			return Security.desDecrypt(data, key, transformation);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("DES解密失败！");
		}
	}

	/**
	 * 获取公钥
	 * 
	 * @param modulus
	 *            公钥模数
	 * @param publicExponent
	 *            公钥指数
	 * @return 公钥
	 * @throws NoSuchAlgorithmException
	 *             系统不支持RSA算法
	 * @throws InvalidKeySpecException
	 *             传入模数或指数错误
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		BigInteger m = new BigInteger(modulus, 10);
		BigInteger e = new BigInteger(publicExponent, 10);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;

	}

	/**
	 * 获取私钥
	 * 
	 * @param modulus
	 *            私钥模数
	 * @param privateExponent
	 *            私钥指数
	 * @return 私钥
	 * @throws NoSuchAlgorithmException
	 *             系统不支持RSA算法
	 * @throws InvalidKeySpecException
	 *             传入模数或指数错误
	 */
	public static PrivateKey getPrivateKey(String modulus,
			String privateExponent) throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		BigInteger m = new BigInteger(modulus, 10);
		BigInteger e = new BigInteger(privateExponent, 10);
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;

	}

	/**
	 * RSA加密
	 * 
	 * @param plainText
	 *            明文
	 * @param key
	 *            密钥
	 * @param transformation
	 *            变换模式(包括变换模式，填充方式等比如"RSA/ECB/PKCS1Padding"
	 * @return 密文
	 * @throws Exception
	 *             加密失败
	 */
	public static byte[] rsaEncrypt(String plainText, Key key,
			String transformation) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(plainText
					.getBytes(ENCODING_TYPE));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("RSA加密失败！");
		}
	}

	/**
	 * RSA解密
	 * 
	 * @param cipherText
	 *            待解密密文
	 * @param key
	 *            密钥
	 * @param transformation
	 *            变换模式(包括变换模式，填充方式等比如"RSA/ECB/PKCS1Padding"
	 * @return 明文
	 * @throws Exception
	 *             解密失败
	 */
	public static String rsaDecrypt(byte[] cipherText, Key key,
			String transformation) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] deBytes = cipher.doFinal(cipherText);
			return new String(deBytes, ENCODING_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("RSA解密失败！");
		}
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            加密前的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encodeDes(String key, String input) throws Exception {
		byte[] data = encrypt(key, input);
		// return Hex.encodeHexString(data);
		return byte2HexString(data);
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            加密前的字符串
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String key, String input) throws Exception {
		return doFinal(key, Cipher.ENCRYPT_MODE, input.getBytes());
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            解密前的字符串
	 * @return encode方法返回的字符串
	 * @throws Exception
	 */
	public static String decodeDes(String key, String input) throws Exception {
		byte[] data = String2Byte(input); // Hex.decodeHex(input.toCharArray());
		return new String(decrypt(key, data));
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param input
	 *            encrypt方法返回的字节数组
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String key, byte[] input) throws Exception {
		return doFinal(key, Cipher.DECRYPT_MODE, input);
	}

	/**
	 * 执行加密解密操作
	 * 
	 * @param key
	 *            密钥
	 * @param opmode
	 *            操作类型：Cipher.ENCRYPT_MODE-加密，Cipher.DECRYPT_MODE-解密
	 * @param input
	 *            加密解密前的字节数组
	 * @return
	 * @throws Exception
	 */
	private static byte[] doFinal(String key, int opmode, byte[] input)
			throws Exception {
		key = key != null ? key : DEFAULT_KEY;
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(MODE);
		// 用密匙初始化Cipher对象
		// IvParameterSpec param = new IvParameterSpec(IV);
		// cipher.init(Cipher.DECRYPT_MODE, securekey, param, sr);
		cipher.init(opmode, securekey, sr);
		// 执行加密解密操作
		return cipher.doFinal(input);
	}

	/**
	 * byte[]转换成字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2HexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String stmp = Integer.toHexString(b[i] & 0xff);
			if (stmp.length() == 1)
				sb.append("0" + stmp);
			else
				sb.append(stmp);
		}
		return sb.toString();
	}

	/**
	 * 16进制转换成byte[]
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] String2Byte(String hexString) {
		if (hexString.length() % 2 == 1)
			return null;
		byte[] ret = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			ret[i / 2] = Integer.decode("0x" + hexString.substring(i, i + 2))
					.byteValue();
		}
		return ret;
	}

}

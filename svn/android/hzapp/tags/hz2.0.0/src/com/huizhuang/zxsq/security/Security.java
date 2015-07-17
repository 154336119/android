package com.huizhuang.zxsq.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.huizhuang.zxsq.utils.StringEncodeUtil;

/**
 * 加密工具类负责数据加解密
 */
public class Security {

	private static final int ONE_BLOCK_LENGTH = 8;
	private static final String DATA_LENGTH_ERROR = "data length is error!";
	private static final String ENCODING_TYPE = "UTF-8";
	private static final String RSA_ECB_PKCS1_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	private static final String RSA_ALGORITHM = "RSA";
	private static final String DES_ECB_TRANSFORMATION = "DES/ECB/NoPadding";
	private static final String DES_ALGORITHM = "DES";
	private static final String STRING_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 生成会话密钥
	 * 
	 * @param len
	 *            生成密钥字节数
	 * @return 会话密钥
	 */
	public static byte[] generateSessionKey(int len) {
		byte[] tempPool = STRING_POOL.getBytes();
		Random random = new Random();
		byte[] key = new byte[len];
		for (int i = 0; i < len; i++) {
			int offset = Math.abs(random.nextInt()) % tempPool.length;
			key[i] = tempPool[offset];
		}
		return key;
	}

	public static byte[] generateSessionKey(String data, int len) {
		byte[] f4sign = StringEncodeUtil.hexDecode(data);
		byte[] tempPool = STRING_POOL.getBytes();
		Random random = new Random();
		byte[] key = new byte[len];
		for (int i = 0; i < 4; i++) {
			key[i] = f4sign[i];
		}
		for (int i = 4; i < len; i++) {
			int offset = Math.abs(random.nextInt()) % tempPool.length;
			key[i] = tempPool[offset];
		}
		return key;
	}

	/**
	 * des ecb加密
	 * 
	 * @param data
	 *            明文数据
	 * @param key
	 *            密钥
	 * @return 密文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 */
	public static byte[] desECBEncrypt(byte[] data, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return desEncrypt(data, key, DES_ECB_TRANSFORMATION);
	}

	public static byte[] desEncrypt(byte[] data, byte[] key,
			String transformation) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		DESKeySpec spec = new DESKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory
				.getInstance(DES_ALGORITHM);
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		return cipher.doFinal(data);
	}

	/**
	 * DES ECB解密
	 * 
	 * @param data
	 *            密文数据
	 * @param key
	 *            密钥
	 * @return 明文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 */
	public static byte[] desECBDecrypt(byte[] data, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		return desDecrypt(data, key, DES_ECB_TRANSFORMATION);
	}

	public static byte[] desDecrypt(byte[] data, byte[] key,
			String transformation) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		DESKeySpec spec = new DESKeySpec(key);
		SecretKeyFactory keyfactory = SecretKeyFactory
				.getInstance(DES_ALGORITHM);
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, deskey);
		return cipher.doFinal(data);
	}

	/**
	 * 3DES ECB加密
	 * 
	 * @param data
	 *            明文数据
	 * @param key
	 *            密钥
	 * @return 密文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 */
	public static byte[] triDesECBEncrypt(byte[] data, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] key1 = new byte[ONE_BLOCK_LENGTH];
		byte[] key2 = new byte[ONE_BLOCK_LENGTH];
		System.arraycopy(key, 0, key1, 0, key1.length);
		System.arraycopy(key, ONE_BLOCK_LENGTH, key2, 0, key2.length);
		byte[] ret = desECBEncrypt(data, key1);
		ret = desECBDecrypt(ret, key2);
		return desECBEncrypt(ret, key1);
	}

	/**
	 * 3DES ECB解密
	 * 
	 * @param data
	 *            密文数据
	 * @param key
	 *            密钥
	 * @return 明文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 */
	public static byte[] triDesECBDecrypt(byte[] data, byte[] key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] key1 = new byte[ONE_BLOCK_LENGTH];
		byte[] key2 = new byte[ONE_BLOCK_LENGTH];
		System.arraycopy(key, 0, key1, 0, key1.length);
		System.arraycopy(key, ONE_BLOCK_LENGTH, key2, 0, key2.length);
		byte[] ret = desECBDecrypt(data, key1);
		ret = desECBEncrypt(ret, key2);
		return desECBDecrypt(ret, key1);
	}

	private static PublicKey getPublicKey(String modulus, String publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		BigInteger m = new BigInteger(modulus, 10);
		BigInteger e = new BigInteger(publicExponent, 10);
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;

	}

	private static PrivateKey getPrivateKey(String modulus,
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
	 *            明文数据
	 * @param modulus
	 *            公钥模数
	 * @param publicExponent
	 *            公钥指数
	 * @return RSA密文
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无RSA算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无PKCS1Padding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static byte[] rsaEncrypt(byte[] plainText, String modulus,
			String publicExponent) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		return rsaEncrypt(plainText, modulus, publicExponent,
				RSA_ECB_PKCS1_TRANSFORMATION);
	}

	public static byte[] rsaEncrypt(byte[] plainText, String modulus,
			String publicExponent, String transformation)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(transformation);
		PublicKey publicKey = getPublicKey(modulus, publicExponent);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(plainText);
	}

	/**
	 * RSA解密
	 * 
	 * @param cipherText
	 *            密文数据
	 * @param modulus
	 *            公钥模数
	 * @param privateExponet
	 *            公钥指数
	 * @return 明文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无RSA算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无PKCS1Padding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static byte[] rsaDecrypt(byte[] cipherText, String modulus,
			String privateExponet) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		return rsaDecrypt(cipherText, modulus, privateExponet,
				RSA_ECB_PKCS1_TRANSFORMATION);
	}

	public static byte[] rsaDecrypt(byte[] cipherText, String modulus,
			String privateExponet, String transformation)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(transformation);
		PrivateKey privateKey = getPrivateKey(modulus, privateExponet);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] deBytes = cipher.doFinal(cipherText);
		return deBytes;
	}

	/**
	 * 加密cupmobile数据
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            明文数据
	 * @return 密文
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static String encryptSection(byte[] key, String data)
			throws UnsupportedEncodingException, InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		byte[] temp = data.getBytes(ENCODING_TYPE);
		int len = temp.length;
		int rest = len % ONE_BLOCK_LENGTH;
		if (rest != 0) {
			len = (len / ONE_BLOCK_LENGTH + 1) * ONE_BLOCK_LENGTH;
		}
		byte[] d = new byte[len];
		byte[] one = new byte[ONE_BLOCK_LENGTH];
		for (int i = 0; i < temp.length / ONE_BLOCK_LENGTH; i++) {
			System.arraycopy(temp, i * ONE_BLOCK_LENGTH, one, 0,
					ONE_BLOCK_LENGTH);
			byte[] ret = triDesECBEncrypt(one, key);
			System.arraycopy(ret, 0, d, i * ONE_BLOCK_LENGTH, ONE_BLOCK_LENGTH);
		}
		if (rest != 0) {
			System.arraycopy(temp, temp.length / ONE_BLOCK_LENGTH
					* ONE_BLOCK_LENGTH, one, 0, rest);
			for (int i = rest; i < ONE_BLOCK_LENGTH; i++) {
				one[i] = 0;
			}
			byte[] ret = triDesECBEncrypt(one, key);
			System.arraycopy(ret, 0, d, temp.length / ONE_BLOCK_LENGTH
					* ONE_BLOCK_LENGTH, ONE_BLOCK_LENGTH);
		}
		return StringEncodeUtil.hexEncode(d);
	}

	/**
	 * 解密cpumobile数据
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            密文数据
	 * @return 明文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static String decryptSection(byte[] key, String data)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		byte[] temp = StringEncodeUtil.hexDecode(data);
		if (temp.length == 0 || temp.length % ONE_BLOCK_LENGTH != 0) {
			throw new IllegalBlockSizeException(DATA_LENGTH_ERROR);
		}
		byte[] plain = new byte[temp.length];
		byte[] one = new byte[ONE_BLOCK_LENGTH];
		for (int i = 0; i < temp.length; i += ONE_BLOCK_LENGTH) {
			System.arraycopy(temp, i, one, 0, ONE_BLOCK_LENGTH);
			byte[] ret = triDesECBDecrypt(one, key);
			System.arraycopy(ret, 0, plain, i, ONE_BLOCK_LENGTH);
		}
		int reallen = plain.length;
		// 去掉后面0
		for (; reallen > 0; reallen--) {
			if (plain[reallen - 1] != 0) {
				break;
			}
		}
		return new String(plain, 0, reallen, ENCODING_TYPE);
	}

	/**
	 * 按照OTA包格式加密数据
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            明文数据
	 * @return 密文
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static byte[] encryptOTAPackage(byte[] key, byte[] data)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		// 需要首先填充0x80,然后不为8的整数倍再填0x00
		int len = data.length + 1;
		if (len % ONE_BLOCK_LENGTH != 0) {
			len = (len / ONE_BLOCK_LENGTH + 1) * ONE_BLOCK_LENGTH;
		}
		byte[] ret = new byte[len];
		byte[] plainData = new byte[len];
		System.arraycopy(data, 0, plainData, 0, data.length);
		plainData[data.length] = (byte) 0x80;
		byte[] one = new byte[ONE_BLOCK_LENGTH];
		for (int i = 0; i < ret.length / ONE_BLOCK_LENGTH; i++) {
			System.arraycopy(plainData, i * ONE_BLOCK_LENGTH, one, 0,
					ONE_BLOCK_LENGTH);
			byte[] oneRet = triDesECBEncrypt(one, key);
			System.arraycopy(oneRet, 0, ret, i * ONE_BLOCK_LENGTH,
					ONE_BLOCK_LENGTH);
		}
		return ret;
	}

	/**
	 * 按照OTA包格式解密数据
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            密文数据
	 * @return 明文数据
	 * @throws InvalidKeyException
	 *             无效密钥
	 * @throws NoSuchAlgorithmException
	 *             无DES算法
	 * @throws InvalidKeySpecException
	 *             不符合密钥规格
	 * @throws NoSuchPaddingException
	 *             无NoPadding填充类型
	 * @throws BadPaddingException
	 *             填充错误
	 * @throws IllegalBlockSizeException
	 *             数据长度不正确
	 * @throws UnsupportedEncodingException
	 *             不支持UTF-8编码
	 */
	public static byte[] decryptOTAPackage(byte[] key, byte[] data)
			throws IllegalBlockSizeException, InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, BadPaddingException {
		if (data.length == 0 || data.length % ONE_BLOCK_LENGTH != 0) {
			throw new IllegalBlockSizeException(DATA_LENGTH_ERROR);
		}
		byte[] plainData = new byte[data.length];
		byte[] one = new byte[ONE_BLOCK_LENGTH];
		for (int i = 0; i < data.length; i += ONE_BLOCK_LENGTH) {
			System.arraycopy(data, i, one, 0, ONE_BLOCK_LENGTH);
			byte[] ret = triDesECBDecrypt(one, key);
			System.arraycopy(ret, 0, plainData, i, ONE_BLOCK_LENGTH);
		}
		return plainData;
	}

	public static String  getHMACSHA256String(String value) {
		try{
			if(value==null) value="";
		String macKey = "huizhuanggougou";
		String macData = value;
		System.out.println("MACDATA:" + macData);

		Mac mac = Mac.getInstance("HmacSHA256");
		// get the bytes of the hmac key and data string
		byte[] secretByte = macKey.getBytes("UTF-8");
		byte[] dataBytes = macData.getBytes("UTF-8");
		SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");

		mac.init(secret);
		byte[] doFinal = mac.doFinal(dataBytes);
		byte[] hexB = new Hex().encode(doFinal);
		String checksum = new String(hexB);
		System.out.println("checksum :" + checksum);
		return checksum;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}

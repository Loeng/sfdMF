package com.ekfans.pub.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ekfans.base.system.util.SystemConst;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptionAndDecryption {

	private static Logger log = LoggerFactory.getLogger(EncryptionAndDecryption.class);
	private static final BASE64Encoder base64Encoder = new BASE64Encoder();
	private static final BASE64Decoder base64Decoder = new BASE64Decoder();

	/**
	 * 加密方法 返回的是Base64编码后的字符串
	 * 
	 * @param rawKeyData
	 * @param str
	 * @return
	 */
	public static String encrypt(byte rawKeyData[], String str) {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = null;
		SecretKeyFactory keyFactory = null;
		SecretKey key = null;
		Cipher cipher = null;
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			dks = new DESKeySpec(rawKeyData);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			keyFactory = SecretKeyFactory.getInstance("DES");
			key = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 现在，获取数据并加密
			byte data[] = str.getBytes();
			// 正式执行加密操作
			byte[] encryptedData = cipher.doFinal(data);
			for (int i = 0; i < encryptedData.length; i++) {
				System.out.print(encryptedData[i]);
			}

			// 返回Base64编码
			return base64Encoder.encode(encryptedData);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return "";
		}
	}

	/**
	 * 解密方法
	 * 
	 * @param rawKeyData
	 * @param encryptedData
	 * @return
	 */
	public static String decrypt(byte rawKeyData[], String encryptedStr) {
		byte[] encryptedData = null;

		DESKeySpec dks = null;
		SecretKeyFactory keyFactory = null;
		try {
			// 先进行base64解码
			encryptedData = base64Decoder.decodeBuffer(encryptedStr);
			// 从原始密匙数据创建一个DESKeySpec对象
			dks = new DESKeySpec(rawKeyData);
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
			keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dks);
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, key, sr);
			// 正式执行解密操作
			byte decryptedData[] = cipher.doFinal(encryptedData);

			return new String(decryptedData);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return "";
		}
	}

	public static void main(String[] args) {
//		long currentNumber = new Date().getTime();
		String tempcurrent = "11fBVMs0PEPdlBewszA+AlHw==11";
		String aa = URLEncoder.encode(tempcurrent);
		System.out.println(aa);
		System.out.println(URLDecoder.decode(aa));
	}
}

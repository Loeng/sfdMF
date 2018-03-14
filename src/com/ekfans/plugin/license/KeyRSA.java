package com.ekfans.plugin.license;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;

import javax.crypto.Cipher;

/**
 * 用来做License加解密用的核心类
 * 
 * @Title: KeyRSA.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author Lgy
 * @date 2013-12-23
 * @version 1.0
 */
public class KeyRSA {

	public String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
			// System.out.println(arrOut[i/2]);
		}
		return arrOut;
	}

	public String Decoder(String pwdStr) throws Exception {
		if (null == pwdStr || pwdStr.equals("")) {
			throw new Exception("解密参数不能为空!");
		}
		String url = getClass().getResource("private_key.dat").getPath();
		FileInputStream private_file = new FileInputStream(url);
		ObjectInputStream private_oos = new ObjectInputStream(private_file);
		PrivateKey private_key = (PrivateKey) private_oos.readObject();
		byte[] bs = hexStr2ByteArr(pwdStr);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, private_key);
		byte[] t2 = cipher.doFinal(bs);
		return new String(t2);
	}
}

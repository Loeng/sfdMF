package com.ekfans.plugin.webService.bcs;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.sunline.sign.util.CertUtil;
import com.sunline.sign.util.SignUtil;

public class BcsSignHelper {
	public static String sign(String str) {

		String signedStr = "";

		// try {
		// str = new String(str.getBytes("UTF-8"), "GBK");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		byte[] signed = null;
		// 签名
		try {
			// String path =
			// "/Users/liuguoyu/Documents/Projects/MetaFscm/WebRoot/WEB-INF/classes";
			String path = BcsSignHelper.class.getClassLoader().getResource("/").getPath();
			if (path.endsWith("/") || path.endsWith("\\")) {
				path = path + "bcsPay/604.pfx";
			} else {
				path = path + "/bcsPay/604.pfx";
			}
			// String path = Cache.getBCSResource("SIGN.KEY.PFX");
			InputStream in = new java.io.FileInputStream(path);// .pfx证书文件存放路径
			// 根据证书获取密钥
			PrivateKey myprikey = CertUtil.readPrivateKeyFromPKCS12StoredFile(in, "123456");
			// 关闭流
			in.close();
			// 使用SHA-1签名算法对证书进行签名
			signed = SignUtil.sign(myprikey, str.getBytes("GBK"), "SHA1WithRSA");
			// 把字符数组形式的签名转换成16进制
			signedStr = ByteUtil.bytesToHexString(signed);
			return signedStr;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			System.out.println("签名");
		}

		return null;
	}

	public static Boolean checkSign(String signStr, String bodyStr) {

		// try {
		// bodyStr = new String(bodyStr.getBytes("UTF-8"), "GBK");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		// 验签
		try {
			// String path =
			// "/Users/liuguoyu/Documents/Projects/MetaFscm/WebRoot/WEB-INF/classes";
			String path = BcsSignHelper.class.getClassLoader().getResource("/").getPath();
			// String path =
			// "/Users/liuguoyu/Documents/Projects/MetaFscm/WebRoot/";
			if (path.endsWith("/") || path.endsWith("\\")) {
				path = path + "bcsPay/604.cer";
			} else {
				path = path + "/bcsPay/604.cer";
			}
			// String path = Cache.getBCSResource("SIGN.KEY.CER");
			InputStream in = new java.io.FileInputStream(path);// .cer签名证书文件存放路径
			PublicKey pubkey = CertUtil.readPublicKeyFromX509StoredFile(in);// 获取证书的签名
			in.close();
			byte content[] = bodyStr.getBytes("GBK");
			// 把16进制转换为字符数组
			byte signature[] = ByteUtil.hexStringToByte(signStr);
			if (SignUtil.verify(pubkey, content, signature, "SHA1WithRSA")) {// 根据SHA1WithRSA签名算法对签名进行验证
				return true;
			} else {
				return false;
			}
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		String aa = "<Body><Response><IS_SUCCESS>您已注册成功</IS_SUCCESS><ACCOUNT_NO>80015296060101910000008</ACCOUNT_NO></Response></Body>";
		String signStr = sign(aa);
		System.out.println(signStr);
		System.out.println(checkSign(signStr, aa));

	}
}

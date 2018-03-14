package com.ekfans.plugin.webService.bcs;

public class ByteUtil {
	 /**
	   * 把16进制字符串转换成字节数组
	   * @param hexString
	   * @return byte[]
	   */
	  public static byte[] hexStringToByte(String hex) {
	   int len = (hex.length() / 2);
	   byte[] result = new byte[len];
	   char[] achar = hex.toCharArray();
	   for (int i = 0; i < len; i++) {
	    int pos = i * 2;
	    result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
	   }
	   return result;
	  }
	  
	 private static int toByte(char c) {
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);
	    return b;
	 }

	 /**
	  * 数组转换成十六进制字符串
	  * @param byte[]
	  * @return HexString
	  */
	 public static final String bytesToHexString(byte[] bArray) {
	  StringBuffer sb = new StringBuffer(bArray.length);
	  String sTemp;
	  for (int i = 0; i < bArray.length; i++) {
	   sTemp = Integer.toHexString(0xFF & bArray[i]);
	   if (sTemp.length() < 2)
	    sb.append(0);
	   sb.append(sTemp.toUpperCase());
	  }
	  return sb.toString();
	 }
}

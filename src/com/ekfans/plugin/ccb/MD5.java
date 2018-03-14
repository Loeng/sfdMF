package com.ekfans.plugin.ccb;

import java.security.MessageDigest;

/**
 * MD5算法
 *
 * @author LSQ
 */
public class MD5 {

    // / <summary>
    // / 字符串MD5加密
    // / </summary>
    // / <param name="Text">要加密的字符串</param>charset字符编码
    // / <returns>密文</returns>
    public final static String getMD5(String text, String charset) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = text.getBytes(charset); // 设字符编码
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(strTemp);

            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
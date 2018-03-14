package com.ekfans.pub.util;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
/** 
 * 图片与BASE64编码互转工具类 
 * 
 */  
public class Base64Util {  
      
    public static void main(String[] args) {  
        // Base64编码 生成图片
      //  GenerateImage(base64Str, "G:\\testABC.png");  
        // byte数组转换为Base64编码  
        //System.out.println(bytes2base64(new byte[1024]));
    }  

    /** 
     * 图片文件转换为Base64编码
     * @param imgFilePath 图片路径 
     * @return String 
     */  
    public static String getImageBase64Str(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理  
        if(imgFilePath == null || imgFilePath == ""){  
            return "";  
        }  
        File file = new File(imgFilePath);  
        if(!file.exists()){  
            return "";  
        }  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            InputStream in = new FileInputStream(imgFilePath);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串  
    }  
    
    /** 
     * Base64编码 生成图片
     * @param imgStr Base64字符串 
     * @param imgFilePath 生成图片保存路径  
     * @return boolean 
     */  
    public static boolean generateBase64Image(String imgStr, String imgFilePath) {  
        if (imgStr == null) // 图像数据为空  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {  
            // Base64解码  
            byte[] bytes = decoder.decodeBuffer(imgStr);  
            for (int i = 0; i < bytes.length; ++i) {  
                if (bytes[i] < 0) {// 调整异常数据  
                    bytes[i] += 256;  
                }  
            }  
            // 生成jpeg图片  
            OutputStream out = new FileOutputStream(imgFilePath);  
            out.write(bytes);  
            out.flush();  
            out.close();  
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }  
      
    /** 
     * 对Base64字符串进行Base64解码并生成图片 
     * @param imgStr 图片字符串 
     * @return byte[] 
     */  
    public static byte[] getStrToBytes(String imgStr) {   
        if (imgStr == null) // 图像数据为空  
            return null;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try {  
            // Base64解码  
            byte[] bytes = decoder.decodeBuffer(imgStr);  
            for (int i = 0; i < bytes.length; ++i) {  
                if (bytes[i] < 0) {// 调整异常数据  
                    bytes[i] += 256;  
                }  
            }  
            // 生成jpeg图片  
            return bytes;  
        } catch (Exception e) {  
            return null;  
        }  
    }  
    /** 
     * 图片byte数组转换为Base64编码
     * @param imgFilePath 图片路径 
     * @return String 
     */  
    public static String bytes2base64(byte[] data) {  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串  
    } 
    
}   

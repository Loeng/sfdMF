package com.ekfans.pub.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * @ClassName: ZipUtil
 * @Description: TODO(对压缩包的处理)
 * @author Jin
 * @date 2014-12-9 上午9:31:34
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class ZipUtil {
    private static final int buffer = 2048;

    /**
     * @Title: unZip
     * @Description: TODO(根据输入流减压zip) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param is
     *            void 返回类型
     * @param outPath
     *            减压输出路径
     * @param inPath
     *            减压输入路径
     * @throws
     */
    public static Map<String, List<String>> unZip(String inPath,HttpServletRequest request, String filePath) {
        //绝对路径
        String outPath = request.getSession().getServletContext().getRealPath("/") + filePath;
        // 设置通道长度
        byte[] readContent = new byte[1024];
        // 设置返回Map
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        try {
            ZipFile zip = new ZipFile(inPath, "GBK");
            Enumeration entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                //获取文件名称
                String fileOldName = ze.getName();
                //如果有/去掉只留下名称
                if(fileOldName.indexOf("/")!=-1){
                    fileOldName =fileOldName.substring(fileOldName.indexOf("/")+1);
                }
                String path = outPath  + fileOldName;
                // 判断是否是目录
                if (ze.isDirectory()) {
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                    BufferedInputStream bi = new BufferedInputStream(zip.getInputStream(ze));
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.flush();
                    bos.close();
                    
                    String productNo = "";
                    
                    if (fileOldName.indexOf("-") != -1) {
                        String[] names = fileOldName.split("-");
                        if (names != null && names.length > 0) {
                            productNo = names[0];
                        }
                    } else {
                        if (fileOldName.indexOf(".") != -1) {
                            productNo = fileOldName.substring(0,fileOldName.indexOf("."));
                        } else {
                            productNo = fileOldName;
                        }
                    }
                    List<String> fileList = null;
                    if (!map.containsKey(productNo)) {
                        fileList = new ArrayList<String>();
                    } else {
                        fileList = map.get(productNo);
                    }
                    fileList.add(filePath + fileOldName);
                    map.put(productNo, fileList);
                }
            }
            zip.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 判断减压文件类型
    public static boolean isPics(String filename) {
        boolean flag = false;
        if (filename.endsWith(".jpg") || filename.endsWith(".gif") || filename.endsWith(".bmp")
                || filename.endsWith(".png"))
            flag = true;
        return flag;
    }

}

package com.ekfans.pub.util;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
 
/**
 * 基于 httpclient 4.3.1版本的 http工具类
 * @author mcSui
 *
 */
public class HttpClientUtil {
 
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
 
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(180000).setSocketTimeout(180000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }
 
    public static String doGet(String url, Map<String, String> params){
        return doGet(url, params,CHARSET);
    }
    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,CHARSET);
    }
    /**
     * HTTP Get 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doGet(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            entity.consumeContent();
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
    /**
     * HTTP Post 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public static String doPost(String url,Map<String,String> params,String charset){
        if(StringUtils.isBlank(url)){
            return null;
        }
        try {
            List<NameValuePair> pairs = null;
            if(params != null && !params.isEmpty()){
                pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs != null && pairs.size() > 0){
                httpPost.setEntity(new UrlEncodedFormEntity(pairs,CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                result = EntityUtils.toString(entity, "utf-8");
            }
            entity.consumeContent();
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String []args){
    	// 定于数据map,存放数据
 		Map<String, Object> paramMap = new HashMap<String, Object>();
 		// 定义数据list 存放数据map
 		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
 		// 定义map,用于存放数据list
 		Map<String, Object> map = new HashMap<String, Object>();
 		
 		// 定义map,用于接口传递数据
 		Map<String, String> tempMap = new HashMap<String, String>();
 		paramMap.put("companyId", "4544");
 		paramMap.put("carNo", "51018316446545");
 		paramMap.put("carType", "1");
 		paramMap.put("fullWeight", 54);
 		paramMap.put("startTime", DateUtil.getSysDateString());
 		paramMap.put("endTime", DateUtil.getSysDateString());
 		paramMap.put("ysNo", "5614553");
 		paramMap.put("fullNum", 2);
 		paramMap.put("xszFileIn", FileUtil.getInputStream("C:/Users/Administrator/Desktop/学习文档/Jetbrick1.2.2帮助文档.pdf"));
 		paramMap.put("sourceId", "5445454534");
 		paramMap.put("orgId", "0001");
 		listMap.add(paramMap);
 		
 		map.put("carInfos", listMap);
 		// 对应接编码
 		tempMap.put("source", "101");
 		// orgId 区分组织机构
 		tempMap.put("orgId","0001");
 		// 密钥,验证平台
 		tempMap.put("key","hnsfd20160218ekfans");
 		// 数据
 		tempMap.put("data", JsonUtil.convertToJsonString(map));
 		// 从配置文件获取第三方url地址
 		String url = "http://192.168.0.188:8080/sfdMonitor/data/monitorSync";
	         
        String postData = doPost(url,tempMap);
        System.out.println(postData);
    }
     
}
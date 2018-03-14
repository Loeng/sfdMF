package com.ekfans.plugin.wuliubao.yunshu.controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.ekfans.pub.util.StringUtil;

/**
 * 百度地图相关工具类
 * @author pp
 * @Date 2017年8月1日09:21:30
 */
public class BaiduMapUtil {
	//百度的ak值
	private static String AK = "5uA73MkTqoa86neL4RXQScbFVO9GoCUO";
	 /**
     * 根据地址获得经纬度
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
     */
    public static LatitudeAndLongitude getLngAndLat(String address) throws JSONException, UnsupportedEncodingException {
        LatitudeAndLongitude latAndLng = new LatitudeAndLongitude();
        //使用UTF-8编码
        String add = URLEncoder.encode(address, "UTF-8");
        String url = "http://api.map.baidu.com/geocoder/v2/?address=" + add + "&output=json&ak="+AK+"";
        String json = loadJSON(url);
        if (StringUtil.isEmpty(json)) {
            return latAndLng;
        }
        int len = json.length();
        // 如果不是合法的json格式
        if (json.indexOf("{") != 0 || json.lastIndexOf("}") != len - 1) {
            return latAndLng;
        }
        JSONObject obj =  new JSONObject(json);
        if (obj.get("status").toString().equals("0")) {
            double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            latAndLng.setLatitude(lat);
            latAndLng.setLongitude(lng);
        }
        return latAndLng;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            URLConnection uc = urlObj.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ((inputLine = br.readLine()) != null) {
                json.append(inputLine);
            }
            br.close();
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }catch (Exception e) {
        	e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 测试方法 说明：把代码中的ak值（红色字部分）更改为你自己的ak值，在百度地图API中注册一下就有。
     * 百度路径：http://lbsyun.baidu.com/index.php?title=webapi/guide/changeposition
     * @throws JSONException 
     * @throws UnsupportedEncodingException 
     */
    public static void main(String[] args) throws JSONException, UnsupportedEncodingException {
        LatitudeAndLongitude latAndLng = BaiduMapUtil.getLngAndLat("湖南,长沙");
        System.out.println("经度：" + latAndLng.getLongitude() + "---纬度：" + latAndLng.getLatitude());
    }
    
    /**
     * 物流宝获取开始地 目的地的距离 单位 km
     * @param startFullPath
     * @param endFullPath
     * @return
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public static double getCommodityDistance(String startFullPath,String endFullPath) throws UnsupportedEncodingException, JSONException{
    	LatitudeAndLongitude start = BaiduMapUtil.getLngAndLat(startFullPath);
    	LatitudeAndLongitude end = BaiduMapUtil.getLngAndLat(endFullPath);
    	double d = getDistance(start.getLongitude(),start.getLatitude(),end.getLongitude(),end.getLatitude());
    	return d;
    }
    
    /**
     * 物流宝获取商品离我的的距离 单位 km
     * @param startFullPath
     * @param endFullPath
     * @return
     * @throws UnsupportedEncodingException
     * @throws JSONException
     */
    public static double getDistanceMe(String startFullPath,String latitude,String longitude) throws UnsupportedEncodingException, JSONException{
    	LatitudeAndLongitude start = BaiduMapUtil.getLngAndLat(startFullPath);
    	double lat = Double.valueOf(latitude);
    	double lon = Double.valueOf(longitude);
    	double d = getDistance(start.getLongitude(),start.getLatitude(),lon,lat);
    	return d;
    }
    
    /**
     * 补充：计算两点之间真实距离
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d;
    }

}

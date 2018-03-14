package com.ekfans.pub.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import sun.net.www.protocol.http.HttpURLConnection;

public class HttpUtil {

	private static final String APPKEY = "RC-App-Key";
	private static final String NONCE = "RC-Nonce";
	private static final String TIMESTAMP = "RC-Timestamp";
	private static final String SIGNATURE = "RC-Signature";
	private static String proxyHost = null;
	private static Integer proxyPort = null;
	private static String charset = "utf-8";

	// 设置body体
	public static void setBodyParameter(StringBuilder sb, HttpURLConnection conn) throws IOException {
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.writeBytes(sb.toString());
		out.flush();
		out.close();
	}

	// 添加签名header
	public static HttpURLConnection CreatePostHttpConnection(String appKey, String appSecret, String uri) throws MalformedURLException, IOException, ProtocolException {
		String nonce = String.valueOf(Math.random() * 1000000);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder toSign = new StringBuilder(appSecret).append(nonce).append(timestamp);
		String sign = CodeUtil.hexSHA1(toSign.toString());

		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setInstanceFollowRedirects(true);
		conn.setConnectTimeout(300000);
		conn.setReadTimeout(300000);

		conn.setRequestProperty(APPKEY, appKey);
		conn.setRequestProperty(NONCE, nonce);
		conn.setRequestProperty(TIMESTAMP, timestamp);
		conn.setRequestProperty(SIGNATURE, sign);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		return conn;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

//	public static SdkHttpResult returnResult(HttpURLConnection conn) throws Exception, IOException {
//		String result;
//		InputStream input = null;
//		if (conn.getResponseCode() == 200) {
//			input = conn.getInputStream();
//		} else {
//			input = conn.getErrorStream();
//		}
//		result = new String(readInputStream(input));
//		return new SdkHttpResult(conn.getResponseCode(), result);
//	}
	
	/**
     * Do POST request
     * @param url
     * @param parameterMap
     * @return
     * @throws Exception 
     */
    public static String doPost(String url, Map<String, Object> parameterMap) throws Exception {
        /* Translate parameter map to parameter date string */
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            Object value = null;
            while (iterator.hasNext()) {
                key = (String)iterator.next();
                if (parameterMap.get(key) != null) {
                    value = (Object)parameterMap.get(key);
                } else {
                    value = "";
                }
                
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        
        URLEncoder.encode(parameterBuffer.toString(),"UTF-8");
        System.out.println(parameterBuffer);
//        System.out.println("POST parameter : " + parameterBuffer.toString());
        
//        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
//        httpURLConnection.setChunkedStreamingMode(999999999);
        URL localURL = new URL(url);
        
        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        try {
        	
        	
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            
            outputStreamWriter.write(parameterBuffer.toString());
            
            outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
            if (httpURLConnection.getResponseCode() == 404) {
                throw new Exception("404");
            }
            
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            
            if (outputStream != null) {
                outputStream.close();
            }
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }

	/**
	 * Do POST request
	 * @param url
	 * @param parameterMap
	 * @return
	 * @throws Exception 
	 */
	public static String doPost(String url, JSONObject parameterMap) throws Exception {
		/* Translate parameter map to parameter date string */
		StringBuffer parameterBuffer = new StringBuffer();
		if (parameterMap != null) {
			Iterator iterator = parameterMap.keys();
			String key = null;
			Object value = null;
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				if (parameterMap.get(key) != null) {
					value = (Object) parameterMap.get(key);
				} else {
					value = "";
				}

				parameterBuffer.append(key).append("=").append(value);
				if (iterator.hasNext()) {
					parameterBuffer.append("&");
				}
			}
		}
		// HttpURLConnection httpURLConnection = (HttpURLConnection)new
		// URL(url).openConnection();
		// httpURLConnection.setChunkedStreamingMode(999999999);
		URL localURL = new URL(url);

		URLConnection connection = openConnection(localURL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", charset);
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		try {
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.write(parameterBuffer.toString());
			outputStreamWriter.flush();
			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}
			if (httpURLConnection.getResponseCode() == 404) {
				throw new Exception("404");
			}
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			reader = new BufferedReader(inputStreamReader);
			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}

    /**
     * @Title doGet
     * @Description TODO(get提交) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param url
     * @return
     * @throws Exception String
     * @Auth：成都易科远见科技有限公司
     * @date：2015下午4:24:17
     */
    public static String doGet(String url) throws Exception {
        
        URL localURL = new URL(url);
        
        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            
        } finally {
            
            if (reader != null) {
                reader.close();
            }
            
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            
            if (inputStream != null) {
                inputStream.close();
            }
            
        }

        return resultBuffer.toString();
    }
    
    
    private static URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}

package com.chendeji.rongchen.server.httpCommon;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.chendeji.rongchen.common.util.Logger;

import android.text.TextUtils;

/**
 * 访问网络工具类
 * @author chendeji
 *
 */
public class HttpRequestClient {
	
	/**
	 * get方式访问
	 * @param encode 编码方式
	 * @return result get方式访问网络返回的数据
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	public static int get(String url, String encode, StringBuilder builder) throws ClientProtocolException, IOException{
		if(TextUtils.isEmpty(url) || TextUtils.isEmpty(encode)){
			throw new NullPointerException();
		}
		url = url.concat(encode);
        Logger.i("chendeji", "发送的请求路径" + url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpParams params = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(params, 5000); // 设置超时时间
		HttpConnectionParams.setSoTimeout(params, 5000); // 设置超时时间
		httpGet.setParams(params);
		HttpResponse response = httpClient.execute(httpGet);
		String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        builder.append(result);
		return response.getStatusLine().getStatusCode();
	}
	
	/**
	 * post方式访问网络
	 * @param url 访问地址
	 * @param entity 实体
	 * @return 服务器返回结果
	 */
	public static String post(String url, HttpEntity entity){
		if(TextUtils.isEmpty(url)){
			throw new NullPointerException();
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpParams params = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(params, 3000); // 设置超时时间
		HttpConnectionParams.setSoTimeout(params, 3000); // 设置超时时间
		httpPost.setParams(params);
		httpPost.setEntity(entity);
		
		String result = null;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

package com.chendeji.rongchen.server.httpCommon;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import com.chendeji.rongchen.server.AppConfigFactory;
import com.chendeji.rongchen.server.AppConst;

public class DPHttpRequestClient {

	/**
	 * 生成访问路径
     * @param paramMap 请求参数
	 * @return 访问路径
	 */
	public static String getQueryString(Map<String, String> paramMap) {
        String appKey = AppConfigFactory.getInstance().getAppConfig(AppConst.AppBaseConst.APP_KEY);
        String appSecret = AppConfigFactory.getInstance().getAppConfig(AppConst.AppBaseConst.APP_SECRET);
		String sign = sign(appKey, appSecret, paramMap);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, String> entry : paramMap.entrySet()) {
			stringBuilder.append('&').append(entry.getKey()).append('=')
					.append(entry.getValue());
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	/**
	 * 生成有进行URL转换的访问路径
     * @param paramMap 请求参数
	 * @return 经过转码的请求路径
	 */
	public static String getUrlEncodedQueryString(Map<String, String> paramMap) {
        String appKey = AppConfigFactory.getInstance().getAppConfig(AppConst.AppBaseConst.APP_KEY);
        String appSecret = AppConfigFactory.getInstance().getAppConfig(AppConst.AppBaseConst.APP_SECRET);
		String sign = sign(appKey, appSecret, paramMap);

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("appkey=").append(appKey).append("&sign=")
				.append(sign);
		for (Entry<String, String> entry : paramMap.entrySet()) {
			try {
				stringBuilder.append('&').append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
                e.printStackTrace();
			}
		}
		String queryString = stringBuilder.toString();
		return queryString;
	}

	/**
	 *
	 * 开始请求资源
	 * @param apiUrl 请求路径
	 * @param paramMap 请求参数
	 * @return 返回结果
	 * @throws java.io.IOException
	 * @throws org.apache.http.client.ClientProtocolException
	 */
	public static int doGet(String apiUrl, Map<String, String> paramMap, StringBuilder builder) throws ClientProtocolException, IOException {
		String queryString = getUrlEncodedQueryString(paramMap);
		int retCode = HttpRequestClient.get(apiUrl, queryString, builder);

		return retCode;
	}

	/**
	 * 生成sign
	 *
     * @param appKey appkey
     * @param secret appsecret
     * @param paramMap 请求参数
	 * @return
	 */
	public static String sign(String appKey, String secret,
			Map<String, String> paramMap) {
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(appKey);
		for (String key : keyArray) {
			stringBuilder.append(key).append(paramMap.get(key));
		}
		stringBuilder.append(secret);
		String codes = stringBuilder.toString();
		String sign = new String(Hex.encodeHex(DigestUtils.sha(codes)))
				.toUpperCase();
		return sign;
	}

}

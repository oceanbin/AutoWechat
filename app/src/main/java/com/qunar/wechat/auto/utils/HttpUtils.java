/***********************************************************************
 *  Copyright (C) 1999-2017,ctrip.com. All rights reserved.
 *
 *  Author:  chen.d       
 *  Date:    2017/4/5
 *  Purpose: 
 *  2017/4/5  chen.d    微信IM消息互通项目
 * ***********************************************************************/
package com.qunar.wechat.auto.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lihaibin.li on 2017/11/20.
 */
public class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName();

    private static final Gson GSON = new Gson();

    private HttpUtils() {
    }

    /**
     * HttpGet
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        Response response = getResponse(url);
        String responseBody = response.body().string();
        return responseBody;
    }

    /**
     * HttpPost
     *
     * @param url
     * @param jsonRequest
     * @return
     * @throws IOException
     */
    public static String post(String url, String jsonRequest) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = getClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("网络请求异常，请稍后重试", e);
        }
    }

    /**
     * 异步post
     * @param url
     * @param jsonRequest
     * @param callback
     */
    public static void post(String url, String jsonRequest, Callback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        getClient().newCall(request).enqueue(callback);
    }

    public static <T> T post(String url, Object requestObject, Class<T> responseTypeClass) {
        String jsonRequest = GSON.toJson(requestObject);
        String responseBody = post(url, jsonRequest);
        if (StringUtils.isBlank(responseBody)) {
            return null;
        }

        return GSON.fromJson(responseBody, responseTypeClass);
    }

    /*** Private Methods ***/
    private static Response getResponse(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        return getClient().newCall(request).execute();
    }

    /**
     * 获取基础配置的OkHttpClient
     *
     * @return
     */
    private static OkHttpClient getClient() {
        return HttpClientHolder.okHttpClient;
    }


    //region Private Methods/Class

    /**
     * ClientHolder
     */
    private static class HttpClientHolder {
        private HttpClientHolder() {
        }

        static OkHttpClient okHttpClient = getOkHttpClient();

        /**
         * 获取OkHttpClient最基础的实例
         *
         * @return
         */
        private static OkHttpClient getOkHttpClient() {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            // ignore
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            // ignore
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLSocketFactory sslSocketFactory = null;
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Exception e) {
                Log.e(TAG, "HttpClient设置SSL异常" + e.getLocalizedMessage());
            }

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            if (sslSocketFactory != null) {
                clientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            }

            return clientBuilder.build();
        }
    }
}

package com.sbkj.car.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description: HttpClient工具类
 * @Author Zangdy
 * @CreateTime 2019/4/30 15:23
 */
@Component
public class HttpClientTool {

    private static int status = 200;
    /**
     * 从链接池获取连接的超时时间
     **/
    private int onnectionRequestTimeout = 1000;
    /**
     * 从client到server的请求超时
     **/
    private int connectTimeout = 1000;
    /**
     * 从server到client的响应超时
     **/
    private int socketTimeout = 1000 * 10;
    /**
     * 创建连接池中使用
     **/
    private static int maxTotal = 100;
    private static int defaultMaxPerRoute = 70;

    /**
     * 创建链接池
     */
    private static PoolingHttpClientConnectionManager pool;

    static {
        pool = new PoolingHttpClientConnectionManager();
        pool.setMaxTotal(maxTotal);
        pool.setDefaultMaxPerRoute(defaultMaxPerRoute);
    }

    /**
     * 发送请求
     */
    public String postRequest(String url, Object object) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pool).build();
        HttpPost httpPost = new HttpPost(url);
        // 设置参数为json格式传输
        httpPost.addHeader("Content-Type", "application/json");
        // 将对象转换成json
        String json = JSONObject.toJSONString(object);
        try {
            // 为post请求设置参数
            httpPost.setEntity(new StringEntity(json));
            httpPost.setConfig(this.getConfig());
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println("============  准备发送!!!");
            if (response.getStatusLine().getStatusCode() == status) {
                System.out.println("============  " + response.getStatusLine().getStatusCode());
                InputStream is = response.getEntity().getContent();
                //构造一个字符流缓存
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str = "";
                while ((str = br.readLine()) != null) {
                    System.out.println("============  HttpClientTool str: " + str);
                }
                //关闭流
                is.close();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private RequestConfig getConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                //从链接池获取连接的超时时间
                .setConnectionRequestTimeout(onnectionRequestTimeout)
                //从client到server的请求超时
                .setConnectTimeout(connectTimeout)
                //从server到client的响应超时
                .setSocketTimeout(socketTimeout)
                .build();
        return requestConfig;
    }

//    @Test
//    public void testPost() throws Exception {
//        for (int i = 0; i < 15; i++) {
//            postRequest("http://127.0.0.1:8180/auth/testA/test?str=11111", "{\n" +
//                    "  \"bid\": 10390,\n" +
//                    "  \"status\": \"sold_out\",\n" +
//                    "  \"data\": []\n" +
//                    "}");
//        }
//    }
}



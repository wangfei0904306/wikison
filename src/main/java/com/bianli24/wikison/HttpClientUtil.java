package com.bianli24.wikison;


import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;


/**
 * HttpClient 工具
 * Created by wanghw on 2017/7/15.
 */
public class HttpClientUtil {

    private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private static OkHttpClient client = new OkHttpClient();

    public static String post(String url,String params) throws Exception{
        RequestBody body = new FormBody.Builder().add("params",params).build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String post(String url,Map<String,String> params) throws Exception{
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,String> entry: params.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
            log.info("params:"+entry.getKey()+" "+entry.getValue());
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postBody(String url,String params) throws Exception{
        RequestBody body = RequestBody.create(MediaType.parse("text/xml; charset=utf-8"),params);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String get(String url) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static InputStream download(String url)throws Exception{
        Request request = new Request.Builder().url(url).build();
        ResponseBody body = client.newCall(request).execute().body();
        return body.byteStream();
    }
}

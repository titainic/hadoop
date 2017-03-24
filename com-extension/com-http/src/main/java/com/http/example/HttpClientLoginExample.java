package com.http.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *htmlunit jsoup 爬虫
 */
public class HttpClientLoginExample
{

    public static void main(String[] args) throws Exception
    {
        HttpClientLoginExample example = new HttpClientLoginExample();
        example.grabPageHTML();
    }

    public void grabPageHTML() throws Exception
    {

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "tykyadmin"));
        nvps.add(new BasicNameValuePair("password", "123"));

        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        HttpPost request = new HttpPost("http://127.0.0.1:8080/rescat/login");
        request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

        HttpResponse response = httpclient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        response.getEntity().toString();
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity, "GBK");
        System.out.println(html);
    }


}

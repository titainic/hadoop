package com.titanic.kylin;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * kylin http api
 */
public class KylinHttpApi
{
    private static String encoding;
    private static String kylin_url = "http://192.9.7.191:7070/kylin/api";
    private static String kylin_user = "ADMIN";
    private static String kylin_pwd = "KYLIN";
    private static String kylin_requestType_post = "POST";
    private static String kylin_requestType_get = "GET";


    public static void main(String[] args)
    {
        login(kylin_url, kylin_pwd);
    }

    private static String login(String user, String pwd)
    {
        String para = "/user/authentication";
        byte[] key = (user + ":" + pwd).getBytes();
        encoding = Base64.encodeBase64String(key);
        return execute(para, kylin_requestType_post, null);
    }

    private static String execute(String para, String requestType, String body)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            URL url = new URL(kylin_url + para);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setDoInput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            connection.setRequestProperty("Content-Type", "application/json");

            if (body != null)
            {
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }
            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null)
            {
                sb.append(line);
            }
            in.close();
            connection.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }


}

package com.emd.emdmobile.app;

import android.net.Credentials;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class webFunction {
    private String strJsonResponse;
    private String strHtmlResponse;
    private boolean boolAuthValid = false;
    private final String userName = "emdexternal";
    private final String password = "3MDW3bu$r";

    public webFunction(){}

    public void sendJSONRequest(String url, JSONObject jObject)
    {
        try
        {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","application/json");

            if(jObject != null) {
            StringEntity se = new StringEntity(jObject.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            }

            String auth = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(),Base64.DEFAULT);
            auth = auth.replace("\n","");
            httpPost.addHeader("Authorization",auth);

            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse rsp = httpClient.execute(httpPost);

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(rsp.getEntity().getContent(), "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                strJsonResponse = sb.toString();
                strJsonResponse = strJsonResponse.replace("\\","");
                strJsonResponse = strJsonResponse.substring(1, strJsonResponse.length() - 1);
            }
            catch(Exception e){
                Log.e("JSON Buffer error", "Error converting result : " + e.toString());
            }

        }
        catch(Exception e)
        {
            Log.e("HTTP_Exception","Error in http connection " + e.toString());
        }
    }

    public void sendDominoAuthRequest(String URL, String Username, String Password, String Database){

        String loginUrl = URL;
        HttpClient client = new DefaultHttpClient();

        try{
            HttpGet get = new HttpGet(loginUrl);
            HttpResponse rsp = client.execute(get);
            HttpEntity ent = rsp.getEntity();

            HttpPost post = new HttpPost(loginUrl);
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("username",Username));
            nvp.add(new BasicNameValuePair("password",Password));
            nvp.add(new BasicNameValuePair("redirectto",Database));
            post.setEntity(new UrlEncodedFormEntity(nvp));
            HttpResponse rsp2 = client.execute(post);

            BufferedReader reader = new BufferedReader(new InputStreamReader(rsp2.getEntity().getContent(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line2;
            while ((line2 = reader.readLine()) != null) {
                sb.append(line2);
            }
            strHtmlResponse = sb.toString();

            Document htmlDoc = Jsoup.parse(strHtmlResponse);
            if(htmlDoc != null){
                Element htmlElem = htmlDoc.select("title").first();
                if(htmlElem != null){
                    boolAuthValid = false;
                }
                else{
                    boolAuthValid = true;
                }
            }
            else{
                boolAuthValid = false;
            }
        }
        catch(Exception e){
        }
    }

    public void sendPushover(String application, String Group, String Message){
        try{
            String Url = "https://api.pushover.net/1/messages.json";

            JSONObject j = new JSONObject();
            j.put("token",application);
            j.put("user",Group);
            j.put("message",Message);

            HttpClient c = new DefaultHttpClient();
            HttpPost p = new HttpPost(Url);
            p.setHeader("Content-type","application/json");

            StringEntity se = new StringEntity(j.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            p.setEntity(se);

            HttpResponse r = c.execute(p);

        }
        catch(JSONException ex){

        }
        catch (UnsupportedEncodingException enc){

        }
        catch (IOException io){

        }
    }

    public String getJsonResponseString(){
        return strJsonResponse;
    }

    public boolean getAuthValid(){
        return boolAuthValid;
    }

    public String getHtmlResponseString(){
        return strHtmlResponse;
    }
}

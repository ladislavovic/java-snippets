/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.httpclient._01_basics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author kulhalad
 */
public class HttpClientMain {

    public static void main(String[] args) throws Exception {
        simpleGETRequest();
//        simplePostRequest();
        //          par();
        //parWorksiteBlock();
        System.out.println(convUserInput("hello world\u00C4 123"));

    }
    
    public static String convUserInput(String userInput) {
        if (userInput == null) return null;
        String result = userInput.replaceAll("[^\\p{L}\\d\\* ]", StringUtils.EMPTY);
        return result;
    }
    
    
    
    private static void parWorksiteBlock() throws IOException {
        Credentials credentials = new UsernamePasswordCredentials("usamid@17842", "L6nqGQxz");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpclient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
               
        HttpPost httpPost = new HttpPost("http://obo.par.se/itb/doc/WorksiteBlock.xml");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("worksiteId", "1:200198879"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            System.out.println("status: " + response.getStatusLine());
     
            HttpEntity entity = response.getEntity();
            StringWriter sw = new StringWriter();
            IOUtils.copy(entity.getContent(), sw, "utf-8"); // TODO get encoding
            System.out.println(sw.toString());            
                    
            // Do something useful with the response body here

            // Fully consume entity and close content stream if exists
            EntityUtils.consume(entity);           
        } finally {
            response.close();
            httpclient.close();
        }
    }
    
    
    private static void par() throws IOException {
//        Credentials credentials = new UsernamePasswordCredentials("usamid@17842", "L6nqGQxz");

        Credentials credentials = new UsernamePasswordCredentials("ariten4@utveckling", "i2jPaeaX");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpclient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
               
        HttpPost httpPost = new HttpPost("http://obo.par.se/itb/doc/S-W-4.xml");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("companyLegalName", "VM Music"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        
        System.out.println("request: " + httpPost);
        
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            System.out.println("status: " + response.getStatusLine());
     
            HttpEntity entity = response.getEntity();
            StringWriter sw = new StringWriter();
            IOUtils.copy(entity.getContent(), sw, "utf-8"); // TODO get encoding
            System.out.println("AAA: " + sw.toString());            
                    
            // Do something useful with the response body here

            // Fully consume entity and close content stream if exists
            EntityUtils.consume(entity);           
        } finally {
            response.close();
            httpclient.close();
        }
    }

    private static void simpleGETRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.google.com");
        CloseableHttpResponse response = httpclient.execute(httpGet);

        try {
            assertEquals(200, response.getStatusLine().getStatusCode());

            HttpEntity entity = response.getEntity();
            assertTrue(entity.getContentType().getValue().contains("text/html"));
            // Do something useful with the response body here
            
            StringWriter sw = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), sw); // TODO set encoding
            System.out.println("AAA: " + sw.toString());
            
            

            // Fully consume entity and close content stream if exists
            EntityUtils.consume(entity);
        } finally {
            // You must close every response, it holds data stream. Without that
            // the underlaying connection can not be reused.
            response.close();

            // You also must close httpclient. I do not what resources it holds
            // but according to documentation it is correct way.
            httpclient.close();
        }
    }

    private static void simplePostRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.google.com");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            assertEquals(405, response.getStatusLine().getStatusCode());
            assertEquals("Method Not Allowed", response.getStatusLine().getReasonPhrase());
            
            HttpEntity entity = response.getEntity();
            assertTrue(entity.getContentType().getValue().contains("text/html"));
            // Do something useful with the response body here

            // Fully consume entity and close content stream if exists
            EntityUtils.consume(entity);           
        } finally {
            response.close();
            httpclient.close();
        }
    }

}

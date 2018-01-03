package cz.kul.app.tomcat;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import junit.framework.AssertionFailedError;

public class TomcatAssert {

    public static void assertGETResponseCode(String url, int expected) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int responseCode = response.getStatusLine().getStatusCode();
                if (expected != responseCode) {
                    throw new AssertionFailedError("Exected response code: " + expected + " Real response code: " + responseCode);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

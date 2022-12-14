package cz.kul.snippets.httpclient.example01_request_with_body;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Example01 {

	@Test
	public void sendPostRequestWithBody() throws Exception {
		String json = "{\n" +
				"  \"from\": [\n" +
				"    {\n" +
				"      \"externalId\": \"AAAAAAAAAAO1\",\n" +
				"      \"systemId\": \"CROSS\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"to\": [\n" +
				"    {\n" +
				"      \"externalId\": \"AAAAAAAAAAB4\",\n" +
				"      \"systemId\": \"CROSS\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"linkTypes\": [\n" +
				"    {\n" +
				"      \"linkType\": \"OPTICAL_FIBRE\",\n" +
				"      \"requiredCapacity\": 0\n" +
				"    },\n" +
				"    {\n" +
				"      \"linkType\": \"OPTICAL_PATH\",\n" +
				"      \"requiredCapacity\": 0\n" +
				"    }\n" +
				"  ],\n" +
				"  \"weightFunctions\": [\n" +
				"    {\n" +
				"      \"weightFunctionName\": \"BY_NODE_HIER_COUNT\",\n" +
				"      \"weightFunctionParams\": {\n" +
				"        \"hierPathPenaltyFactor\": 1,\n" +
				"        \"reconnectPatchWeigth\": 1,\n" +
				"        \"newPatchWeigth\": 1\n" +
				"      },\n" +
				"      \"boost\": 1\n" +
				"    }\n" +
				"  ],\n" +
				"  \"fromToUpHierarchyEnabled\": false  \n" +
				"}"    ;



		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost("http://localhost:7070/shortest-path-tracings");

			StringEntity entity = new StringEntity(json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");


			try (CloseableHttpResponse response = client.execute(httpPost)) {
				HttpEntity entity1 = response.getEntity();
				String entityStr = EntityUtils.toString(entity1);
				System.out.println(entityStr);
			}
		}
	}



}

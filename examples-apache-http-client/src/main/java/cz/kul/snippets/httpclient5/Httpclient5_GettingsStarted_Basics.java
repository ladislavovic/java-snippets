package cz.kul.snippets.httpclient5;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class Httpclient5_GettingsStarted_Basics
{

    @Test
    public void createClientAndPerformAGetRequest() throws Exception
    {
        runHttpServer();



        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            httpClient.execute(
                new HttpGet("http://localhost:8000/test"),
                response -> { // instance of ClassicHttpResponse

                    // get the status code
                    int responseCode = response.getCode();
                    System.out.println("responseCode: " + responseCode);

                    // get the header
                    String headerValue = response.getFirstHeader("Content-Type").getValue();
                    System.out.println("headerValue: " + headerValue);

                    // get the content type
                    ContentType contentType = ContentType.parse(response.getEntity().getContentType());
                    System.out.println("contentType: " + contentType);
                    System.out.println("mimeType: " + contentType.getMimeType());
                    System.out.println("charset: " + contentType.getCharset());

                    // get the body
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    response.getEntity().writeTo(byteArrayOutputStream);
//                    String body = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
//                    System.out.println("body: " + body);

                    // get the body2
                    String body2 = EntityUtils.toString(response.getEntity());
                    System.out.println("body2: " + body2);

                    System.out.println("firstCharacter: " + Integer.toHexString((int) body2.charAt(0)));
                    return body2;
                }
            );

        }
    }

    @Test
    public void removeBOM() throws Exception
    {
        runHttpServer();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            httpClient.execute(
                new HttpGet("http://localhost:8000/test"),
                response -> { // instance of ClassicHttpResponse

                    // modify entity
                    HttpEntity repeatableEntity = convertToRepeatableEntity(response.getEntity());
                    repeatableEntity = doRemoveBOM(repeatableEntity);

                    response.setEntity(repeatableEntity);

                    // modify headers
                    Header firstHeader = response.getFirstHeader("content-length");
                    if (firstHeader != null) {
                        long currentLong = Long.parseLong(firstHeader.getValue());
                        long newLong = currentLong - 3;
                        response.setHeader("content-length", Long.toString(newLong));
                    }

                    System.out.println("Headers:");
                    for (Header header : response.getHeaders()) {
                        System.out.println("    " + header.getName() + ": " + header.getValue());
                    }

                    String body2 = EntityUtils.toString(repeatableEntity);
                    System.out.println("body2: " + body2);
                    System.out.println("firstCharacter: " + Integer.toHexString((int) body2.charAt(0)));

                    return body2;

                }
            );

        }
    }

    private boolean isUtf8Entity(HttpEntity entity) throws IOException
    {
        String contentEncoding = entity.getContentEncoding();
        if (contentEncoding == null) {
            return false;
        }
        return StandardCharsets.UTF_8.equals(Charset.forName(contentEncoding));
    }

    private boolean containsBOM(HttpEntity entity) throws IOException
    {
        byte[] bomUtf8Sequence = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        byte[] firstThreeBytes = new byte[3];
        try (InputStream inputStream = entity.getContent()) {
            int bytesRead = inputStream.read(firstThreeBytes);
        }
        return Arrays.equals(bomUtf8Sequence, firstThreeBytes);
    }

    private ContentType getEntityContentType(HttpEntity entity) {
        var contentType = Optional.ofNullable(entity.getContentType())
            .map(ContentType::parse)
            .orElseGet(() -> ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));

        if (contentType.getCharset() == null) {
            contentType = contentType.withCharset(StandardCharsets.UTF_8);
        }
        return contentType;
    }

    private HttpEntity doRemoveBOM(HttpEntity entity) throws IOException
    {
        byte[] byteArray = toByteArray(entity);
        byte[] modifiedContent = new byte[byteArray.length - 3];
        System.arraycopy(byteArray, 3, modifiedContent, 0, modifiedContent.length);

        return new ByteArrayEntity(modifiedContent, getEntityContentType(entity));
    }

    private HttpEntity removeBOM(HttpEntity entity) throws IOException
    {
        if (!isUtf8Entity(entity)) {
            return entity;
        }
        if (!containsBOM(entity)) {
            return entity;
        }
        return doRemoveBOM(entity);
    }


    public static void runHttpServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException {
//            byte[] bytes = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0x41};
            byte[] bytes = "\uFEFFABC".getBytes(StandardCharsets.UTF_8);

            t.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }



    public static HttpEntity convertToRepeatableEntity(final HttpEntity entity) throws
        IOException
    {
        if (entity.isRepeatable()) {
            return entity;
        }

        byte[] entityContent = toByteArray(entity);
        var contentType = Optional.ofNullable(entity.getContentType())
            .map(ContentType::parse)
            .orElseGet(() -> ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));

        if (contentType.getCharset() == null) {
            contentType = contentType.withCharset(StandardCharsets.UTF_8);
        }

        return new ByteArrayEntity(entityContent, contentType);
    }

    public static byte[] toByteArray(final HttpEntity entity) throws
        IOException
    {
        var outputStream = new ByteArrayOutputStream();
        entity.writeTo(outputStream);
        outputStream.flush();
        return outputStream.toByteArray();
    }




}

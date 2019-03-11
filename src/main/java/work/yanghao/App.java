package work.yanghao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Yano
 * This is a httpclient request the web server and get some
 * message if the request is success !
 * the test rest web server the ip address is http://localhost:8080/
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        //create a CloseableHttpClient Object
        CloseableHttpClient closeableHttpClient = createCloseableHttpClient();
        //代理对象
        HttpHost proxy = new HttpHost("127.0.0.1", 8080, "http");
        //配置对象
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        //创建请求方法的实例， 并指定请求url
        HttpGet httpget=new HttpGet("http://localhost:8080/httpClientRequest");

        //有些服务器必须使用浏览器配置，这里配置浏览器的请求头信息
        httpget.setConfig(config);
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
        CloseableHttpResponse response=closeableHttpClient.execute(httpget);
        HttpEntity entity=response.getEntity();
        String content=EntityUtils.toString(entity, "utf-8");
        System.out.println(content);
        closeableHttpClient.close();

        //the code of the request

    }

    /**
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createCloseableHttpClient(){

        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }

            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }



}

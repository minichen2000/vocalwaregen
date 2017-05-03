package com.mfe.mfewordcard.vocalwaregen.JettyHttpClientService;

import com.mfe.mfewordcard.vocalwaregen.constants.ConfLoader;
import com.mfe.mfewordcard.vocalwaregen.constants.ConfigKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.util.HttpCookieStore;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.net.CookieStore;

public class HttpClientService
{

    private static Logger log = LogManager.getLogger( HttpClientService.class );

    private static HttpClientService inst_ = null;

    private HttpClient httpClient = null;

    private CookieStore cookieStore = new HttpCookieStore();

    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    public CookieStore getCookieStore()
    {
        return cookieStore;
    }

    void init()
    {

        SslContextFactory sf = new SslContextFactory(true);

        /*SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance( "TLS" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        X509TrustManager tm = new X509TrustManager()
        {
            @Override
            public void checkClientTrusted( X509Certificate[] chain,
                                            String authType ) throws CertificateException
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted( X509Certificate[] chain,
                                            String authType ) throws CertificateException
            {
                // TODO Auto-generated method stub

            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                // TODO Auto-generated method stub
                return null;
            }
        };
        try {
            sslContext.init( null, new TrustManager[] {tm}, null );
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        sf.setSslContext(sslContext);*/


        httpClient=new HttpClient(sf);
        httpClient.setFollowRedirects(false);
        httpClient.setCookieStore(cookieStore);
        //proxy
        String host = ConfLoader.getInstance().getConf(ConfigKey.proxy_host, null);
        int port = ConfLoader.getInstance().getInt(ConfigKey.proxy_port, 0);
        if(null!=host && !host.isEmpty() && 0!=port){
            ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
            HttpProxy proxy = new HttpProxy(host, port);
            proxyConfig.getProxies().add(proxy);
        }
        //////////////
        try {
            httpClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpClientService instance()
    {
        if( inst_ == null )
        {
            inst_ = new HttpClientService();
            inst_.init();
        }
        return inst_;
    }

    private HttpClientService()
    {
    }

}

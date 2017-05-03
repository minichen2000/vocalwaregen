package com.mfe.mfewordcard.vocalwaregen.JettyHttpClientService;

import com.mfe.mfewordcard.vocalwaregen.Utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class HttpCall
{
    private static Logger log = LogManager.getLogger( HttpCall.class );

    private HttpCall()
    {

    }

    private static HttpCall inst = new HttpCall();

    public static HttpCall instance()
    {
        return inst;
    }

    private ContentResponse sendRequest(Request req) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("req: "+req.toString());
        log.info("req.getHost: "+req.getHost());
        log.info("req.getPort: "+req.getPort());
        log.info("req.getPath: "+req.getPath());
        log.info("req.getHeaders: "+req.getHeaders().toString());
        ContentResponse resp=req.send();

        log.info("Resp: Http code: "+resp.getStatus()+"\nReason: "+resp.getReason()+"\nContent:\n"+resp.getContentAsString());
        log.info("Resp: Headers: \n"+resp.getHeaders());
        return resp;
    }

    private String sendRequestReturnString(Request req){
        try
        {
            ContentResponse resp=sendRequest(req);

            if( resp.getStatus() != 201
                    && resp.getStatus() != 200 )
            {
                String rlt="Http code: "+resp.getStatus()+"\nReason: "+resp.getReason()+"\nContent:\n"+resp.getContentAsString();
                return rlt;
            }else{
                return resp.getContentAsString();
            }
        }
        catch( Exception e )
        {
            log.error( e );
            return e.toString();
        }
    }
    private boolean sendRequestReturnFile(Request req, String destFile){
        try
        {
            ContentResponse resp=sendRequest(req);

            if( resp.getStatus() != 201
                    && resp.getStatus() != 200 )
            {
                return false;
            }else{
                byte[] buf=resp.getContent();
                if(null==buf){
                    return false;
                }else{
                    log.info("destFile: "+destFile);
                    return FileUtils.saveBytes(buf, destFile);
                }
            }
        }
        catch( Exception e )
        {
            log.error( e );
            return false;
        }
    }

    public synchronized String getCall( String url, String contentType )
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.GET);

        if( contentType != null && !contentType.isEmpty() )
            req.header( "Accept", contentType );

        return sendRequestReturnString(req);
    }

    public synchronized String getCall( String url, String contentType, List<KeyValuePair> params, boolean getUrlOnly)
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.GET);

        if( contentType != null && !contentType.isEmpty() )
            req.header( "Accept", contentType );

        if(null!=params){
            for(KeyValuePair kv : params){
                req.param(kv.getKey(), kv.getValue());
            }
        }
        if(getUrlOnly){
            return req.getURI().toString();
        }else{
            return sendRequestReturnString(req);
        }
    }
    public synchronized boolean getAsFile( String url, String contentType, List<KeyValuePair> params, String destFile)
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.GET);

        if( contentType != null && !contentType.isEmpty() )
            req.header( "Accept", contentType );

        if(null!=params){
            for(KeyValuePair kv : params){
                req.param(kv.getKey(), kv.getValue());
            }
        }
        return sendRequestReturnFile(req, destFile);
    }

    public synchronized String postCall( String url, String inputParam, String contentType, String acceptContentType)
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.POST);
        if(null!=inputParam && !inputParam.isEmpty()){
            req.content(new StringContentProvider(inputParam), contentType);
        }
        if(null!=acceptContentType && acceptContentType.length()>0){
            req.header("Accept", acceptContentType);
        }
        req.header("Content-Type", contentType);
        return sendRequestReturnString(req);

    }

    public synchronized String putCall( String url, String inputParam, String contentType, String acceptContentType)
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.PUT);
        if(null!=inputParam && !inputParam.isEmpty()){
            req.content(new StringContentProvider(inputParam), contentType);
        }
        if(null!=acceptContentType && acceptContentType.length()>0){
            req.header("Accept", acceptContentType);
        }
        log.info("PUT content:\n"+inputParam);


        return sendRequestReturnString(req);

    }


    public synchronized String deleteCall( String url)
    {
        Request req=HttpClientService.instance().getHttpClient().newRequest(url).method(HttpMethod.DELETE);
        return sendRequestReturnString(req);

    }
}

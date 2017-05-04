package com.mfe.mfewordcard.vocalwaregen.servlet;

import com.mfe.mfewordcard.vocalwaregen.JettyHttpClientService.HttpCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpFields;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by minichen on 2017/5/4.
 */
public class OpUtils {
    private static Logger log= LogManager.getLogger(OpUtils.class);
    static public void forwardResp(HttpServletResponse resp, String method, String url, String data, String contentType, String acceptContentType) throws IOException {
        try {
            ContentResponse cr=HttpCall.instance().forwardRequest(method,url,data,contentType, acceptContentType);
            if(null==cr){
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ContentResponse is null.");
            }else{
                resp.setStatus(cr.getStatus());
                copyHeaders(cr, resp);

                byte[] content=cr.getContent();
                log.info("CR Status: "+cr.getStatus());
                log.info("CR Headers: "+cr.getHeaders().toString());
                log.info("CR MediaType: "+cr.getMediaType());
                log.info("CR Encoding: "+cr.getEncoding());
                log.info("CR Content byte length: "+content.length);

                OutputStream outStream = resp.getOutputStream();
                outStream.write(content, 0, content.length);
                outStream.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (TimeoutException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, e.getMessage());
        }
    }
    static private void copyHeaders(ContentResponse src, HttpServletResponse dest){
        HttpFields headers = src.getHeaders();
        for(String name : headers.getFieldNamesCollection()){
            dest.setHeader(name, headers.get(name));
        }
    }
}

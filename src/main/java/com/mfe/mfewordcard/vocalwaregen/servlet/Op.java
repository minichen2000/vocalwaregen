package com.mfe.mfewordcard.vocalwaregen.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class Op extends HttpServlet
{
	private static Logger log = LogManager.getLogger( Op.class );
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
		resp.setHeader("Cache-Control","no-cache");

		req.setCharacterEncoding("utf-8");
		BufferedReader reader = req.getReader();
		String line;
		StringBuilder sb=new StringBuilder();
		while ((line = reader.readLine()) != null)
			sb.append(line);
		String opjson=sb.toString();

		if(null!=opjson && !opjson.isEmpty()){
			OpModel opmodel=(new ObjectMapper()).readValue(opjson, OpModel.class);
			if(null!=opmodel && null!=opmodel.url && null!=opmodel.method){
				String url=opmodel.url;
				String method=opmodel.method;
				String contentType=opmodel.contentType;
				String acceptContentType=opmodel.acceptContentType;
				String payloadData=opmodel.data;
				log.info( "Op: [" + method+"]: "+url );
				log.info( "Op: contentType: " + contentType);
				log.info( "Op: acceptContentType: " + acceptContentType);
				log.info( "Op: payloadData: \n" + payloadData);

				OpUtils.forwardResp(resp, method, url, payloadData, contentType, acceptContentType);
				return;
			}
		}
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "opjson is not correct.");
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
    
    
}
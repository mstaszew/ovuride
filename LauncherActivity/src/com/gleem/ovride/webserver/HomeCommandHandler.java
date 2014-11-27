package com.gleem.ovride.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public class HomeCommandHandler implements HttpRequestHandler {
	private String htmlReply;
	public HomeCommandHandler(String htmlReply) {
		this.htmlReply = htmlReply;
	}
	
	@Override
	public void handle(HttpRequest arg0, HttpResponse response, HttpContext arg2)
			throws HttpException, IOException {
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");

				writer.write(htmlReply);
				writer.flush();
			}
		});
		response.setHeader("Content-Type", "text/html");
		response.setEntity(entity);
	}

}

package tip.apoteket.soapconnector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import tip.apoteket.message.Message;

public class SOAPConnector {

	private HttpClient httpClient;
	private String endPoint;
	private String soapAction;
	
    public SOAPConnector(String endPoint, String soapAction) {
    	this.httpClient = new HttpClient();
        this.endPoint = endPoint;
        this.soapAction = soapAction;
    }
    
    public String post(Message message) throws Exception{
    	if (this.endPoint == null || this.soapAction == null){
    		throw new Exception("Cannot find the endpoint : "+ this.endPoint +" or soapAciton :"+this.soapAction+".Please correct the webservice definition in configuration!");
    	}
    	String ecid = "";
    	
    	// Prepare HTTP post
        PostMethod post = new PostMethod(endPoint);
    	
	    RequestEntity entity = new StringRequestEntity(message.getSOAPMessage(), "text/xml", "UTF-8");
	    post.setRequestEntity(entity);
	    post.setRequestHeader("SOAPAction", soapAction);
	    
	    try {
            int result = httpClient.executeMethod(post); 
            // Display status code
            System.out.println("Response status code: " + result);
            ecid = post.getResponseHeader("X-ORACLE-DMS-ECID").getValue();
            
	    }
        finally {
            // Release current connection to the connection pool once you are done
            post.releaseConnection();
        }
	    
		return ecid;
    	
    }
    

}

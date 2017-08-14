/**
 * 
 */
package tip.apoteket.consumers.aq;

import java.sql.Connection;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jms.AQjmsSession;
import oracle.jms.AQjmsTextMessage;
import oracle.jms.AQjmsTopicConnectionFactory;
import tip.apoteket.soapconnector.SOAPConnector;
import tip.apoteket.util.Configuration;
import tip.apoteket.util.DataSource;

/**
 * @author Amit Sengupta
 *
 */
public class AQConsumer {

	String endPoint = "http://ela02soavh05.apoteket.se:7041/soa-infra/services/default/ItemRetailJMSConsumer/ItemRetailSOAPMsgConsumer";
	String soapAction="Consume_Message_pttBinding";
	
	public void dequeueMessage(OracleDataSource ds, String schemaname, String topicname, String sunscribername) throws JMSException, ClassNotFoundException, SQLException{
		
		Class.forName("oracle.AQ.AQOracleDriver");
		
		Connection aqconn = ds.getConnection();
		aqconn.setAutoCommit(false);
		
		TopicConnection tc_conn = null;
		TopicSession jms_sess = null;
		
		try{
			tc_conn =AQjmsTopicConnectionFactory.createTopicConnection(aqconn);
			jms_sess = tc_conn.createTopicSession(true, Session.SESSION_TRANSACTED);
	        tc_conn.start();
	        Topic queueTopic = ((AQjmsSession) jms_sess).getTopic(schemaname, topicname);
	        
	        TopicSubscriber subsApi =  (TopicSubscriber)((AQjmsSession) jms_sess).createDurableSubscriber(queueTopic, sunscribername);
	        
	        Message msg = subsApi.receive(10);
	        System.out.println("Subscriber has started consuming messages ...");
	        int counter =0;
	        
	        while(msg != null){
	        	counter++;
	        	
	        	System.out.println(((AQjmsTextMessage)msg).getText());
	        	
	        	//-----POSting meesage to SOAPConnector-----
	        	System.out.println("Try and post the consumed message to the service endpoint...");
	    		SOAPConnector soac = new SOAPConnector(endPoint, soapAction);
	    		tip.apoteket.message.Message nmsg = new tip.apoteket.message.Message(((AQjmsTextMessage)msg).getText());
	    		
	    		try {
	    			String ecid = soac.post(nmsg);
	    			System.out.println("Post successful....(ecid): "+ecid);
	    		} catch (Exception e) {
	    			System.out.println("Post failed! Check the service endpoint...");
	    			e.printStackTrace();
	    		}	        	
	    		//-----POSting meesage to SOAPConnector ends----
	        	msg = subsApi.receive(10);
	        }
	        
	        System.out.println("Subscriber has stopped consuming messages...Queue is empty!");
	        System.out.println("Consumed and processed "+ counter +" messages");
	        aqconn.commit();
		}
		finally{
			tc_conn.close();
			jms_sess.close();
			aqconn.close();
		}
		return ;
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws JMSException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, JMSException, SQLException, Exception {
		// TODO Auto-generated method stub
		
		Configuration conf = new Configuration("dev", "items");
		
		AQConsumer aqc = new AQConsumer();
		aqc.dequeueMessage(DataSource.getDataStore("dev"), conf.getProperty("user"), conf.getProperty("queuename"), conf.getProperty("subscribername"));

	}

}

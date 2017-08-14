package tip.apoteket.message;

public class Message {
	private String payload;
	
	public Message() {}
	
	public Message(String payload) {
		this.payload = payload;
	}
	
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String getSOAPMessage() {
		String soapMessage = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header/><soapenv:Body>";
		soapMessage += payload;
		soapMessage += "</soapenv:Body></soapenv:Envelope>";
		return soapMessage;
	}
	
}

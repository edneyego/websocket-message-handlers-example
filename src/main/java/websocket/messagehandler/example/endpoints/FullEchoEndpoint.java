package ws_messages.endpoints;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class EchoEndpoint extends javax.websocket.Endpoint {

	private final static Logger log = Logger.getLogger(EchoEndpoint.class.getSimpleName());
	
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		final String sessionId = session.getId();
		log.info("established session with id: "+sessionId);
		
		//add text based message handler
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			
			@Override
			public void onMessage(String msg) {
				log.info(sessionId+": text message: "+msg);
			}
		});
		
		//add binary based message handler
		session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

			@Override
			public void onMessage(ByteBuffer buffer) {
				log.info(sessionId+": binary message: "+new String(buffer.array()));
			}
		});
	}

}

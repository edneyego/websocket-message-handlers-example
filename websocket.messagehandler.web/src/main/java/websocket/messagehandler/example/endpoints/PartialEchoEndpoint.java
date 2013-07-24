package websocket.messagehandler.example.endpoints;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class PartialEchoEndpoint extends javax.websocket.Endpoint {

	private final static Logger log = Logger
			.getLogger(PartialEchoEndpoint.class.getSimpleName());

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		final String sessionId = session.getId();
		log.info("established session with id: " + sessionId);

		// add text based message handler
		session.addMessageHandler(new MessageHandler.Partial<String>() {

			@Override
			public void onMessage(String msg, boolean last) {
				StringBuffer logMessage = new StringBuffer();
				logMessage.append(sessionId).append(": text message: ")
						.append(msg).append(" last: ").append(last);
				log.info(logMessage.toString());
			}
		});

		// add binary based message handler
		session.addMessageHandler(new MessageHandler.Partial<ByteBuffer>() {

			@Override
			public void onMessage(ByteBuffer buffer, boolean last) {
				StringBuffer logMessage = new StringBuffer();
				logMessage.append(sessionId).append(": binary message: ")
						.append(new String(buffer.array())).append(" last: ").append(last);
				log.info(logMessage.toString());
			}
		});

	}

}

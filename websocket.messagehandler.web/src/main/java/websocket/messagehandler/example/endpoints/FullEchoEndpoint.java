package websocket.messagehandler.example.endpoints;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;
import javax.websocket.Session;

public class FullEchoEndpoint extends javax.websocket.Endpoint {

	private final static Logger log = Logger.getLogger(FullEchoEndpoint.class
			.getSimpleName());

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		final String sessionId = session.getId();
		log.info("established session with id: " + sessionId);

		// add text based message handler
		session.addMessageHandler(new MessageHandler.Whole<String>() {

			@Override
			public void onMessage(String msg) {
				log.info(sessionId + ": text message: " + msg);
			}
		});

		// add binary based message handler
		session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

			@Override
			public void onMessage(ByteBuffer buffer) {
				log.info(sessionId + ": binary message: "
						+ new String(buffer.array()));
			}
		});

		session.addMessageHandler(new MessageHandler.Whole<PongMessage>() {

			@Override
			public void onMessage(PongMessage pongMessage) {
				StringBuffer pong = new StringBuffer();
				pong.append(sessionId)
						.append(": pong message: ")
						.append(new String(pongMessage.getApplicationData()
								.array()));
				log.info(pong.toString());

			}
		});

		String pingString = FullEchoEndpoint.class.getName() + " pings";
		ByteBuffer pingData = ByteBuffer.allocate(pingString.getBytes().length);
		pingData.put(pingString.getBytes()).flip();
		try {
			session.getBasicRemote().sendPing(pingData);
		} catch (IllegalArgumentException | IOException e) {
			log.severe("error in sending ping");
		}
	}
}

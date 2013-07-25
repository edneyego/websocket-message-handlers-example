package websocket.messagehandler.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import websocket.messagehandler.example.endpoint.Endpoint;

public class App {

	static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) throws DeploymentException,
			IOException, URISyntaxException, InterruptedException {

		WebSocketContainer container = ContainerProvider
				.getWebSocketContainer();
		Session session = container.connectToServer(Endpoint.class, new URI(
				"ws://localhost:8080/websocket-messages/partialEchoEndpoint"));
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(new String("Here is a message!").getBytes("UTF-8"));
		buffer.flip();
		session.getAsyncRemote().sendBinary(buffer, new SendHandler() {

			@Override
			public void onResult(SendResult result) {
				System.out.println("is send result ok: " + result.isOK());
				latch.countDown();
			}
		});

		latch.await(10, TimeUnit.SECONDS);
		session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,
				"sesison close"));

		session = container.connectToServer(Endpoint.class, new URI(
				"ws://localhost:8080/websocket-messages/fullEchoEndpoint"));

		Thread.sleep(1000);
		session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,
				"sesison close"));
	}
}

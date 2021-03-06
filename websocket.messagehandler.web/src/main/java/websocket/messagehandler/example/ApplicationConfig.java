package websocket.messagehandler.example;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import websocket.messagehandler.example.endpoints.FullEchoEndpoint;
import websocket.messagehandler.example.endpoints.PartialEchoEndpoint;

public class ApplicationConfig implements ServerApplicationConfig {

	private final static Logger log = Logger.getLogger(ApplicationConfig.class.getName());

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(
			Set<Class<?>> annotatedClasses) {
		return annotatedClasses;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(
			Set<Class<? extends Endpoint>> endpointClassesSet) {
		Set<ServerEndpointConfig> endpointConfigs = new HashSet<ServerEndpointConfig>(
				1);

		endpointConfigs.add(ServerEndpointConfig.Builder.create(
				FullEchoEndpoint.class, "/fullEchoEndpoint").build());
		endpointConfigs.add(ServerEndpointConfig.Builder.create(
				PartialEchoEndpoint.class, "/partialEchoEndpoint").build());

		return endpointConfigs;
	}

}

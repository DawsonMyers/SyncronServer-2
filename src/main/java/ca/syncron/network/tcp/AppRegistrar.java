package ca.syncron.network.tcp;

import ca.syncron.network.tcp.client.Client;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.network.tcp.server.Server;
import ca.syncron.network.tcp.server.ServerController;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/13/2015.
 */
public class AppRegistrar<T> {
	static              String nameId = AppRegistrar.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);


	public static AbstractTcpConnector mConnector;
	public static Server               mServer;
	public static Client               mClient;
	public static ServerController     mServerController;
	public static ClientController     mClientController;

	public static boolean serverControllerRegistered;
	public static boolean clientControllerRegistered;
	public static boolean serverRegistered;
	public static boolean clientRegistered;
	public static boolean connectorRegistered;


	public AppRegistrar() {}

	public static void register(Object obj) {
		String type = obj.getClass().getName();
		try {
			if (obj instanceof Server) mServer = ((Server) obj);
			if (obj instanceof ServerController) mServerController = ((ServerController) obj);
			if (obj instanceof Client) mClient = ((Client) obj);
			if (obj instanceof ClientController) mClientController = ((ClientController) obj);
			if (obj instanceof AbstractTcpConnector) mConnector = ((AbstractTcpConnector) obj);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not identify class to register");
		}
		log.debug("Instance of type " + type + " has registered");
	}

	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	//  Connectors
	///////////////////////////////////////////////////////
	public static void registerConnector(AbstractTcpConnector connector) {
		log.debug("AbstractTcpConnector instance registered");
		mConnector = connector;
		connectorRegistered = true;
	}

	public static void registerServer(Server server) {
		log.debug("Server instance registered");
		mServer = server;
		serverRegistered = true;
	}

	public static void registerClint(Client client) {
		log.debug("Client instance registered");
		mClient = client;
		clientRegistered = true;
	}

	public static Server getServer() {
		log.debug("Server instance retrieved");

		if (mServer != null) {
			return mServer;
		} else return mServer = new Server();
	}

	public static Client getClient() {
		log.debug("Client instance retrieved");
		if (mClient != null) {
			return mClient;
		} else return mClient = new Client(getClientController());
	}

	//  Controllers
	///////////////////////////////////////////////////////
	public static void registerServerController(ServerController serverController) {
		mServerController = serverController;
		serverControllerRegistered = true;
	}

	public static void registerClientController(ClientController clientController) {
		mClientController = clientController;
		clientControllerRegistered = true;
	}

	public static ServerController getServerController() {
		log.debug("Client instance retrieved");
		if (mServerController != null) {
			return mServerController;
		} else return mServerController = new ServerController();
	}

	public static ClientController getClientController() {
		if (mClient != null) {
			return mClientController;
		} else return mClientController = new ClientController();
	}

	public static AbstractTcpConnector getConnector() {
		if (mConnector != null) {
			return null;
		}
		return mConnector;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
	}
}

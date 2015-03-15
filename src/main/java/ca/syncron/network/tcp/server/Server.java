package ca.syncron.network.tcp.server;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AbstractTcpConnector;
import ca.syncron.network.tcp.AppRegistrar;
import com.jcabi.aspects.Loggable;
import naga.NIOSocket;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Server extends AbstractTcpConnector {
	static              String           nameId = ServerController.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	public static Server me;
	ServerController mController = ServerController.getInstance();

	public static String getNameId() {return nameId;}

	public static Server getInstance() {return me;}

	public Server() {
		me = this;
		isServer(true);
		AppRegistrar.register(this);
		//	initCallbacks();
		//registerCallbacks(callbacks);
	}


	public Server(boolean asServer) {
		super(asServer);
		initCallbacks();
	}

	@Override
	public void acceptFailed(IOException exception) {
		super.acceptFailed(exception);
	}

	@Override
	public void serverSocketDied(Exception exception) {
		super.serverSocketDied(exception);
	}

	@Loggable
	@Override
	public void newConnection(NIOSocket nioSocket) {
		super.newConnection(nioSocket);
	}

	@Override
	public void removeUser(User user) {
		super.removeUser(user);
	}

	private void initCallbacks() {}

	@Override
	public void sendMessage(Message msg) {
		msg.getUser().getSocket().write(msg.serializeMessage().getBytes());
//		for (User user : mUsers) {
//			if (user.getType() == Message.UserType.NODE) {
//				user.getSocket().write(msg.serializeMessage().getBytes());
//			}
//		}
	}

	public void sendNodeMessage(Message msg) {
		for (User user : mUsers) {
			if (user.getType() == Message.UserType.NODE) {
				log.info("ClientController - mArduino.setPin({},{})", msg.getPin(), msg.getValue());
				user.getSocket().write(msg.serializeMessage().getBytes());
			} else log.error("NO NODES CONNECTED - MSG NOT SENT");
		}
	}

	public void sendNodeServerMessage(Message msg) {

		for (User user : mUsers) {
			if (user.getType() == Message.UserType.NODE_SERVER) {
				log.info("Sending msg to NodeServer: {}, Target user=", msg.getUserId(), msg.getTargetId());
				user.getSocket().write(msg.serializeMessage().getBytes());
			} else log.error("NO NODES CONNECTED - MSG NOT SENT");
		}
	}

	@Override
	public void handleDigitalMessage(Message msg) {
		log.info("handleDigitalMessage");

		sendNodeMessage(msg);
	}

	@Override
	public void handleAnalogMessage(Message msg) { }

	@Override
	public void handleChatMessage(Message msg) {
		log.info("handleChatMessage");
		//if (msg.getSerialMessage().startsWith("{")) msg.setSerialMessage(msg.serializeMessage());
		msg.setSerialMessage(msg.serializeMessage());
		log.info("[CHAT] " + msg.getUser() + ": " + msg.getChatMessage());
		broadcast(msg.getUser(), msg.getSerialMessage());
	}


	@Override
	public void handleAdminMessage(Message msg) {}


	@Override
	public void handleUpdateMessage(Message msg) { }

	@Override
	public void handleRegisterMessage(Message msg) {
		log.info("handleRegisterMessage");
		try {
			User user = msg.getUser();
			user.register(msg);
			//getUserBundles().add(user.getUserBundle());
			log.info("User: " + msg.getUserName() + " has registered");
//			user.setType(msg.getUserType());
//			user.setName(msg.getUserId());
			invalidateStatus();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void handleStatusMessage(Message msg) { }

	@Override
	public void handleLoginMessage(Message msg) { }

	@Override
	public void handleUserMessage(Message msg) { }

	@Override
	public void handleErrorMessage(Message msg) {
		log.info("handleErrorMessage");
	}

	@Override
	public <T> void processMessage(T msg) { }

	@Override
	public void invalidateStatus() {
		super.invalidateStatus();
		Message msg = new Message();
		msg.status(msg);
		msg.setAnalogValues(mController.getAnalog());
		msg.setDigitalValues(mController.getDigital());
		msg.setUserBundles(mController.getUserBundles());
		broadcast(msg);
	}
}


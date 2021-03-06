package ca.syncron.network.tcp.server;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AbstractTcpConnector;
import ca.syncron.network.tcp.AppRegistrar;
import com.jcabi.aspects.Loggable;
import naga.NIOSocket;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

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
		if (msg.getTargetId() != null) sendToTarget(msg);
		if (msg.getSenderType() == Message.UserType.NODE) broadcast(msg);
		else sendNodeMessage(msg);
	}

	@Override
	public void handleAnalogMessage(Message msg) {sendNodeMessage(msg); }

	@Override
	public void handleChatMessage(Message msg) {
		if (msg.getTargetId() != null) sendToTarget(msg);
		log.info("handleChatMessage");
		//if (msg.getSerialMessage().startsWith("{")) msg.setSerialMessage(msg.serializeMessage());
		msg.setSerialMessage(msg.serializeMessage());
		log.info("[CHAT] " + msg.getUser() + ": " + msg.getChatMessage());
		broadcast(msg.getUser(), msg.getSerialMessage());
	}


	@Override
	public void handleAdminMessage(Message msg) {}


	@Override
	public void handleUpdateMessage(Message msg) {
		if (msg.getTargetId() != null) sendToTarget(msg);
		if (msg != null) {
			int i = 0;
			if (msg.getAnalogValues() != null) {
				mController.setAnalogVals(msg.getAnalogValues());
				msg.getUser().setAnalogVals(msg.getAnalogValues());
				i++;
			}

			if (msg.getDigitalValues() != null) {
				mController.setDigitalVals(msg.getDigitalValues());
				msg.getUser().setDigitalVals(msg.getDigitalValues());
				i++;
			}
			if (i > 0) invalidateValues(msg);
			if (i > 0) msg.getUser().notifySubscribers();//invalidateValues(msg);
		}
	}

	private void invalidateValues(Message msg) {
		msg.initVals();
		msg.getUser().notifySubscribers();
	}

	@Override
	public void handleRegisterMessage(Message msg) {
		log.info("handleRegisterMessage");
		try {
			User user = msg.getUser();
			user.register(msg);
			if (!mUserMap.containsKey(user.getUserId())) mUserMap.put(user.getUserId(), user);
			getUserBundles().add(user.getUserBundle());
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
	public void handleStatusMessage(Message msg) {
		if (msg.getTargetId() != null) sendToTarget(msg);
//		if (msg != null) {
//			if (msg.getAnalogValues() != null) {
//				mController.setAnalogVals(msg.getAnalogValues());
//			}
//
//			if (msg.getDigitalValues() != null) {
//				mController.setDigitalVals(msg.getDigitalValues());
//			}
//		}
	}

	@Override
	public void handleLoginMessage(Message msg) { if (msg.getTargetId() != null) sendToTarget(msg);}

	@Override
	public void handleUserMessage(Message msg) { if (msg.getTargetId() != null) sendToTarget(msg);}

	@Override
	public void handleErrorMessage(Message msg) {
		log.info("handleErrorMessage");
	}

	@Override
	public void handleTargetMessage(Message msg) {
		super.handleTargetMessage(msg);
		//msg.setMessageType(msg.getTargetMsgTag());
		msg.setIsTargeted(false);
		if (mUserMap.containsKey(msg.getTargetId())) {
			User u = mUserMap.get(msg.getTargetId());
			msg.serializeMessage();
			u.sendMessage(msg.getSerialMessage());
		} else log.debug("Message target id not found");

	}

	@Override
	public void handleSubscribeMessage(Message msg) {
		super.handleSubscribeMessage(msg);
		msg.getUser().subscribe(msg.getTargetId(), msg.getDoSubsribe());
	}

	@Override
	public void handleQueryMessage(Message msg) {
		super.handleQueryMessage(msg);
	}

	@Override
	public void handleAccessMessage(Message msg) {
		super.handleAccessMessage(msg);
	}

	@Override
	public void sendSubscribeMessage(Message msg) {
		super.sendSubscribeMessage(msg);
	}

	@Override
	public void sendQueryMessage(Message msg) {
		super.sendQueryMessage(msg);
	}

	@Override
	public void sendErrorMessage(Message msg) {
		super.sendErrorMessage(msg);
	}

	@Override
	public void sendAccessMessage(Message msg) {
		super.sendAccessMessage(msg);
	}

	@Override
	public <T> void processMessage(T msg) { }

	@Override
	public void sendUpdateMessage(Message msg) {
		super.sendUpdateMessage(msg);
		if (msg == null) {
			msg = new Message().newUpdate(this.mController);
			broadcastUsers(msg);
			return;
		}

		broadcastUsers(msg);
	}


	@Override
	public void invalidateStatus() {
		super.invalidateStatus();
		Message msg = new Message();
		msg.status(msg);
//		msg.setAnalogValues(mController.getAnalog());
//		msg.setDigitalValues(mController.getDigital());
		msg.setUserBundles(mController.getUserBundles());
		broadcast(msg);
	}

	public void invalidateValues() {
		log.info("Analog = " + Arrays.toString(mController.getAnalog()));
		log.info("Analog = " + Arrays.toString(mController.getDigital()));
		Message msg = new Message();
		msg.update(mController.getDigital(), mController.getAnalog());

		broadcast(msg);
	}

	@Override
	public void sendStreamMessage(Message msg) {
		super.sendStreamMessage(msg);
		sendNodeMessage(msg);
	}

	@Override
	public void handleStreamMessage(Message msg) {
		super.handleStreamMessage(msg);
		if (msg.getUserType() == Message.UserType.NODE) sendStreamMessage(msg);
		else broadcast(msg);
	}
}


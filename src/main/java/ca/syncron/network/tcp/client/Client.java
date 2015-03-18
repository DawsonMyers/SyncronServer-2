package ca.syncron.network.tcp.client;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AbstractTcpConnector;
import ca.syncron.network.tcp.AppRegistrar;
import naga.NIOSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ca.syncron.network.message.Message.UserType.NODE;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Client extends AbstractTcpConnector {
	static              String nameId = Client.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static ClientController mController;
	public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

	public Client() { }

	public Client(ClientController controller) {
		this();
		AppRegistrar.register(this);
		mController = controller;
		isServer(false);
		//registerCallbacks(callbacks);
	}


	@Override
	public void connectionOpened(NIOSocket nioSocket) {
		super.connectionOpened(nioSocket);
	}

	@Override
	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		super.connectionBroken(nioSocket, exception);
	}

	@Override
	public void packetReceived(NIOSocket socket, byte[] packet) {
		super.packetReceived(socket, packet);
	}

	@Override
	public void packetSent(NIOSocket socket, Object tag) {
		super.packetSent(socket, tag);
	}


	@Override
	public void handleDigitalMessage(Message msg) {
		super.handleDigitalMessage(msg);
		log.info("handleDigitalMessage");
		log.error("setPin(" + msg.getPin() + ", " + msg.getValue());
		mController.setPin(msg.getPinAsInt(), msg.getValueAsInt());
	}

	@Override
	public void handleAnalogMessage(Message msg) {
		super.handleAnalogMessage(msg);
	}

	@Override
	public void handleChatMessage(Message msg) {

		super.handleChatMessage(msg);
	}

	@Override
	public void handleAdminMessage(Message msg) {
		super.handleAdminMessage(msg);
	}

	@Override
	public void handleUpdateMessage(Message msg) {
		super.handleUpdateMessage(msg);
	}

	@Override
	public void handleRegisterMessage(Message msg) {
		super.handleRegisterMessage(msg);
		msg.register(false);
		msg.setUserName(mController.getUserName());
		msg.setUserType(mController.getUserType());
		mHandler.addToSendQueue(msg);
	}

	@Override
	public void handleStatusMessage(Message msg) {
		super.handleStatusMessage(msg);
		mController.updateStatus(msg);

	}

	@Override
	public void handleLoginMessage(Message msg) {
		super.handleLoginMessage(msg);
	}

	@Override
	public void handleUserMessage(Message msg) {
		super.handleUserMessage(msg);
	}

	@Override
	public void handleErrorMessage(Message msg) {
		super.handleErrorMessage(msg);
	}

	//  Sending
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void sendMessage(Message msg) {
		super.sendMessage(msg);

		if (mSocket != null) {
			if (msg.getSerialMessage() == null) {
				msg.serializeMessage();
				log.debug("sendMessage:: serializing msg to send");
				//return;
			}
			mSocket.write(msg.getSerialMessage().getBytes());
			log.info("Message sent");
		}
	}

	@Override
	public void sendUpdateMessage(Message msg) {
		if (msg == null) {
			msg = new Message().newUpdate(this.mController);
//			msg = new Message(NODE, ANDROID);
//			msg.update(mController.getDigital(), mController.getAnalog());
		}
		sendMessage(msg);
	}

	@Override
	public void sendChatMessage(Message msg) {

	}

	@Override
	public void sendSystemMessage(Message msg) {

	}

	@Override
	public void sendRegisterMessage(Message msg) {

	}

	@Override
	public void sendStreamMessage(Message msg) {
		super.sendStreamMessage(msg);
		if (msg != null) {
			sendMessage(msg);
			return;
		}
		msg = new Message(Message.MessageType.STREAM, NODE);
		msg.setAnalogValues(mController.getAnalog());
		sendMessage(msg);
	}

	public void sendAnalogMessage(Message msg) {

		if (msg != null) {
			sendMessage(msg);
			return;
		}
		msg = new Message(Message.MessageType.STREAM, NODE);
		msg.setAnalogValues(mController.getAnalog());
		sendMessage(msg);
	}

	@Override
	public void handleStreamMessage(Message msg) {
		super.handleStreamMessage(msg);
		mController.setSampleRate(msg.getSampleRate());
		mController.setStreamEnabled(msg.getStreamEnabled());
		if (mController.getSreamEnabled()) {
			startStream();
		} else scheduler.shutdown();
	}

	private void startStream() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				sendUpdateMessage(null);
			}
		};

		long rate = mController.getSampleRate();
		scheduler.schedule(r, rate, TimeUnit.MILLISECONDS);

	}

	public static String getNameId() {return nameId;}
}

package ca.syncron.network.tcp.client;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AbstractTcpConnector;
import naga.NIOSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.syncron.network.message.Message.UserType.ANDROID;
import static ca.syncron.network.message.Message.UserType.NODE;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Client extends AbstractTcpConnector {
	static              String nameId = Client.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static ClientController mController;

	public Client(ClientController controller) {
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
	}

	@Override
	public void handleStatusMessage(Message msg) {
		super.handleStatusMessage(msg);
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
		log.info("handleSendMessage");
		if (mSocket != null) {
			if (msg.getSerialMessage() == null) {
				log.error("sendMessage:: serial message empty");
				return;
			}
			mSocket.write(msg.getSerialMessage().getBytes());
			log.info("Message sent");
		}
	}

	@Override
	public void sendUpdateMessage(Message msg) {
		if (msg == null) {
			msg = new Message(NODE, ANDROID);
			msg.update(mController.getDigital(), mController.getAnalog());
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
	public void sendRegiterMessage(Message msg) {

	}

}

package ca.syncron.network.tcp;

import ca.syncron.network.message.Handler;
import ca.syncron.network.message.Message;
import ca.syncron.network.message.MessageCallbacks;
import ca.syncron.network.tcp.server.User;
import ca.syncron.utils.ComConstants;
import ca.syncron.utils.Constants;
import ca.syncron.utils.Interfaces;
import naga.*;
import naga.eventmachine.EventMachine;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.syncron.utils.Constants.Ports;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/7/2015.
 */
//public abstract class AbstractTcpConnector {
//
//	public AbstractTcpConnector
//
//	{}
//
//}


public class AbstractTcpConnector extends Thread implements ServerSocketObserver, ComConstants,
                                                            SocketObserver, MessageCallbacks.DispatchCallbacks,
                                                            Interfaces.PinStatus {
	static String nameId = AbstractTcpConnector.class.getName();
	String id = getClass().getSimpleName();
	public MessageCallbacks.DispatchCallbacks callbacks;

	public static String userName;
	public final static Logger log = LoggerFactory.getLogger(nameId);
	public static Handler mHandler;
	public static volatile Map<String, User>    connectedClients = new HashMap<String, User>();    // new
	private static         AbstractTcpConnector mInstance        = null;
	public EventMachine mEventMachine;
	public List<User>   mUsers;
	private boolean mIsServer = false;
	public NIOSocket mSocket;
	public boolean   mConnected;


	//
	// ///////////////////////////////////////////////////////////////////////////////////
	public AbstractTcpConnector() {
		mInstance = this;
		//mEventMachine = machine;
		mUsers = new ArrayList<User>();
		mHandler = new Handler(this);
		mHandler.start();
	}

	public AbstractTcpConnector(boolean asServer) {
		this();
		isServer(asServer);
	}

	AbstractTcpConnector(EventMachine machine) {

	}

	public void isServer(boolean b) {
		mIsServer = b;
	}

	public boolean isServer() {
		return mIsServer;
	}

	@Override
	public void run() {
		connect();
	}

	public void connect() {
		int port = Ports.getTcp();
		//	isServer(true);
		if (isServer()) {

			try {   // Server
				log.info("Starting Server");
				EventMachine machine = new EventMachine();
				mEventMachine = machine;
				NIOServerSocket socket = machine.getNIOService().openServerSocket(port);
				socket.listen(this);
				//mSocket.listen(new Server(machine));
				socket.setConnectionAcceptor(ConnectionAcceptor.ALLOW);
				machine.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {    //  Client
			String ip = Constants.IpAddresses.IP;
			try {
				//log.d(id, "Run()");
				log.info("Starting Client");
				EventMachine machine = new EventMachine();

				InetAddress ipAddress = InetAddress.getByName(ip);
				mSocket = machine.getNIOService().openSocket(ipAddress, port);
				mSocket.listen(mInstance);
				mSocket.setPacketReader(new AsciiLinePacketReader());
				mSocket.setPacketWriter(new AsciiLinePacketWriter());
				machine.start();
				if (mSocket.isOpen()) mConnected = true;
				//if(isScheduled && mSocket.isOpen()) scheduler.shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	//
	// ///////////////////////////////////////////////////////////////////////////////////

	public static AbstractTcpConnector getInstance() {
		if (mInstance == null) {
			// mInstance = new ServerTcp();
		}
		return mInstance;

	}

	public void acceptFailed(IOException exception) {
		log.info("Failed to accept connection: " + exception);
	}

	public void serverSocketDied(Exception exception) {
		// If the server mSocket dies, we could possibly try to open a new
		// mSocket.
		log.info("Server mSocket died.");
		System.exit(-1);
	}

	public void newConnection(NIOSocket nioSocket) {
		// Create a new user to hande the new connection.
		log.info("New user connected from " + nioSocket.getIp() + ".");
		mUsers.add(new User(this, nioSocket));
	}

	public void removeUser(User user) {
		log.info("Removing user " + user + ".");
		mUsers.remove(user);
	}

	public void broadcast(User sender, String string) {
		// We convert the packet, then send it to all users except the sender.
		byte[] bytesToSend = string.getBytes();
		for (User user : mUsers) {
			if (user != sender) user.sendBroadcast(bytesToSend);
		}
	}
	public void broadcast(Message msg) {
		// We convert the packet, then send it to all users except the sender.
		byte[] bytesToSend = msg.serializeMessage().getBytes();
		User sender = msg.getUser();
		for (User user : mUsers) {
			if (user != sender) user.sendBroadcast(bytesToSend);
		}
	}

	public EventMachine getEventMachine() {
		return mEventMachine;
	}

	public void sendToTarget(String targetId, String msgString) {
		if (connectedClients.containsKey(targetId)) {
			User target = connectedClients.get(targetId);
			target.getSocket().write(msgString.getBytes());
		}
	}

	public static User userLookup(String keyFragment) {
//		for (String key : connectedClients.keySet()) {
		for (String key : connectedClients.keySet()) {
			if (key.contains(keyFragment)) return connectedClients.get(key);
		}
		return null;
	}

	//  Client Callbacks
	// ///////////////////////////////////////////////////////////////////////////////////
	@Override
	public void connectionOpened(NIOSocket nioSocket) {
		mSocket = nioSocket;
		isConnected(true);
	}

	private void isConnected(boolean b) {
		mConnected = b;
	}

	private boolean isConnected() {
		return mConnected;
	}

	@Override
	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		log.info("Disconnected");

	}

	@Override
	public void packetReceived(NIOSocket socket, byte[] packet) {
		log.info("Packet received");

		mHandler.addToReceiveQueue(new String(packet));

	}

	public void packetReceived(User user, byte[] packet) {
		log.info("Packet received");

		Message msg = new Message();
		msg.setSerialMessage(new String(packet));
		msg = msg.deserializeMessage(msg.getSerialMessage());
		msg.setUser(user);
		mHandler.addToReceiveQueue(msg);
		//mHandler.addToReceiveQueue(new String(packet));

	}

	@Override
	public void packetSent(NIOSocket socket, Object tag) {
		log.info("Packet sent");
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		AbstractTcpConnector.userName = userName;
	}

	public void registerCallbacks(MessageCallbacks.DispatchCallbacks callbacks) {mHandler.register(callbacks);}

	@Override
	public void handleDigitalMessage(Message msg) {
		log.info("handleDigitalMessage");
	}

	@Override
	public void handleAnalogMessage(Message msg) {
		log.info(" handleAnalogMessage");
	}

	@Override
	public void handleChatMessage(Message msg) {
		log.info("Chat message handled");
		log.info("handleChatMessage");
	}

	@Override
	public void handleAdminMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void handleUpdateMessage(Message msg) {
		log.info("handleUpdateMessage");
	}

	@Override
	public void handleRegisterMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void handleStatusMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void handleLoginMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void handleUserMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void handleErrorMessage(Message msg) {
		log.info("handleErrorMessage");
	}

	@Override
	public void sendMessage(Message msg) {
		log.info("handleSendMessage");
//		if (mSocket != null) {
//			mSocket.write(msg.getSerialMessage().getBytes());
//			log.info("Message sent");
//		}
	}

	@Override
	public void sendUpdateMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void sendChatMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void sendSystemMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public void sendRegiterMessage(Message msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public <T> void processMessage(T msg) {
		log.info("handleAdminMessage");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
	}


	@Override
	public void invalidateAnalog() {
		log.info("handleAdminMessage");
	}

	@Override
	public void invalidateDigital() {
		log.info("handleAdminMessage");
	}
}

package ca.syncron.network.tcp;

import ca.syncron.controller.AbstractController;
import ca.syncron.network.message.Handler;
import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.server.User;
import ca.syncron.network.tcp.server.UserBundle;
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

import static ca.syncron.network.message.MessageCallbacks.DispatchCallbacks;
import static ca.syncron.utils.Constants.Ports;
import static java.lang.System.out;
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
                                                            SocketObserver, DispatchCallbacks,
                                                            Interfaces.PinStatus {
	static String nameId = AbstractTcpConnector.class.getName();
	String id = getClass().getSimpleName();
	public DispatchCallbacks callbacks;

	public static String userName;
	public final static Logger log = LoggerFactory.getLogger(nameId);
	public static Handler mHandler;
	public static volatile Map<String, User>    mUserMap    = new HashMap<String, User>();    // new
	private static         AbstractTcpConnector me          = null;
	private static         AbstractController   mController = AbstractController.getInstance();
	public EventMachine mEventMachine;
	public List<User>   mUsers;
	private boolean mIsServer = false;
	public NIOSocket mSocket;
	public boolean   mConnected;

	public ArrayList<UserBundle> getUserBundles() {
		return mUserBundles;
	}

	public void setUserBundles(ArrayList<UserBundle> userBundles) {
		mUserBundles = userBundles;
	}

	public ArrayList<UserBundle> mUserBundles = new ArrayList<>();
	//public Map<String, User.UserBundle> mUserBundles = new HashMap<>();

	//
	// ///////////////////////////////////////////////////////////////////////////////////
	public AbstractTcpConnector() {
		me = this;
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
				mSocket.listen(me);
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

	public static AbstractTcpConnector getMe() {
		if (me == null) {
			// me = new ServerTcp();
		}
		return me;

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
		User user = new User(this, nioSocket);
		mUserMap.putIfAbsent(user.getUserId(), user);
		mUsers.add(user);
		mController.getUserBundles().add(user.getUserBundle());
		//mUsers.add(user);

	}

	public void removeUser(User user) {
		log.info("Removing user " + user + ".");
		mUsers.remove(user);
		mUserMap.remove(user.getUserId());
		mController.getUserBundles().remove(user.getUserBundle());
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
		User sender = msg.getMessageType() == Message.MessageType.STATUS ? new User() : msg.getUser();

		for (User user : mUsers) {
			if (user != sender) user.sendBroadcast(bytesToSend);
		}
	}

	public EventMachine getEventMachine() {
		return mEventMachine;
	}

	public void sendToTarget(String targetId, String msgString) {
		if (mUserMap.containsKey(targetId)) {
			User target = mUserMap.get(targetId);
			target.getSocket().write(msgString.getBytes());
		}
	}

	public static User userLookup(String keyFragment) {
//		for (String key : mUserMap.keySet()) {
		for (String key : mUserMap.keySet()) {
			if (key.contains(keyFragment)) return mUserMap.get(key);
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
		out.println("");
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

	public void registerCallbacks(DispatchCallbacks callbacks) {mHandler.register(callbacks);}

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
		log.info("handleRegisterMessage");
	}

	@Override
	public void handleStatusMessage(Message msg) {
		log.info("handleStatusMessage");
	}

	@Override
	public void handleLoginMessage(Message msg) {
		log.info("handleLoginMessage");
	}

	@Override
	public void handleUserMessage(Message msg) {
		log.info("handleUserMessage");
	}

	@Override
	public void handleErrorMessage(Message msg) {
		log.info("handleErrorMessage");
	}

	@Override
	public void sendMessage(Message msg) {
		log.info("sendMessage");
//		if (mSocket != null) {
//			mSocket.write(msg.getSerialMessage().getBytes());
//			log.info("Message sent");
//		}
	}

	@Override
	public void sendUpdateMessage(Message msg) {
		log.info("handleUpdateMessage");
	}

	@Override
	public void sendChatMessage(Message msg) {
		log.info("handleChatMessage");
	}

	@Override
	public void sendSystemMessage(Message msg) {
		log.info("handleSystemMessage");
	}

	@Override
	public void sendRegisterMessage(Message msg) {
		log.info("handleRegisterMessage");
	}

	@Override
	public <T> void processMessage(T msg) {
		log.info("processMessage");
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
	}


	@Override
	public void invalidateAnalog() {
		log.info(" invalidateAnalog");
	}

	@Override
	public void invalidateDigital() {
		log.info("invalidateDigital");
	}

	//
	///////////////////////////////////////////////////////
	public void invalidateStatus() {
		log.info("invalidateStatus");
	}
}

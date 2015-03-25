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

	//	public Metrics.Counter receiveCounter = new Metrics.Counter();
//	public Metrics.Counter sendCounter    = new Metrics.Counter();
	public static String userName;
	public final static Logger log = LoggerFactory.getLogger(nameId);
	public static Handler mHandler;
	public static volatile Map<String, User>    mUserMap    = new HashMap<String, User>();    // new
	private static         AbstractTcpConnector me          = null;
	private static         AbstractController   mController = AbstractController.getInstance();
	public EventMachine mEventMachine;
	NIOServerSocket mServerSocket;
	public List<User> mUsers;
	private boolean mIsServer = false;
	public NIOSocket mSocket;
	public boolean   mConnected;


	public boolean isReconnecting() {
		return reconnecting;
	}

	public void setReconnecting(boolean reconnecting) {
		this.reconnecting = reconnecting;
	}

	private boolean reconnecting;

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
		AppRegistrar.register(this);
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
		if (!isConnected()) connect();
	}

	public void connect() {
		int port = Ports.getTcp();
		//	isServer(true);

		if (isServer()) {

			try {   // Server
				if (!isReconnecting()) log.info("Starting Server");
				else log.info("Reconnecting to Server");
				EventMachine machine = new EventMachine();
				mEventMachine = machine;
				NIOServerSocket socket = machine.getNIOService().openServerSocket(port);
				mServerSocket = socket;
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
				if (!isReconnecting()) log.info("Starting Client");
				else log.info("Reconnecting to Server");
				//log.info("Starting Client");
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


	public synchronized void reconnect() {
		reconnecting = true;
		int count = 0;
		int index = 0;
		int port = Ports.getTcp();
		//	isServer(true);
		if (isServer()) {

			// Server
//			if (mEventMachine != null) {
//				mEventMachine.stop();
//			} else try {
//				mEventMachine = new EventMachine();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//

			try {
				Thread.sleep(1000);

				log.info("Attempting to connect a new server instance");
				while (!isConnected()) {
					count++;
					index++;
					connect();
					if (index >= 5) {
						log.info("Connection attempts: " + count);
						index = 0;
					}
					Thread.sleep(5000);
					if (mServerSocket.isOpen()) isConnected(true); mConnected = true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
			}

		} else {    //  Client
			String ip = Constants.IpAddresses.IP;
			try {
				EventMachine machine = mEventMachine;
				InetAddress ipAddress = InetAddress.getByName(ip);
//

				log.info("Attempting to reconnect to server");
				while (!isConnected()) {
					count++;
					if (mSocket.isOpen()) break;

					try {
//
						connect();
						//log.error("Connection attempts: " + count);

						//if (count % 3 == 0) log.error("Connection attempts: " + count);

						index++;
						connect();
						if (index >= 5) {
							log.info("Connection attempts: " + count);
							index = 0;
						}
						Thread.sleep(5000);

						if (mSocket.isOpen()) mConnected = true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					} //catch (UnknownHostException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
				}
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
		//	if (!mUserMap.containsKey(user.getUserId())) mUserMap.put(user.getUserId(), user);
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

	public void broadcastUsers(Message msg) {
		byte[] bytesToSend = msg.serializeMessage().getBytes();
		for (User user : mUsers) {
			if (user.getType() != Message.UserType.NODE) user.sendBroadcast(bytesToSend);
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

	public void sendToTarget(Message msg) {
		if (mUserMap.containsKey(msg.getTargetId())) {
			User target = mUserMap.get(msg.getTargetId());
			byte[] bytesToSend = msg.serializeMessage().getBytes();
			target.getSocket().write(bytesToSend);
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
		setReconnecting(false);
	}

	private void isConnected(boolean b) {
		mConnected = b;
	}

	private boolean isConnected() {
		return mConnected;
	}

	@Override
	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		isConnected(false);
		if (isReconnecting()) return;
		log.error("Disconnected");
		reconnect();

	}

	@Override
	public void packetReceived(NIOSocket socket, byte[] packet) {
		out.println("");
		log.info("Packet received");
		mHandler.addToReceiveQueue(new String(packet));

	}

	public void packetReceived(User user, byte[] packet) {
		log.info("Packet received from: " + user.getUserId());

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
	public void handleStreamMessage(Message msg) {

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
	public void handleSubscribeMessage(Message msg) {

	}

	@Override
	public void handleQueryMessage(Message msg) {

	}

	@Override
	public void handleUnknownMessage(Message msg) {

	}

	@Override
	public void handleAccessMessage(Message msg) {

	}

	@Override
	public void handleTargetMessage(Message msg) {

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
	public void sendStreamMessage(Message msg) {

	}

	@Override
	public void sendDigitalMessage(int pin, int value) {

	}

	@Override
	public void sendDigitalMessage(String pin, String value) {

	}

	@Override
	public void sendAnalogMessage(Message msg) {

	}

	@Override
	public <T> void processMessage(T msg) {
		log.info("processMessage");
	}

	@Override
	public void sendSubscribeMessage(Message msg) {

	}

	@Override
	public void sendUserMessage(Message msg) {

	}

	@Override
	public void sendErrorMessage(Message msg) {

	}

	@Override
	public void sendQueryMessage(Message msg) {

	}

	@Override
	public void sendUnknownMessage(Message msg) {

	}

	@Override
	public void sendAccessMessage(Message msg) {

	}

	@Override
	public void sendTargetMessage(Message msg) {

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

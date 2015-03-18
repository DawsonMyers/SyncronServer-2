/**
 *
 */
package ca.syncron.network.tcp.server;


import ca.syncron.network.message.Message;
import ca.syncron.network.message.Message.UserType;
import ca.syncron.network.message.MessageProcessor;
import ca.syncron.network.tcp.AbstractTcpConnector;
import ca.syncron.network.tcp.node.NodeClientBundler;
import ca.syncron.utils.ComConstants;
import ca.syncron.utils.Constants.Access;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.eventmachine.DelayedEvent;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.lang.System.out;

/**
 * @author Dawson
 */
// jsonMsg = {message_type: "digital", sender_type:"node",value:"0"}
public class User implements SocketObserver, ComConstants {
	public final static  Logger log                = LoggerFactory.getLogger(User.class.getName());
	private final static long   LOGIN_TIMEOUT      = 30 * 100000;
	private final static long   INACTIVITY_TIMEOUT = 500 * 60 * 1000;


	private Access mAccessLevel = Access.USER;
	private DelayedEvent mDisconnectEvent;
	ExecutorService executor = Executors.newSingleThreadExecutor();
	public static MessageProcessor mapper;
	public       String               targetMsg    = "EMPTY MESSAGE";
	//
	///////////////////////////////////////////////////////
	public       AbstractTcpConnector mServer      = null;
	private      NIOSocket            mSocket      = null;
	private      String               mName        = "NotSet";
	public       UserType             mType        = UserType.ANDROID;
	public       String               mUserId      = "NotSet";
	public final Date                 mTimeStamp   = new Date();
	public       boolean              isRegistered = false;
	private UserBundle                                    mBundle;
	private HashMap<String, NodeClientBundler.NodeBundle> mNodes;

	public String mNodeServerId;

	public String getNodeServerId() {
		return mNodeServerId;
	}

	public void setNodeServerId(String nodeServerId) {
		mNodeServerId = nodeServerId;
	}

	public HashMap<String, NodeClientBundler.NodeBundle> getNodes() {
		return mNodes;
	}

	public void setNodes(HashMap<String, NodeClientBundler.NodeBundle> bundle) {
		mNodes = bundle;
	}


	class Test extends Thread {
		public Test() {start();}

		public void run() {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				try {
					String s = br.readLine();
					Message msg = new Message();
					msg.setMessageType(Message.MessageType.CHAT);
					msg.setChatMessage("___________TESTING___________");
					msg.setUserType(UserType.NODE);
					sendMessageObject(msg);
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}
	}

	public void sendMessageObject(Message msg) {
		getSocket().write(mapper.serializeMessage(msg).getBytes());
	}

	public User() {}

	public User(AbstractTcpConnector server, NIOSocket socket) {
		mServer = server;
		mSocket = socket;
		getSocket().setPacketReader(new AsciiLinePacketReader());
		getSocket().setPacketWriter(new AsciiLinePacketWriter());
		getSocket().listen(this);
		setUserId(getSocketAddress());
		//mName = null;
		mBundle = new UserBundle();
		mapper = new MessageProcessor();
	}

	public void connectionOpened(NIOSocket nioSocket) {

//		 mDisconnectEvent = mServer.getEventMachine().executeLater(new
//		 Runnable() {
//		 public void run() {
//		 getSocket().write("{\"value\":\"2\",\"chatMessage\":\"DISCONNECTED\",\"admin\":true,\"reqResponse\":false,\"messageType\":\"CHAT\"}\n".getBytes());
//		 getSocket().closeAfterWrite();
//		 }
//		 }, LOGIN_TIMEOUT);

		Message msg = new Message();//Message.MessageType.REGISTER, UserType.ANDROID);
		msg.setMessageType(Message.MessageType.REGISTER);
		msg.setReqResponse(true);
		//msg.setTargetUser(this);
		//mServer.mHandler.addToReceiveQueue(msg);
		String s = mapper.serializeMessage(msg);
		//msg.serialize();
		log.info(s);
		nioSocket.write(s.getBytes());
		log.info("Registration request sent to user at " + getSocketAddress());
		// nioSocket.write("Please enter your name:".getBytes());
	}

	public String toString() {
		return mName != null ? mName + "@" + getSocket().getIp() : "anon@" + getSocket().getIp();
	}

	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		// Inform the other users if the user was logged in.
//		if (mName != null) {
//			log.info(mName + " left the chat.");
//			//mServer.broadcast(this, mName + " left the chat.");
//		}
		// Remove the user.
		unregister();

	}

	private void scheduleInactivityEvent() {
		// Cancel the last disconnect event, schedule another.
		if (mName != null && !mName.contains("node")) {
			if (mDisconnectEvent != null) mDisconnectEvent.cancel();
			mDisconnectEvent = mServer.getEventMachine().executeLater(new Runnable() {
				public void run() {
					getSocket().write("{\"messageType\":\"DISCONNECT\"}".getBytes());
					getSocket().closeAfterWrite();
				}
			}, INACTIVITY_TIMEOUT);
		}


		if (mDisconnectEvent != null && mName != null && mName.length() > 0 & mName.contains("node")) mDisconnectEvent.cancel();

	}

	// Received
	// ///////////////////////////////////////////////////////////////////////////////////

	public void packetReceived(NIOSocket socket, byte[] packet) {
		out.println("");
		mServer.packetReceived(this, packet);
	}

	public void packetSent(NIOSocket socket, Object tag) {
		mServer.packetSent(socket, tag);
	}

	public void sendBroadcast(byte[] bytesToSend) {
		// Only send broadcast to users logged in.
		//	if (mName != null) {
		getSocket().write(bytesToSend);
		//	}
	}

	public void sendToTarget(byte[] bytesToSend, User target) {
		// send to specific client
		if (target != null) {
			target.getSocket().write(bytesToSend);
		}
	}

	public void sendToTarget(String targetId, String msgString) {
		if (mServer.mUserMap.containsKey(targetId)) {
			User target = mServer.mUserMap.get(targetId);
			target.getSocket().write(msgString.getBytes());
			log.info("sending relay to " + targetId);
		}
	}

	public void sendMessage(String msgString) {
		getSocket().write(msgString.getBytes());
	}


//  Getter/Setter
//////////////////////////////////////////////////////////////////////////////////////////////////////////

	public String getSocketAddress() {
		return getSocket().getIp() + ":" + getSocket().getPort();
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		mUserId = userId;
	}

	public Date getTimeStamp() {
		return mTimeStamp;
	}


	public void setType(UserType type) {
		mType = type;
	}

	public UserType getType() {return mType;}

	/**
	 * @return object mSocket of type NIOSocket
	 */
	public NIOSocket getSocket() {
		return mSocket;
	}


	/**
	 * @return object mName of type String
	 */
	public String getName() {
		return this.mName;
	}

	/**
	 * @param m_name the mName to set
	 */
	public void setName(String m_name) {
		this.mName = m_name;

		setUserId(m_name + "@" + mSocket.getAddress().toString().replace("/", ""));
	}

	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void register(Message msg) {
		setName(msg.getUserName());
		setType(msg.getUserType());
		mBundle.init(this);
		setRegistered(true);
		try {
		log.info("User: " + getName() + format("{}", getType().toString()) + " has registered");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void unregister() {

		setRegistered(false);
		mServer.getUserBundles().remove(getUserBundle());
		mServer.removeUser(this);

		try {
			log.info("User: " + getName() + format("{}", getType().toString()) + " has disconnected");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		mServer.invalidateStatus();
	}


	public Access getAccessLevel() {
		return mAccessLevel;
	}

	public void setAccessLevel(Access accessLevel) {
		mAccessLevel = accessLevel;
	}

	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public UserBundle getUserBundle() {

		return mBundle;

	}

}




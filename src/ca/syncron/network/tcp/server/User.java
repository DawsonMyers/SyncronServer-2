/**
 * 
 */
package ca.syncron.network.tcp.server;


import ca.syncron.network.message.Message;
import ca.syncron.network.message.Message.UserType;
import ca.syncron.network.message.MessageProcessor;
import ca.syncron.network.tcp.AbstractTcpConnector;
import ca.syncron.utils.ComConstants;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

/**
 * @author Dawson
 *
 */
// jsonMsg = {message_type: "digital", sender_type:"node",value:"0"}
public class User implements SocketObserver, ComConstants {
	public final static  Logger log                = LoggerFactory.getLogger(User.class.getName());
	private final static long   LOGIN_TIMEOUT      = 30 * 100000;
	private final static long   INACTIVITY_TIMEOUT = 500 * 60 * 1000;
	public final  AbstractTcpConnector mServer;
	private final NIOSocket            m_socket;
	private       String               m_name;
	private       DelayedEvent         m_disconnectEvent;
	public String targetMsg = "EMPTY MESSAGE";
	ExecutorService executor = Executors.newSingleThreadExecutor();
	public static MessageProcessor mapper;
public UserType mType = UserType.ANDROID;

	public void setType(UserType type) {
		mType = type;
	}

	public UserType getType() {return mType;}

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

	public User(AbstractTcpConnector server, NIOSocket socket) {
		mServer = server;
		m_socket = socket;
		getSocket().setPacketReader(new AsciiLinePacketReader());
		getSocket().setPacketWriter(new AsciiLinePacketWriter());
		getSocket().listen(this);
		m_name = null;
		mapper = new MessageProcessor();
	}

	public void connectionOpened(NIOSocket nioSocket) {

//		 m_disconnectEvent = mServer.getEventMachine().executeLater(new
//		 Runnable() {
//		 public void run() {
//		 getSocket().write("{\"value\":\"2\",\"chatMessage\":\"DISCONNECTED\",\"admin\":true,\"reqResponse\":false,\"messageType\":\"CHAT\"}\n".getBytes());
//		 getSocket().closeAfterWrite();
//		 }
//		 }, LOGIN_TIMEOUT);

		Message msg = new Message( );//Message.MessageType.REGISTER, UserType.ANDROID);
		msg.setMessageType(Message.MessageType.REGISTER);
		msg.setReqResponse(true);
		//msg.setTargetUser(this);
		//mServer.mHandler.addToReceiveQueue(msg);
		String s = mapper.serializeMessage(msg);
		//msg.serialize();
		out.println(s);
		nioSocket.write(s.getBytes());
		out.println("Req sent to user");
		// nioSocket.write("Please enter your name:".getBytes());
	}

	public String toString() {
		return m_name != null ? m_name + "@" + getSocket().getIp() : "anon@" + getSocket().getIp();
	}

	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		// Inform the other users if the user was logged in.
		if (m_name != null) {
			out.println( m_name + " left the chat.");
			//mServer.broadcast(this, m_name + " left the chat.");
		}
		// Remove the user.
		mServer.removeUser(this);
	}

	private void scheduleInactivityEvent() {
		// Cancel the last disconnect event, schedule another.
		if (m_name != null && !m_name.contains("node")) {
			if (m_disconnectEvent != null) m_disconnectEvent.cancel();
			m_disconnectEvent = mServer.getEventMachine().executeLater(new Runnable() {
				public void run() {
					getSocket().write("{\"messageType\":\"DISCONNECT\"}".getBytes());
					getSocket().closeAfterWrite();
				}
			}, INACTIVITY_TIMEOUT);
		} 
		

			if (m_disconnectEvent != null  && m_name != null && m_name.length() > 0 & m_name.contains("node")) m_disconnectEvent.cancel();
		
	}

	// Received
	// ///////////////////////////////////////////////////////////////////////////////////

	public void packetReceived(NIOSocket socket, byte[] packet) {

		mServer.packetReceived(this,packet);
	}
	public void packetSent(NIOSocket socket, Object tag) {
		mServer.packetSent(socket,tag);
	}

	public void sendBroadcast(byte[] bytesToSend) {
		// Only send broadcast to users logged in.
	//	if (m_name != null) {
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
		if (mServer.connectedClients.containsKey(targetId)) {
			User target = mServer.connectedClients.get(targetId);
			target.getSocket().write(msgString.getBytes());
			out.println("sending relay to " + targetId);
		}
	}

	public void sendMessage(String msgString) {
		getSocket().write(msgString.getBytes());
	}
	

	/**
	 * @return object m_socket of type NIOSocket
	 */
	public NIOSocket getSocket() {
		return m_socket;
	}


	/**
	 * @return object m_name of type String
	 */
	public String getName() {
		return this.m_name ;
	}

	/**
	 * @param m_name
	 *             the m_name to set
	 */
	public void setName(String m_name) {
		this.m_name = this.m_name + "@" + m_socket.getAddress();;
	}

}

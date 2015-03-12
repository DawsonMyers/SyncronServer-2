package ca.syncron.purgatory;

import ca.syncron.network.message.Handler;
import ca.syncron.network.tcp.server.User;
import ca.syncron.utils.ComConstants;
import naga.ConnectionAcceptor;
import naga.NIOServerSocket;
import naga.NIOSocket;
import naga.ServerSocketObserver;
import naga.eventmachine.EventMachine;

import java.io.IOException;
import java.util.*;

/**
 * Created by Dawson on 3/7/2015.
 */


public class ServerOLD extends Thread implements ServerSocketObserver, ComConstants {

	public static Handler mHandler;
	public static volatile Map<String, User> connectedClients = new HashMap<String, User>();    // new
	private static         ServerOLD         mServer          = null;
	public EventMachine m_eventMachine;
	public List<User>   m_users;

	//
	// ///////////////////////////////////////////////////////////////////////////////////
	public ServerOLD() {}

	ServerOLD(EventMachine machine) {
		mServer = this;
		m_eventMachine = machine;
		m_users = new ArrayList<User>();
		// mUsers = new ArrayList<User>();
		mHandler = new Handler();
		mHandler.start();
	}

	@Override
	public void run() {

		int port = PORT_SERVER_TCP;
		try {
			EventMachine machine = new EventMachine();
			m_eventMachine = machine;
			NIOServerSocket socket = machine.getNIOService().openServerSocket(port);
			socket.listen(this);
			//socket.listen(new Server(machine));
			socket.setConnectionAcceptor(ConnectionAcceptor.ALLOW);
			machine.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//
	// ///////////////////////////////////////////////////////////////////////////////////

	public static ServerOLD getInstance() {
		if (mServer == null) {
			// mServer = new ServerTcp();
		}
		return mServer;

	}

	public void acceptFailed(IOException exception) {
		System.out.println("Failed to accept connection: " + exception);
	}

	public void serverSocketDied(Exception exception) {
		// If the server socket dies, we could possibly try to open a new
		// socket.
		System.out.println("Server socket died.");
		System.exit(-1);
	}

	public void newConnection(NIOSocket nioSocket) {
		// Create a new user to hande the new connection.
		System.out.println("New user connected from " + nioSocket.getIp() + ".");
		//m_users.add(new User(this, nioSocket));
	}

	public void removeUser(User user) {
		System.out.println("Removing user " + user + ".");
		m_users.remove(user);
	}

	public void broadcast(User sender, String string) {
		// We convert the packet, then send it to all users except the sender.
		byte[] bytesToSend = string.getBytes();
		for (User user : m_users) {
			if (user != sender) user.sendBroadcast(bytesToSend);
		}
	}

	public EventMachine getEventMachine() {
		return m_eventMachine;
	}

	public void sendToTarget(String targetId, String msgString) {
		if (connectedClients.containsKey(targetId)) {
			User target = connectedClients.get(targetId);
			target.getSocket().write(msgString.getBytes());
		}
	}

	public static User userLookup(String keyFragment) {
		for (String key : connectedClients.keySet()) {
			if (key.contains(keyFragment)) return connectedClients.get(key);
		}
		return null;
	}

}

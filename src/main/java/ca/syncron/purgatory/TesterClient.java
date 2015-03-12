package ca.syncron.purgatory;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AbstractTcpConnector;
import naga.NIOSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ca.syncron.network.message.Message.MessageType;
import static ca.syncron.network.message.Message.UserType;

/**
 * Created by Dawson on 3/7/2015.
 */
public class TesterClient extends  AbstractTcpConnector {
	static              String nameId = TesterClient.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
public static TesterClient client;
	public TesterClient() {

		isServer(false);

		//registerCallbacks(callbacks);
	}
	public static void main(String args[]) {
	 client = new TesterClient();
		client.start();
		input();
	}

	public static void input() {
		new Thread(() -> {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = "";
			while (true) {
				try {
					in =br.readLine();
					System.out.println(in);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (in.contains("pin")) {
					String s = in.replace("pin", "");
					String[] vals = s.split("_");
					int pin = Integer.parseInt(vals[0]);
					int val = Integer.parseInt(vals[1]);
					Message msg = new Message(UserType.ANDROID, UserType.NODE);
					msg.setPin("2");
					msg.setValue("1");

					client.sendMessage(msg);
					//client.setPin(Integer.parseInt(vals[0]), val);
				}
				switch (in) {
					case "chat":
						Message msg = new Message(MessageType.CHAT, UserType.ANDROID);
						msg.setChatMessage("DAWSON");
						client.sendMessage(msg);
						break;
					case "reg":
						Message msg1 = new Message(MessageType.REGISTER, UserType.ANDROID);
						msg1.setUserName("Dawson");
						client.sendMessage(msg1);
						break;
					case "digital":
						Message msg2 = new Message(MessageType.DIGITAL, UserType.ANDROID);
						client.sendMessage(msg2);
						break;
					case "recon":
						client.mSocket.close();
						client = null;
						try {
							Thread.sleep(1000);
							client = new TesterClient();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						client = new TesterClient();
//						Message msg2 = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
//						client.sendMessage(msg2);
						break;

				}
			}
		}).start();
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

	@Override
	public void sendMessage(Message msg) {
		super.sendMessage(msg);
		try {
			if (msg.serializeMessage().length() > 1) mSocket.write(msg.getSerialMessage().getBytes());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
}
class Test   {
	Test(TesterClient client) {mClient = client; }
	TesterClient mClient;
	void run() {
		new Thread(() -> {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = "";
			while (true) {
				try {
					in =br.readLine();
					System.out.println(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
				switch (in) {
					case "chat":
						Message msg = new Message(MessageType.CHAT, UserType.ANDROID);
						mClient.sendMessage(msg);
						break;
					case "reg":
						Message msg1 = new Message(MessageType.REGISTER, UserType.ANDROID);
						mClient.sendMessage(msg1);
						break;
					case "digital":
						Message msg2 = new Message(MessageType.DIGITAL, UserType.ANDROID);
						mClient.sendMessage(msg2);
						break;

				}
			}
		}).start();
	}

//	public static void input() {
//		new Thread(() -> {
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			String in = "";
//			while (true) {
//				try {
//					in = br.readLine();
//					log.info(in);
//					if (in.contains("pin")) {
//						String s = in.replace("pin", "");
//						String[] vals = s.split("_");
//						int pin = Integer.parseInt(vals[0]);
//						int val = Integer.parseInt(vals[1]);
//						con.setPin(Integer.parseInt(vals[0]), val);
//					}
//					if (in.contains("a")) {
//						log.info(getAnalogString());
//					}
//					if (in.contains("d")) {
//						log.info(getDigitalString());
//					}
//					switch (in) {
//						case "1":
//							con.setPin(2, 1);
//							break;
//						case "2":
//							con.setPin(2, 0);
//							break;
//						case "3":
//							con.setPin(3, 1);
//							break;
//						case "4":
//							con.setPin(3, 0);
//							break;
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}

}
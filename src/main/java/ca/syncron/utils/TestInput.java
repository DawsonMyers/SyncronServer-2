package ca.syncron.utils;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AppRegistrar;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.network.tcp.server.ServerController;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Dawson on 3/11/2015.
 */
public class TestInput {
	public static ServerController server;
	static              String           nameId = TestInput.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	static ClientController client;

	public TestInput(ClientController cli) {
		client = cli;
	}

	public static void input() {
		new Thread(() -> {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = "";
			while (true) {
				try {
					in = br.readLine();
					System.out.println(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
// pin2_1
				if (in != null) {
					if (in.contains("pin")) {
						String s = in.replace("pin", "");
						String[] vals = s.split("_");
						int pin = Integer.parseInt(vals[0]);
						int val = Integer.parseInt(vals[1]);

						Message msg = new Message(Message.UserType.ANDROID, Message.UserType.NODE);
						msg.digital(pin + "", val + "");
						msg.setPin(vals[0]);
						msg.setValue(vals[1]);

						client.mClient.sendMessage(msg);
						//client.setPin(Integer.parseInt(vals[0]), val);
					}
				} Message msg;
				switch (in) {
					case "stream1":
						msg = new Message(Message.MessageType.STREAM, Message.UserType.ANDROID);
						msg.setSampleRate(600);
						msg.setStreamEnabled(true);
						client.mClient.sendMessage(msg);
						break;
					case "stream0":
						msg = new Message(Message.MessageType.STREAM, Message.UserType.ANDROID);
						msg.setSampleRate(600);
						msg.setStreamEnabled(false);
						client.mClient.sendMessage(msg);
						break;
					case "chat":
						  msg = new Message(Message.MessageType.CHAT, Message.UserType.ANDROID);
						msg.setChatMessage("This is a chat message");
						client.mClient.sendMessage(msg);
						break;
					case "reg":
						 msg = new Message(Message.MessageType.REGISTER, Message.UserType.ANDROID);
						msg.setUserName("Dawson");
						client.mClient.sendMessage(msg);
						break;
					case "regN":
						 msg = new Message(Message.MessageType.REGISTER, Message.UserType.NODE);
						msg.setUserName("Dawson");
						client.mClient.sendMessage(msg);
						break;
					case "digital":
						 msg = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
						client.mClient.sendMessage(msg);
						break;
					case "count":
						int rCount = AppRegistrar.getConnector().mHandler.receiveCounter;
						int sCount = AppRegistrar.getConnector().mHandler.sendCounter;
						System.out.println("Recieved = " + rCount + "\n Sent = " + sCount);
						break;
					case "d":

						System.out.println("Digital = " + Arrays.toString(client.getDigital()) + "\n Analog = " + Arrays.toString(client.getAnalog()));
						break;
					case "recon":
						client.mClient.mSocket.close();
						client = null;
						try {
							Thread.sleep(1000);
						//	client = new TesterClient();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					//	client = new TesterClient();
//						Message msg2 = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
//						client.sendMessage(msg2);
						break;
					case "s1":
						msg = new Message(Message.MessageType.STREAM, Message.UserType.ANDROID);
						msg.setSampleRate(600);
						msg.setStreamEnabled(true);
						server.mServer.broadcast(msg);
						client.startStreaming();

						break;
					case "s2":

						//server.mServer.broadcast(msg);
						client.setStreamEnabled(true);
						client.startStreaming();

						break;
					case "sd":

						System.out.println("Digital = " + Arrays.toString(server.getDigital()) + "\n Analog = " + Arrays.toString(server.getAnalog()));
						break;
				}
			}
		}).start();
	}

	public static void serverRef(ServerController s) {
		server = s;
	}
}

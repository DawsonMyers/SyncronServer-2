package ca.syncron.utils;

import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.client.ClientController;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Dawson on 3/11/2015.
 */
public class TestInput {
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
					in =br.readLine();
					System.out.println(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
// pin2_1
				if (in.contains("pin")) {
					String s = in.replace("pin", "");
					String[] vals = s.split("_");
					int pin = Integer.parseInt(vals[0]);
					int val = Integer.parseInt(vals[1]);

					Message msg = new Message(Message.UserType.ANDROID, Message.UserType.NODE);
					msg.digital(pin+"",val+"");
					msg.setPin("2");
					msg.setValue("1");

					client.mClient.sendMessage(msg);
					//client.setPin(Integer.parseInt(vals[0]), val);
				}Message msg;
				switch (in) {
					case "chat":
						  msg = new Message(Message.MessageType.CHAT, Message.UserType.ANDROID);
						msg.setChatMessage("DAWSON");
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

				}
			}
		}).start();
	}
}

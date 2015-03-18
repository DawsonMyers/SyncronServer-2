package ca.syncron.tests;

import ca.syncron.boards.ArduinoConnector;
import ca.syncron.controller.AbstractController;
import ca.syncron.network.message.Message;
import ca.syncron.network.tcp.AppRegistrar;
import ca.syncron.network.tcp.client.Client;
import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.utils.Interfaces.PinCallbacks;
import ca.syncron.utils.Interfaces.PinStatus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ca.syncron.network.message.Message.UserType.ANDROID;
import static ca.syncron.network.message.Message.UserType.NODE;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/10/2015.
 */
public class ClientControllerTest extends AbstractController implements Runnable, PinCallbacks, PinStatus {
	static              String nameId = ClientControllerTest.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static Client           client;
	public static ArduinoConnector mArduino;
	public static ClientController me = new ClientController();

	public static String getNameId() {return nameId;}
	//  Constructors
	//////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ClientControllerTest() {
		AppRegistrar.register(this);
		//me = this;
		setUserName("Odroid");
		setUserType(NODE);
	}

	public static ClientController getInstance() {
		return me;
	}

	//  main()
///////////////////////////////////////////////////////
	public static void main(String[] args) {
		(mArduino = new ArduinoConnector(me)).start();
		(client = new Client(me)).start();
		//TestInput.input();
		input();
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

				if (in.contains("pin")) {
					String s = in.replace("pin", "");
					String[] vals = s.split("_");
					int pin = Integer.parseInt(vals[0]);
					int val = Integer.parseInt(vals[1]);
					Message msg = new Message(Message.UserType.ANDROID, Message.UserType.NODE);
					msg.setPin("2");
					msg.setValue("1");

					client.sendMessage(msg);
					//client.setPin(Integer.parseInt(vals[0]), val);
				}
				switch (in) {
					case "chat":
						Message msg = new Message(Message.MessageType.CHAT, Message.UserType.ANDROID);
						msg.setChatMessage("DAWSON");
						client.sendMessage(msg);
						break;
					case "reg":
						Message msg1 = new Message(Message.MessageType.REGISTER, Message.UserType.ANDROID);
						msg1.setUserName("Dawson");
						client.sendMessage(msg1);
						break;
					case "digital":
						Message msg2 = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
						client.sendMessage(msg2);
						break;
					case "stream1":
						msg = new Message(Message.MessageType.STREAM, Message.UserType.ANDROID);
						msg.setSampleRate(600);
						msg.setStreamEnabled(true);
						client.sendMessage(msg);
						break;
					case "stream0":
						msg = new Message(Message.MessageType.STREAM, Message.UserType.ANDROID);
						msg.setSampleRate(600);
						msg.setStreamEnabled(false);
						client.sendMessage(msg);
						break;
					case "recon":
						client.mSocket.close();
						client = null;
//						try {
//							Thread.sleep(1000);
//							client = new TesterClient();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						client = new TesterClient();
//						Message msg2 = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
//						client.sendMessage(msg2);
						break;

				}
			}
		}).start();
	}

	//  run()
///////////////////////////////////////////////////////
	public void run() {

	}

//  body
//////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int[] getAnalog() {
		return mArduino.getAnalog();
	}

	@Override
	public int[] getDigital() {
		return mArduino.getDigital();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
	}

	@Override
	public void setPin(int pin, int state) {
		mArduino.setPin(pin, state);
		log.info("ClientController - mArduino.setPin({},{})", pin, state);
	}

	@Override
	public void digitalPinChanged(int pin, int value) {
		invalidateDigital();

	}

	@Override
	public void analogPinChanged(int pin, int value) {
		invalidateAnalog();

	}

	@Override
	public void invalidateAnalog() {
//		Message msg = new Message(NODE, ANDROID);
//		msg.newUpdate(getDigital(), getAnalog());
		//client.sendUpdateMessage(msg);
	}

	@Override
	public void invalidateDigital() {
		Message msg = new Message(NODE, ANDROID);
		msg.update(getDigital(), getAnalog());
		client.sendUpdateMessage(msg);
	}

	@Override
	public boolean isStreamEnabled() {
		return super.isStreamEnabled();
	}

	@Override
	public void setStreamEnabled(boolean streamEnabled) {
		super.setStreamEnabled(streamEnabled);
		if (streamEnabled) {
			startStreaming();
		}
	}

	boolean isStreaming = false;

	private void startStreaming() {
		if (!isStreaming) {
			new Thread(() -> {
				long last = 0;
				isStreaming = true;
				while (isStreamEnabled()) {
					client.sendStreamMessage(null);
					try {
						Thread.sleep(getSampleRate());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} isStreaming = false;
			}).start();
		}
	}

	@Override
	public void setSampleRate(long sampleRate) {
		super.setSampleRate(sampleRate);
	}
}

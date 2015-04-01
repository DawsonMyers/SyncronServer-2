package ca.syncron.network.tcp.client;

import ca.syncron.boards.ArduinoConnector;
import ca.syncron.controller.AbstractController;
import ca.syncron.gui.netbeans.SyncTestGui;
import ca.syncron.network.tcp.AppRegistrar;
import ca.syncron.utils.Interfaces.PinCallbacks;
import ca.syncron.utils.Interfaces.PinStatus;
import ca.syncron.utils.TestInput;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.ArduinoTestGui;
import tests.TestGuiDialog;

import static ca.syncron.network.message.Message.UserType.NODE;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/10/2015.
 */
public class ClientController extends AbstractController implements Runnable, PinCallbacks, PinStatus {
	static              String nameId = ClientController.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static Client           mClient;
	public static ArduinoConnector mArduino;
	public static ClientController me = new ClientController();
	//private final SyncTestGui gui;
	private boolean mSreamEnabled;

	public String userName = "Client";

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String port = "/dev/ttyS10";

	public static String getNameId() {return nameId;}

	//  Constructors
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SyncTestGui getGui() {
		return SyncTestGui.getInstance();
	}

	public ClientController() {
//		ConnectionUi connectionPannel = new ConnectionUi();
//		connectionPannel.setVisible(true);
		AppRegistrar.register(this);
		me = this;
		setUserName("Odroid");
		setUserType(NODE);
		//gui = SyncTestGui.start();
//	Injector injector = Guice.createInjector(new
		//TestInput test = new TestInput(this);
		//test.input();
	}

	public static ClientController getInstance() {
		return me;
	}

	public static ClientController createInjector() {
		return me;
	}

	public static ClientController inject() {
		Injector injector = Guice.createInjector();
		return me;
	}

	static ArduinoTestGui testGui;
	@Inject
	static TestGuiDialog  dialog;

	//  main()
///////////////////////////////////////////////////////
	//public static void main(String[] args) {
	public static ClientController begin(String[] args) {
		(mArduino = new ArduinoConnector(me)).start();
		(mClient = new Client(me)).start();
		TestInput input = new TestInput(me);
		input.input();
//		dialog = new TestGuiDialog();
//		TestGuiDialog dialog = new TestGuiDialog();
//		dialog.pack();
//		dialog.setVisible(true);
//		dialog
		//testGui = new ArduinoTestGui();
		return me;
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
		//digitalVals[pin] = state;
		mArduino.setPin(pin, state);
		//gui.setPin(pin, state);
		log.info("ClientController - mArduino.setPin({},{})", pin, state);
	}

	@Override
	public void digitalPinChanged(int pin, int value) {
		//log.info("Digital pin #" +pin + " set to: " + value);
		//invalidateDigital();

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
//		Message msg = new Message(NODE, ANDROID);
//		msg.update(getDigital(), getAnalog());
		mClient.sendUpdateMessage(null);
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

	public boolean isStreaming = false;
	Stream t;


	public void startStreaming() {
		t = new Stream(this);
		//t.start();
		log.info("trying to start stream");
//		if (!isStreaming){
//		Thread t = new Thread(() -> {
//			int c = 0;
//			long last = 0;
//			isStreaming = true;
//			while (isStreamEnabled()) {
//				mClient.sendStreamMessage(null);
//				c++;
//				log.info("Streaming...#"+c);
//				try {
//					Thread.sleep(getSampleRate());
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} isStreaming = false;
//		},"Streamer");
//	t.start();
	}


	@Override
	public void setSampleRate(long sampleRate) {
		super.setSampleRate(sampleRate);
	}

	public boolean getSreamEnabled() {
		return mSreamEnabled;
	}
}

class Stream extends Thread {
	private final ClientController mController;

	public Stream(ClientController controller) {
		mController = controller;
	}

	public boolean isStreaming = false;

	public void run() {
		int c = 0;
		long last = 0;
		isStreaming = true;
		while (mController.isStreamEnabled()) {
			mController.mClient.sendStreamMessage(null);
			c++;
			mController.log.info("Streaming...#" + c);
			try {
				Thread.sleep(mController.getSampleRate());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isStreaming = false;
	}

}
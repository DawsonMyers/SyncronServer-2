package ca.syncron.network.tcp.client;

import ca.syncron.boards.ArduinoConnector;
import ca.syncron.controller.AbstractController;
import ca.syncron.network.message.Message;
import ca.syncron.utils.Interfaces.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.syncron.network.message.Message.UserType.*;
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

	//  Constructors
	//////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ClientController() {
		me = this;

	}

	public static ClientController getInstance() {
		return me;
	}

	//  main()
///////////////////////////////////////////////////////
	public static void main(String[] args) {
		//(mArduino = new ArduinoConnector(me)).start();
		(mClient = new Client(me)).start();
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
		Message msg = new Message(NODE, ANDROID);
		msg.update(getDigital(), getAnalog());
		mClient.sendMessage(null);
	}

	@Override
	public void invalidateDigital() {
		Message msg = new Message(NODE, ANDROID);
		msg.update(getDigital(), getAnalog());
		mClient.sendMessage(null);
	}
}

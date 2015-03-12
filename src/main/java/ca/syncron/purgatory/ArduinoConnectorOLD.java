package ca.syncron.purgatory;

import ca.syncron.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.*;
import org.zu.ardulink.protocol.IProtocol;
/**
 * Created by Dawson on 3/9/2015.
 */
public class ArduinoConnectorOLD {
	static              String nameId = ArduinoConnectorOLD.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);

	public ArduinoConnectorOLD() {}

	public static void main(String[] args) {
		Link link = Link.getDefaultInstance();

		link.addConnectionListener(new ConnectionListener() {

			@Override
			public void disconnected(DisconnectionEvent e) {
				System.out.println("Board disconnected");
			}

			@Override
			public void connected(ConnectionEvent e) {
				System.out.println("Board connected");
			}
		});

		link.connect(Constants.Ports.SERIAL_WINDOWS);
		try {
			System.out.println("wait for a while");
			Thread.sleep(2000);
			System.out.println("proceed");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("start Listening");
		link.addDigitalReadChangeListener(new DigitalReadChangeListener() {

			@Override
			public void stateChanged(DigitalReadChangeEvent e) {
				System.out.println("PIN: " + e.getPin() + " STATE: " + e.getValue());
				System.out.println(e.getIncomingMessage());
			}

			@Override
			public int getPinListening() {
				return 3;
			}
		});

		for(int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
				System.out.println("sendPowerON");
				link.sendPowerPinSwitch(5, IProtocol.HIGH);
				Thread.sleep(1000);
				System.out.println("sendPowerOFF");
				link.sendPowerPinSwitch(5, IProtocol.LOW);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		try {
			System.out.println("wait for a while");
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		link.disconnect();
	}
	}


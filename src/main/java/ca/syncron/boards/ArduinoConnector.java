package ca.syncron.boards;

import ca.syncron.network.tcp.client.ClientController;
import ca.syncron.utils.Constants;
import ca.syncron.utils.Interfaces.PinCallbacks;
import ca.syncron.utils.Interfaces.PinControl;
import ca.syncron.utils.Interfaces.RawDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zu.ardulink.Link;
import org.zu.ardulink.event.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Dawson on 3/9/2015.
 */
public class ArduinoConnector extends Thread implements PinCallbacks, RawDataAccess {
	static              String                 nameId = ArduinoPins.class.getSimpleName();
	public final static Logger                 log    = LoggerFactory.getLogger(nameId);
	static              ReentrantReadWriteLock mLock  = new ReentrantReadWriteLock();
	//public static ArdulinkSerial    con;
	public static ArduinoConnector con;
	public static          Link   link        = Link.getDefaultInstance();
	public static          String port        = Constants.Ports.SERIAL_LINUX;//PORT_SERIAL_LINUX;	//"/dev/ttyS10"; //
	public volatile static int[]  analogVals  = new int[12];
	public volatile static int[]  digitalVals = new int[12];
	public volatile static String analog      = "";
	public volatile static String digital     = "";
	public static ClientController mController;
	public static PinCallbacks     mObserver;
	private int digitalPinCount = 12;
	private int analogPinCount  = 12;

	public ArduinoConnector() {}

	public ArduinoConnector(ClientController controller) {
		this();
		mObserver = controller;
		mController = controller;
		Constants.Ports.setSerialPort(Constants.Ports.SERIAL_LINUX);
	}

	//public static void main(String[] args) {
	public static ArduinoConnector connect() {
		con = new ArduinoConnector();
		con.start();
		Link link = Link.getDefaultInstance();
		//input();
		return con;
	}
//		con = new ArdulinkSerial(con);
//		con.start();
	//}

	public static void input() {
		new Thread(() -> {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = "";
			while (true) {
				try {
					in = br.readLine();
					log.info(in);
					if (in.contains("pin")) {
						String s = in.replace("pin", "");
						String[] vals = s.split("_");
						int pin = Integer.parseInt(vals[0]);
						int val = Integer.parseInt(vals[1]);
						con.setPin(Integer.parseInt(vals[0]), val);
					}
					if (in.contains("a")) {
						log.info(getAnalogString());
					}
					if (in.contains("d")) {
						log.info(getDigitalString());
					}
					switch (in) {
						case "1":
							con.setPin(2, 1);
							break;
						case "2":
							con.setPin(2, 0);
							break;
						case "3":
							con.setPin(3, 1);
							break;
						case "4":
							con.setPin(3, 0);
							break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setPin(int pin, int state) {
		Link link = Link.getDefaultInstance();
		link.sendPowerPinSwitch(pin, state);
		if (!link.isConnected()) log.info("ARDUINO IS NOT CONNECTED");
	}

	@Override
	public void digitalPinChanged(int pin, int value) {
		log.info("Digital change: pin = " + pin + " | state= " + value);
		mLock.writeLock().lock();
		try {
			digitalVals[pin - 2] = value;
			mObserver.digitalPinChanged(pin, value);
		} finally {
			mLock.writeLock().unlock();
		}

	}

	@Override
	public void analogPinChanged(int pin, int value) {
		mLock.writeLock().lock();
		try {
			analogVals[pin] = value;
			mObserver.analogPinChanged(pin, value);
		} finally {
			mLock.writeLock().unlock();
		}
		//log.info("Analog change: pin = " + pin +" | value = "+ value);
	}

	static void updateAnalog(int pin, int value) {
		mLock.writeLock().lock();
		analogVals[pin] = value;
		mLock.writeLock().unlock();
	}

	public void run() {
		link = Link.getDefaultInstance();
		link.addConnectionListener(new ConnectionListener() {
			@Override
			public void disconnected(DisconnectionEvent e) {
				log.info("Disconnected from Arduino");
			}

			@Override
			public void connected(ConnectionEvent e) {
				log.info("Connected to Arduino");
			}
		});
		try {
			link.connect(port);
		} catch (UnsatisfiedLinkError e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArduinoPins pins = new ArduinoPins(this, link, analogPinCount, digitalPinCount);

	}

	public static String getAnalogString() {
		String s = null;
		mLock.readLock().lock();
		try {
			s = Arrays.toString(analogVals);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mLock.readLock().unlock();
		}
		return s;
	}

	public static String getDigitalString() {
		String s = null;
		mLock.readLock().lock();
		try {
			s = Arrays.toString(digitalVals);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mLock.readLock().unlock();
		}
		return s;
	}

	@Override
	public int[] getAnalog() {
		int[] vals = null;
		mLock.readLock().lock();
		try {
			vals = analogVals;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mLock.readLock().unlock();
		}
		return vals;
	}

	@Override
	public int[] getDigital() {
		int[] vals = null;
		mLock.readLock().lock();
		try {
			vals = digitalVals;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mLock.readLock().unlock();
		}
		return vals;
	}


/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Created by Dawson on 3/9/2015.
	 */
	public class ArduinoPins {
		public volatile AnalogPin[]  mAnalogPins;
		public volatile DigitalPin[] mDigitalPins;
		PinCallbacks mObserver;
		Link                    mLink;

		public ArduinoPins(PinCallbacks observer, Link link, int analogPins, int digitalPins) {
			for (int i = 0; i < analogPins; i++) {
				mAnalogPins = new AnalogPin[analogPins];
				mAnalogPins[i] = new AnalogPin(observer, link);
				mAnalogPins[i].initPin(i);
			}
			for (int i = 0; i < digitalPins; i++) {
				mDigitalPins = new DigitalPin[digitalPins];
				mDigitalPins[i] = new DigitalPin(observer, link);
				mDigitalPins[i].initPin(i + 2);
			}
		}

		public class AnalogPin implements PinControl {
			PinCallbacks mObserver;
			Link         mLink;

			public AnalogPin(PinCallbacks observer, Link link) {
				mObserver = observer;
				mLink = link;
			}

			public int currentValue = 0;
			public int pinId        = 0;

			public void initPin(final int pin) {
				pinId = pin;
				mLink = Link.getDefaultInstance();
				mLink.addAnalogReadChangeListener(new AnalogReadChangeListener() {
					@Override
					public int getPinListening() {
						return pinId;
					}

					@Override
					public void stateChanged(AnalogReadChangeEvent e) {
						currentValue = e.getValue();
						mObserver.analogPinChanged(e.getPin(), e.getValue());
					}

				});
			}

			@Override
			public void sePinValue(int value) {
				mLink = Link.getDefaultInstance();
				mLink.sendPowerPinIntensity(pinId, value);
				if (!mLink.isConnected()) log.info("ARDUINO IS NOT CONNECTED");
			}

			@Override
			public void setPinListen(int value) {
				if (value == 1) mLink.startListenAnalogPin(pinId);
				else mLink.stopListenAnalogPin(pinId);
			}

			@Override
			public void setPinMode(int value) {

			}
		}

		public class DigitalPin implements PinControl {
			PinCallbacks mObserver;
			Link mLink = Link.getDefaultInstance();
			public int currentValue = 0;
			public int pinId        = 0;

			public DigitalPin(PinCallbacks observer, Link link) {
				mObserver = observer;
				mLink = link;
			}

			public void initPin(final int pin) {
				pinId = pin;
				mLink = Link.getDefaultInstance();
				mLink.addDigitalReadChangeListener(new DigitalReadChangeListener() {
					@Override
					public int getPinListening() {
						return pinId;
					}

					@Override
					public void stateChanged(DigitalReadChangeEvent e) {
						currentValue = e.getValue();
						mObserver.digitalPinChanged(e.getPin(), e.getValue());
					}

				});
			}

			@Override
			public void sePinValue(int value) {
				mLink = Link.getDefaultInstance();
				mLink.sendPowerPinSwitch(pinId, value);
				if (!mLink.isConnected()) log.info("ARDUINO IS NOT CONNECTED");
			}

			@Override
			public void setPinListen(int value) {
				if (value == 1) mLink.startListenDigitalPin(pinId);
				else mLink.stopListenDigitalPin(pinId);
			}

			@Override
			public void setPinMode(int value) {

			}
		}

	}
}

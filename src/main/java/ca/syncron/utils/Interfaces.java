package ca.syncron.utils;

import org.zu.ardulink.Link;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/10/2015.
 */
public interface Interfaces {
	public interface RawDataAccess {
		int[] getAnalog();
		int[] getDigital();
	}

	interface PinCallbacks {
		default void setPin(int pin, int state) {
			Link link = Link.getDefaultInstance();
			link.sendPowerPinSwitch(pin, state);
			if (!link.isConnected()) out.println("ARDUINO IS NOT CONNECTED");
		}

		void digitalPinChanged(int pin, int value);

		void analogPinChanged(int pin, int value);
	}

	interface PinControl {
		void sePinValue(int value);

		void setPinListen(int value);

		void setPinMode(int value);
	}

	interface PinAccess {
		void setAnalogPinValue(int value);

		void setDigitalPinValue(int pin, int value);

		void setAnalogPinListen(int pin, int value);

		void setDigitalPinListen(int pin, int value);

		void setAnalogPinMode(int pin, int value);

		void setDigitalPinMode(int pin, int value);
	}

	interface PinStatus {
		void invalidateAnalog();
		void invalidateDigital();
	}
//	interface StatusCallbacks{
//		void userConnected()
//	}
}

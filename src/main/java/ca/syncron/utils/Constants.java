package ca.syncron.utils;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Constants {

	private Constants() {}

	public static class Ports {
		public static final int    TCP            = 6500;
		public static final String SERIAL_LINUX   = "/dev/ttyS10";
		public static final String SERIAL_WINDOWS = "COM4";
		public static final String SERIAL_WINDOWS8 = "COM8";
		public static       String serialPort     = SERIAL_LINUX;

		public enum SerialPorts {
			LINUX("/dev/ttyS10"), WINDOWS("COM4");
			String p;

			SerialPorts(String s) {
				p = s;
			}
		}

		public static int getTcp() {return TCP;}

		public static String getSerialPort() {return serialPort;}

		public static void setSerialPort(String port) {serialPort = port;}
	}

	public static class IpAddresses {

		public static final String HOME         = "192.168.1.109";
		public static final String SERVER       = "192.168.1.109";
		public static final String IP_HOME      = "192.168.1.109";
		public static final String IP_LOCAL     = "localhost";
		public static final String IP_SERVER    = "192.163.250.179";
		public static final String HTTP_SYNCRON = "http://syncron.ca";
		public static final String HTTP_DAWSON  = "http://dawsonmyers.ca";
		public static final String HTTP_SERVER  = "http://dawsonmyers.ca";
		public static String IP = IP_HOME;

		public static void setIp(String ip) {
			IP = ip;
		}
	}

	public enum Ports1 {
		TCP(6500);
		int port;

		Ports1(int p) {
			port = p;
		}
	}

	public enum Access {GUEST, USER, ELEVATED, ADMIN;}

}

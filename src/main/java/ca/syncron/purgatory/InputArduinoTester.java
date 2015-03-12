package ca.syncron.purgatory;

import ca.syncron.boards.ArduinoConnector;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Dawson on 3/10/2015.
 */
public class InputArduinoTester extends Thread {
	static              String           nameId = InputArduinoTester.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
public static ArduinoConnector con;
	public InputArduinoTester(ArduinoConnector connector) {con = connector;}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String in = "";
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
				log.info(con.getAnalogString());
			}
			if (in.contains("d")) {
				log.info(con.getDigitalString());
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


}

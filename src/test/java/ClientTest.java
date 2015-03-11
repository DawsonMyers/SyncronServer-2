package test.java;

import ca.syncron.network.tcp.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/8/2015.
 */
public class ClientTest {
	static              String nameId = ClientTest.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
public static Client mClient;
	public ClientTest() {}
	public static void main(String args[]) {

		//mClient = new Client( );
		mClient.start();
	}
}

package test.java;

import ca.syncron.network.tcp.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/8/2015.
 */
public class ServerTest {
	static              String nameId = ServerTest.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static Server mServer;

	public ServerTest() {}

	public static void main(String args[]) {

		mServer = new Server();
		mServer.start();
	}
}

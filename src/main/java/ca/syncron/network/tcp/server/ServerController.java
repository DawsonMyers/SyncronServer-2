package ca.syncron.network.tcp.server;

import ca.syncron.controller.AbstractController;
import ca.syncron.network.tcp.AppRegistrar;
import ca.syncron.utils.TestInput;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/11/2015.
 */
public class ServerController extends AbstractController {
	static              String           nameId = ServerController.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	public static       ServerController me     = new ServerController();
	public static Server mServer;

	public static String getNameId() {return nameId;}
	public static ServerController getInstance() {
		return me;
	}

	public ServerController() {AppRegistrar.register(this);}

	//  main()
///////////////////////////////////////////////////////
	public static void main(String[] args) {

		(mServer = new Server(true)).start();
		TestInput.input();
		TestInput.serverRef(me);
	}

	@Override
	public void run() {

	}
}

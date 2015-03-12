package ca.syncron.network.tcp.server;

import ca.syncron.controller.AbstractController;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/11/2015.
 */
public class ServerController extends AbstractController {
	static              String           nameId = ServerController.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	public static       ServerController me     = new ServerController();

	public static ServerController getInstance() {
		return me;
	}
	public ServerController() {}


	@Override
	public void run() {

	}
}

package ca.syncron.network.tcp.client;

/**
 * Created by Dawson on 4/9/2015.
 */
public class RunClient {
	static              String           nameId = RunClient.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public RunClient() {}

	static ClientController client;

	public static void main(String[] args) {

		client = ClientController.begin(null);
	}
}

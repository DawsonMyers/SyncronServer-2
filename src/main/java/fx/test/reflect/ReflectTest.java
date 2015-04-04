package fx.test.reflect;

import ca.syncron.network.tcp.server.UserBundle;
import fx.tables.MyTableRow;

/**
 * Created by Dawson on 4/3/2015.
 */
public class ReflectTest {
	static              String           nameId = ReflectTest.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public ReflectTest() {}


	public static void main(String[] args) {
		UserBundle u1 = new UserBundle();
		UserBundle u2 = new UserBundle();
		MyTableRow tr = new MyTableRow(u1, u2);
	}
}

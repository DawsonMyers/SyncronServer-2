import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/12/2015.
 */
public class LogTester {
	static              String           nameId = LogTester.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public LogTester() {}

	public static void main(String[] args) {
		log.warn("warn");
		log.info("info");
		log.debug("debug");
		log.error("error");
		myMethod();
	}

	static void myMethod() {
		log.warn("warn");
		log.info("info");
		log.debug("debug");
		log.error("error");
	}


}

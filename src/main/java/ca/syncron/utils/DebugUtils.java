package ca.syncron.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

/**
 * Created by Dawson on 3/13/2015.
 */
public class DebugUtils {
	static              String           nameId       = DebugUtils.class.getSimpleName();
	public final static org.slf4j.Logger log          = LoggerFactory.getLogger(nameId);
	private static      boolean          isDebuggable = true;

	public DebugUtils() {}

	public DebugUtils(boolean b) {isDebuggable = b;}

	public static boolean debuggable() {
		return isDebuggable;
	}

	public static void setDebuggable(boolean b) {
		isDebuggable = b;
	}

	public static String formatJsonString(String str) {
		String s = "";
		if (!debuggable()) return "";

		try {
			StringWriter writer = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			//setFormat(true);
			//out.println(mapper.writeValueAsString(msgString));
			Object obj = str;
			log.debug(s = mapper.writeValueAsString("\n" + obj));

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

}

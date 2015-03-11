/**
 *
 */
package ca.syncron.network.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

/**
 * @author Dawson
 */

public class MessageProcessor1 {
	ExecutorService executor = Executors.newCachedThreadPool();
	public static       MessageProcessor1 mProcessor = new MessageProcessor1();
	public final static Logger            log        = LoggerFactory.getLogger(MessageProcessor1.class.getName());
	static Message      msg;
	static StringWriter writer;
	static ObjectMapper mapper;

	public MessageProcessor1() {
		writer = new StringWriter();
		mapper = new ObjectMapper();
		setFormat(false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
	}

	public static void setFormat(boolean bool) {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, bool);
	}

	public synchronized static String serializeMessage(Message msg) {
		if (msg == null) {
			out.println("MessageProcessor:Serialize - Null message");
			return null;
		}
		setFormat(true);
		mapper = new ObjectMapper();

		try {
			mapper.writeValue(writer, msg);
			out.println("serializeMessage");
			out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
			//System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return writer.toString();
	}

	public synchronized static Message deserializeMessage(String msgString) {
		try {
			setFormat(true);
			out.println("deserializeMessage");
			msg = mapper.readValue(msgString, Message.class);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return msg;
	}
}

 

 
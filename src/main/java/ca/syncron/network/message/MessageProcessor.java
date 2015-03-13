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

/**
 * @author Dawson
 */

public class MessageProcessor {
	ExecutorService executor = Executors.newSingleThreadExecutor();
	//public        MessageProcessor mProcessor = new MessageProcessor();
	public final static Logger log = LoggerFactory.getLogger(MessageProcessor.class.getName());
	//Message      msg;
	StringWriter writer;
	ObjectMapper mapper;

	public MessageProcessor() {
		writer = new StringWriter();
		mapper = new ObjectMapper();
		setFormat(false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
	}

	public void setFormat(boolean bool) {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, bool);
	}

	public synchronized String serializeMessage(Message msg) {
		if (msg == null) {
			log.info("MessageProcessor:Serialize - Null message");
			return null;
		}
		StringWriter	writer = new StringWriter();
		ObjectMapper	mapper = new ObjectMapper();
		//setFormat(true);
		//mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writeValue(writer, msg);
			log.info("serializeMessage");
			//log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
			log.info("\n\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return writer.toString();
	}

	public synchronized  Message deserializeMessage(String msgString) {
		Message msg = new Message();
		try {
			StringWriter	writer = new StringWriter();
			ObjectMapper	mapper = new ObjectMapper();
			//setFormat(true);
			//out.println(mapper.writeValueAsString(msgString));


			log.debug("deserializeMessage");
			msg = mapper.readValue(msgString, Message.class);

			mapper.writeValue(writer, msg);
			log.info("\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return msg;
	}
}

 

 
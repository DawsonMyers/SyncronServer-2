package test.java;

import ca.syncron.network.message.Handler;
import ca.syncron.network.message.Message;
import ca.syncron.network.message.MessageBuilder;
import ca.syncron.network.message.MessageCallbacks;
import ca.syncron.network.tcp.server.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;

/**
 * Created by Dawson on 3/7/2015.
 */
public class MessageTest {
	static              String          nameId = MessageTest.class.getSimpleName();
	public final static Logger          log    = LoggerFactory.getLogger(nameId);
	static              int             count  = 0;
	static              ExecutorService ex     = Executors.newCachedThreadPool();
	static              long            t      = 0;
	public static Server                             mServer;
	public static Handler                            mHandler;
	static        MessageCallbacks.DispatchCallbacks callbacks;
	public static int                                c;
	ExecutorService executor = Executors.newSingleThreadExecutor();
//	public static MessageProcessor mapper;
	public static ObjectMapper     mapper;
	static StringWriter writer;

	//@Loggable(logThis = true)
	public static void main(String args[]) {
//		mHandler = new Handler();
//		mHandler.start();
		//initCallbacks();
//		mServer = new Server();
//		mServer.start();
		writer = new StringWriter();
		mapper = new ObjectMapper();
		t = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			Message msg = build();
			count++;
		}
//		out.println("\n\n\t generation time = " + (System.currentTimeMillis() - t) + "\n\t Cycle count = " + count);
	}

	private static void process(Message msg) {
		//out.println("Message Type: " + msg.getMessageType());
		//msg.setSerialMessage(mapper.w(msg));
		//out.println(msg.getSerialMessage());
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writeValue(writer, msg);
			out.println("serializeMessage");
			out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
			//System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		//return writer.toString();
	}

	public MessageTest() {

	}

	//@Cacheable
	//@Async
	//@Parallel(threads = 5)
	//@Loggable(precision = 3, unit = TimeUnit.MILLISECONDS, prepend = true)
	public static Message build() {
		Message msg1 = null;
		ex.execute(() -> {

			Message msg = null;
			//for (int i = 0; i < 5; i++) {
			count++;
			msg = MessageBuilder
					.newMessage().withAdmin(true).withChatMessage("chat")
					.withMessageType(Message.MessageType.CHAT).withValue(1 + "")
					.build();
			process(msg);
			//	}
//			if (c >= 1000)
//				out.println("\n\n\t generation time = " + (System.currentTimeMillis() - t) + "\n\t Cycle count = " + count);
		});
		return msg1;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
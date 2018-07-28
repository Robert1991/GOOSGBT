package test.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class SingleMessageListener implements MessageListener {
	private final ArrayBlockingQueue<Message> messages =
			new ArrayBlockingQueue<Message>(1);
	
	
	@Override
	public void processMessage(Chat chat, Message message) {
		messages.add(message);		
	}
	
	public void receivesAMessage() throws InterruptedException {
		assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
	}
}

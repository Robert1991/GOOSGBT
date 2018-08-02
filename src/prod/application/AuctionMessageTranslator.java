package prod.application;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

	private final AuctionEventListener eventListener;

	public AuctionMessageTranslator(AuctionEventListener listener) {
		this.eventListener = listener;
	}

	@Override
	public void processMessage(Chat unusedChat, Message message) {
		HashMap<String, String> event = unpackEventFrom(message);

		String type = event.get("Event");
		if ("CLOSE".equals(type))
			eventListener.auctionClosed();
		else if("PRICE".equals(type))
			eventListener.currentPrice(Integer.parseInt(event.get("CurrentPrice")), 
					Integer.parseInt(event.get("Increment")));
	}


	private HashMap<String, String> unpackEventFrom(Message message) {
		HashMap<String, String> event = new HashMap<String, String>();
		for(String element : message.getBody().split(";")) {
			String[] pair = element.split(":");
			event.put(pair[0].trim(),pair[1].trim());
		}
		return event;
	}

}

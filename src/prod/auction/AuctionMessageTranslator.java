package prod.auction;

import java.util.HashMap;
import java.util.Map;

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
		AuctionEvent event = AuctionEvent.from(message.getBody());
		
		String eventType = event.type();
		if ("CLOSE".equals(eventType))
			eventListener.auctionClosed();
		else if("PRICE".equals(eventType))
			eventListener.currentPrice(event.currenPrice(), 
					event.increment());
	}
	
	private static class AuctionEvent {
		final Map<String, String> fields = new HashMap<String,String>();
		
		public String type() { return get("Event"); }
		public int increment() { return getInt("Increment"); }
		public int currenPrice() { return getInt("CurrentPrice"); }
		
		String get(String fieldName) {
			return fields.get(fieldName);
		}
		
		int getInt(String fieldName) {
			return Integer.parseInt(get(fieldName));
		}
		
		void addField(String field) {
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}
		
		static AuctionEvent from(String messageBody) {
			AuctionEvent event = new AuctionEvent();
			
			for(String field : fieldsIn(messageBody))
				event.addField(field);
			
			return event;
		}
		
		static String[] fieldsIn(String messageBody) {
			return messageBody.split(";");
		}

		
		
	}
}

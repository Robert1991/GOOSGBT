package prod.auction;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import prod.auctionsniper.SniperListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener {
	private final AuctionEventListener eventListener;
	private final String sniperId;
	
	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
		this.eventListener = listener;
		this.sniperId = sniperId;
	}

	@Override
	public void processMessage(Chat unusedChat, Message message) {
		AuctionEvent event = AuctionEvent.from(message.getBody());
		
		String eventType = event.type();
		if ("CLOSE".equals(eventType))
			eventListener.auctionClosed();
		else if("PRICE".equals(eventType))
			eventListener.currentPrice(event.currenPrice(),
									   event.increment(),
									   event.isFromSniper(sniperId));
	}
	
	private static class AuctionEvent {
		final Map<String, String> fields = new HashMap<String,String>();
		
		public String type() { return get("Event"); }
		public String bidder() { return get("Bidder"); }
		public int increment() { return getInt("Increment"); }
		public int currenPrice() { return getInt("CurrentPrice"); }
		
		public PriceSource isFromSniper(String sniperId) { 
			return bidder().equals(sniperId) ? 
					PriceSource.FromSniper : PriceSource.FromOtherBidder; }
		
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

package test.util;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import prod.application.Main;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;

public class FakeAuctionServer {
	private static final String AUCTION_PASSWORD = "auction";
	
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
	public static final String XMPP_HOSTNAME = "127.0.0.1";

	private final SingleMessageListener messageListener = new SingleMessageListener();
	private final String itemId;
	private final XMPPConnection connection;
	private Chat currentChat;

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException {
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				currentChat = chat;
				currentChat.addMessageListener(messageListener);
			}
		});
	}
	
	public void hasReceivedJoinRequestFromSniper(String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId,
				equalTo(Main.JOIN_COMMAND_FORMAT));
	}
	
	public void reportPrice(int price, int increment, String bidder) throws XMPPException {		
		currentChat.sendMessage(
				String.format("SOLVersion: 1.1; Event: PRICE; " +
		"CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder));
	}

	public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
		receivesAMessageMatching(sniperId, 
				equalTo(format(Main.BID_COMMAND_FORMAT, bid)));
	}

	public void announceClosed() throws XMPPException {
		currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
	}

	public void stop() {
		connection.disconnect();
	}

	public String getItemId() {
		return itemId;
	}
	
	private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException {
		messageListener.receivesAMessage(messageMatcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperId));
	}
	
	private String format(String format, Object...args) {
		return String.format(format, args);
	}
}

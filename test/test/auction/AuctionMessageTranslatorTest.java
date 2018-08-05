package test.auction;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import prod.auction.AuctionEventListener;
import prod.auction.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest {
	public static final Chat UNUSED_CHAT = null;
	
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	@Mock
	private AuctionEventListener listener;
		
	private AuctionMessageTranslator translator;
	
	@Before
	public void InitAuctionMessageTranslator() {
		translator = new AuctionMessageTranslator(listener);
	}
	
	@Test public void notifiesAuctionClosedWhenCloseMessageIsReceived() {
		context.checking(new Expectations() {{
			oneOf(listener).auctionClosed();
		}});
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		
		translator.processMessage(UNUSED_CHAT, message);
	}
	
	@Test public void notifiesBidDetailsWhenCurrentPriceMessageReceived() {
		context.checking(new Expectations(){{
			exactly(1).of(listener).currentPrice(192, 7);
		}});
		
		Message message = new Message();
		
		message.setBody(
				"SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.processMessage(UNUSED_CHAT, message);
	}
	
}
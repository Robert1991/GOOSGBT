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
import prod.auctionsniper.SniperListener.PriceSource;

public class AuctionMessageTranslatorTest {
	public static final Chat UNUSED_CHAT = null;

	private static final String SNIPER_ID = "SomeSniper";
	
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	@Mock
	private AuctionEventListener listener;
		
	private AuctionMessageTranslator translator;
	
	@Before
	public void InitAuctionMessageTranslator() {
		translator = new AuctionMessageTranslator(SNIPER_ID ,listener);
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
			exactly(1).of(listener).currentPrice(192, 7, PriceSource.FromSniper);
		}});
		
		Message message = new Message();
		
		message.setBody(
				"SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " 
						+ SNIPER_ID + ";");
		translator.processMessage(UNUSED_CHAT, message);
	}
	
}
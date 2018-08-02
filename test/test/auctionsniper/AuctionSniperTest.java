package test.auctionsniper;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import prod.auction.Auction;
import prod.auctionsniper.AuctionSniper;
import prod.auctionsniper.SniperListener;

public class AuctionSniperTest {

	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	@Mock
	private SniperListener sniperListener;
	
	@Mock
	private Auction auction;
		
	private AuctionSniper auctionSniper;
	
	@Before
	public void InitAuctionMessageTranslator() {
		auctionSniper = new AuctionSniper(sniperListener, auction);
	}
	
	@Test public void reportsClosedWhenAuctionCloses() {
		context.checking(new Expectations() {{
			exactly(1).of(sniperListener).sniperLost();
		}});
		
		auctionSniper.auctionClosed();
	}
	
	@Test public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 1001;
		final int increment = 25;
		
		context.checking(new Expectations() {{
			exactly(1).of(auction).bid(price + increment);
			atLeast(1).of(sniperListener).sniperBidding();
		}});
		
		auctionSniper.currentPrice(price, increment);
	}

}

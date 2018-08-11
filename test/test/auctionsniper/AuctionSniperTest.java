package test.auctionsniper;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import prod.auction.Auction;
import prod.auctionsniper.AuctionSniper;
import prod.auctionsniper.SniperListener;
import prod.auctionsniper.SniperListener.PriceSource;
import prod.auctionsniper.SniperSnapshot;
import prod.auctionsniper.SniperState;

public class AuctionSniperTest {
	private final String ITEM_ID = "item-54321";
	
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	@Mock
	private SniperListener sniperListener;
	@Mock
	private Auction auction;
		
	private final States sniperState = context.states("sniper");

	private AuctionSniper auctionSniper;
	
	@Before
	public void InitAuctionMessageTranslator() {
		auctionSniper = new AuctionSniper(sniperListener, auction, ITEM_ID);
	}
	
	@Test public void reportsClosedWhenAuctionClosesImmediately() {
		context.checking(new Expectations() {{
			exactly(1).of(sniperListener).
				sniperStateChanged(with(aSniperThatIs(SniperState.LOST)));
		}});
		
		auctionSniper.auctionClosed();
	}
	
	@Test public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		
		context.checking(new Expectations() {{
			exactly(1).of(auction).bid(price + increment);
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, 
					bid, SniperState.BIDDING));
		}});
		
		auctionSniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
	}
	
	@Test public void reportsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
									then(sniperState.is("bidding"));
			atLeast(1).of(sniperListener).sniperStateChanged(
					new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
									when(sniperState.is("bidding"));
		}});
		auctionSniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		auctionSniper.currentPrice(135, 45, PriceSource.FromSniper);
	}
	
	@Test public void reportsLostIfAuctionClosesWhenBidding() {
		context.checking(new Expectations() {{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
									then(sniperState.is("bidding"));
			atLeast(1).of(sniperListener).sniperStateChanged(
					new SniperSnapshot(ITEM_ID, 123, 168, SniperState.LOST));
									when(sniperState.is("bidding"));
		}});
		auctionSniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
		auctionSniper.auctionClosed();
	}
	
	@Test public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {{
			ignoring(auction);
			allowing(sniperListener).sniperStateChanged(
					with(aSniperThatIs(SniperState.WINNING)));
									then(sniperState.is("winning"));
			atLeast(1).of(sniperListener).sniperStateChanged(
					with(aSniperThatIs(SniperState.WON)));
									when(sniperState.is("winning"));
		}});
		auctionSniper.currentPrice(123, 45, PriceSource.FromSniper);
		auctionSniper.auctionClosed();
	}
	
	private Matcher<SniperSnapshot> aSniperThatIs(final SniperState sniperState) {
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(sniperState), "sniper that is", "was ") {
					@Override
					protected SniperState featureValueOf(SniperSnapshot actual) {
						return actual.state;
					}
			
		};
	}

}

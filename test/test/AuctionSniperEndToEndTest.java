package test;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Test;

import test.util.ApplicationRunner;
import test.util.FakeAuctionServer;

public class AuctionSniperEndToEndTest {
	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();
	
	@Test public void sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@After public void stopAuction() {
		auction.stop();
	}

	@After public void stopApplication() {
		application.stop();
	}
	
}

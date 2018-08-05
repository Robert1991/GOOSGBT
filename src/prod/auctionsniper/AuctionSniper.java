package prod.auctionsniper;

import prod.auction.Auction;
import prod.auction.AuctionEventListener;

public class AuctionSniper implements AuctionEventListener {

	private SniperListener sniperListener;
	private Auction auction;

	public AuctionSniper(SniperListener sniperListener, Auction auction) {
		this.sniperListener = sniperListener;
		this.auction = auction;
	}
	
	@Override
	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int price, int increment) {
		auction.bid(price + increment);
		sniperListener.sniperBidding();
	}

	

}

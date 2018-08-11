package prod.auctionsniper;

import prod.auction.Auction;
import prod.auction.AuctionEventListener;
import prod.auctionsniper.SniperListener.PriceSource;

public class AuctionSniper implements AuctionEventListener {
	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(SniperListener sniperListener, Auction auction, String itemId) {
		this.sniperListener = sniperListener;
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(itemId);
	}
	
	public void notifyJoined() {
		notifyChange();
	}
	
	@Override
	public void auctionClosed() {
		snapshot = snapshot.closed();
		notifyChange();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		switch(source) {
		case FromSniper:
			snapshot = snapshot.winning(price);
			break;
		case FromOtherBidder:
			int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
			break;
		}
		
		notifyChange();
	}

	private void notifyChange() {
		sniperListener.sniperStateChanged(snapshot);
	}
}

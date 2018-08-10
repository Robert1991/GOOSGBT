package prod.auctionsniper;

import prod.auction.Auction;
import prod.auction.AuctionEventListener;
import prod.auctionsniper.SniperListener.PriceSource;

public class AuctionSniper implements AuctionEventListener {
	public enum SniperState {
		JOINING,
		BIDDING,
		WINNING,
		LOST,
		WON;
	}
	
	private boolean isWinning = false;
	private SniperListener sniperListener;
	private Auction auction;
	private SniperSnapshot snapshot;

	public AuctionSniper(SniperListener sniperListener, Auction auction, String itemId) {
		this.sniperListener = sniperListener;
		this.auction = auction;
		this.snapshot = SniperSnapshot.joining(itemId); 
	}
	
	@Override
	public void auctionClosed() {
		if (isWinning) {
			sniperListener.sniperWon();
		} else {
			sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource source) {
		isWinning = PriceSource.FromSniper == source;
		
		if (isWinning) {
			snapshot = snapshot.winning(price);
		} else {			
			int bid = price + increment;
			auction.bid(bid);
			snapshot = snapshot.bidding(price, bid);
		}
		
		sniperListener.sniperStateChanged(snapshot);
	}

	

}

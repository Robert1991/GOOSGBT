package prod.auction;

import prod.auctionsniper.SniperListener.PriceSource;

public interface AuctionEventListener {

	void auctionClosed();

	void currentPrice(int price, int increment, PriceSource fromsniper);

}

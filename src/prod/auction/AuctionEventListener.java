package prod.auction;

public interface AuctionEventListener {

	void auctionClosed();

	void currentPrice(int price, int increment);

}

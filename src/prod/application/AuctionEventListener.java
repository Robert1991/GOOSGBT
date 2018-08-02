package prod.application;

public interface AuctionEventListener {

	void auctionClosed();

	void currentPrice(int price, int increment);

}

package prod.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
	enum PriceSource {
		FromSniper, FromOtherBidder;
	}
	
	void sniperLost();
	
	void sniperWon();

	void sniperStateChanged(SniperSnapshot sniperState);
}

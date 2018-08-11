package prod.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
	enum PriceSource {
		FromSniper, FromOtherBidder;
	}
	
	void sniperStateChanged(SniperSnapshot sniperState);
}

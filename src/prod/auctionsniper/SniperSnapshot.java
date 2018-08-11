package prod.auctionsniper;

public class SniperSnapshot {
	public final String itemId;
	public final int lastPrice;
	public final int lastBid;
	public final SniperState state;
	
	public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState sniperState) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.state = sniperState;
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SniperSnapshot))
			return false;
		else {
			SniperSnapshot otherSniperState = (SniperSnapshot)object;
			return otherSniperState.itemId.equals(itemId) && 
				otherSniperState.lastPrice == lastPrice &&
				otherSniperState.lastBid == lastBid;
			
		}
	}

	public static SniperSnapshot joining(String itemId) {
		return new SniperSnapshot(itemId, 0, 0, SniperState.JOINING);
	}
	
	public SniperSnapshot winning(int newLastPrice) {
		return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
	}

	public SniperSnapshot bidding(int newLastPrice, int lastBid) {
		return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.BIDDING);
	}
	
	public SniperSnapshot closed() {
		return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
	}
	
	@Override
	public String toString() {
		return String.format("ItemId: %s LastPrice: %d LastBid: %d SniperState: %s", 
				itemId, lastPrice, lastBid,
				state.toString());
	}

}

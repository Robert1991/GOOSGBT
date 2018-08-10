package prod.application.ui;

import javax.swing.table.AbstractTableModel;

import prod.auctionsniper.AuctionSniper.SniperState;
import prod.auctionsniper.SniperSnapshot;

public class SniperTableModel extends AbstractTableModel {
	public enum Column {
		ITEM_IDENTIFIER,
		LAST_PRICE,
		LAST_BID,
		SNIPER_STATE;
		
		public static Column at(int offset) {
			return values()[offset];
		}
	}
	
	private static final String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, 
			MainWindow.STATUS_BIDDING, MainWindow.STATUS_WINNING, MainWindow.STATUS_LOST, MainWindow.STATUS_WON };
	
	private final SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private String state = MainWindow.STATUS_JOINING;
	private SniperSnapshot snapshot = STARTING_UP;

	private static final long serialVersionUID = 1L;

	public SniperTableModel() {

	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (Column.at(columnIndex)) {
		case ITEM_IDENTIFIER:
			return snapshot.itemId;
		case LAST_PRICE:
			return snapshot.lastPrice;
		case LAST_BID:
			return snapshot.lastBid;
		case SNIPER_STATE:
			return state;
		default:
			throw new IllegalArgumentException("No column at" + columnIndex);
		}
	}

	public void sniperStatusChanged(SniperSnapshot newSnapshot) {
		this.snapshot = newSnapshot;
		this.state = STATUS_TEXT[newSnapshot.state.ordinal()];
		fireTableRowsUpdated(0,0);
	}
}

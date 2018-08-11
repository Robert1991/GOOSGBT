package prod.application.ui;

import javax.swing.table.AbstractTableModel;

import prod.auctionsniper.SniperSnapshot;
import prod.auctionsniper.SniperState;

public class SniperTableModel extends AbstractTableModel {
	public static final String[] STATUS_TEXT = { "Joining", "Bidding",
			"Winning", "Lost", "Won" };
	
	private final SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
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
		return Column.at(columnIndex).valueIn(snapshot);
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return Column.at(columnIndex).name;
	}
	
	public void sniperStateChanged(SniperSnapshot newSnapshot) {
		this.snapshot = newSnapshot;
		fireTableRowsUpdated(0,0);	
	}

	public static String textFor(SniperState sniperState) {
		return STATUS_TEXT[sniperState.ordinal()];
	}
}

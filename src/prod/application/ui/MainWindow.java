package prod.application.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import prod.auctionsniper.SniperSnapshot;


public class MainWindow extends JFrame {
	private final SniperTableModel snipers = new SniperTableModel();
	private static final long serialVersionUID = 1L;
	
	public static final String MAIN_WINDOW_NAME = "Auction Sniper";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	private static final String SNIPERS_TABLE_NAME = "Snipers";	

	public static final String STATUS_JOINING = "Joining";
	public static final String STATUS_LOST = "Lost";
	public static final String STATUS_BIDDING = "Bidding";
	public static final String STATUS_WINNING = "Winning";
	public static final String STATUS_WON = "Won";
	
	public MainWindow() {
		super("Auction Sniper");
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void fillContentPane(JTable snipersTable) {
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable() {
		final JTable snipersTable = new JTable(snipers);
		snipersTable.setName(SNIPERS_TABLE_NAME);
		return snipersTable;
	}

	public void sniperStatusChanged(SniperSnapshot sniperState) {
		snipers.sniperStatusChanged(sniperState);
	}
}

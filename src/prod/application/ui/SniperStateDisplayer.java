package prod.application.ui;

import javax.swing.SwingUtilities;

import prod.auctionsniper.SniperListener;
import prod.auctionsniper.SniperSnapshot;
import prod.auctionsniper.AuctionSniper.SniperState;

public class SniperStateDisplayer implements SniperListener {
	private MainWindow ui;

	public SniperStateDisplayer(MainWindow ui) {
		this.ui = ui;
	}
	
	@Override
	public void sniperLost() {
		showStatus(new SniperSnapshot("", 0 ,0 , SniperState.LOST));
	}

	@Override
	public void sniperWon() {
		showStatus(new SniperSnapshot("", 0 ,0 , SniperState.WON));
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot sniperState) {
		showStatus(sniperState);
	}
	
	public void showStatus(final SniperSnapshot sniperState) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ui.sniperStatusChanged(sniperState);
			}
		});
	}
}
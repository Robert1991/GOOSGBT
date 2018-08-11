package prod.application.ui;

import javax.swing.SwingUtilities;

import prod.auctionsniper.SniperListener;
import prod.auctionsniper.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener {
	private SniperTableModel snipers;

	public SwingThreadSniperListener(SniperTableModel snipers) {
		this.snipers = snipers;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot sniperState) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				snipers.sniperStateChanged(sniperState);
			}
		});
	}
}
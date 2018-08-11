package test.util;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.CoreMatchers.equalTo;

import javax.swing.table.JTableHeader;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import prod.application.ui.MainWindow;

public class AuctionSniperDriver extends JFrameDriver {

	@SuppressWarnings("unchecked")
	public AuctionSniperDriver(int timeOutMillis) {
		super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
				new AWTEventQueueProber(timeOutMillis, 100));
	}

	@SuppressWarnings("unchecked")
	public void showsSniperStatus(String status) {
		new JTableDriver(this).hasCell(withLabelText(equalTo(status)));
	}

	@SuppressWarnings("unchecked")
	public void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
		new JTableDriver(this).hasRow(matching(withLabelText(equalTo(itemId)), withLabelText(valueOf(lastPrice)),
				withLabelText(valueOf(lastBid)),withLabelText(equalTo(status))));
	}

	private String valueOf(int intValue) {
		return String.valueOf(intValue);
	}

	@SuppressWarnings("unchecked")
	public void hasColumnTitles() {
		JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
		headers.hasHeaders(matching(withLabelText("Item"),
				withLabelText("Last Price"),withLabelText("Last Bid"),
				withLabelText("State")));
	}

}

package test.util;

import static org.hamcrest.CoreMatchers.*;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import prod.application.MainWindow;

public class AuctionSniperDriver extends JFrameDriver {

	public AuctionSniperDriver(int timeOutMillis) {
		super(new GesturePerformer(), JFrameDriver.topLevelFrame(named(MainWindow.MAIN_WINDOW_NAME), showingOnScreen()),
				new AWTEventQueueProber(timeOutMillis, 100));
	}

	public void showsSniperStatus(String status) {
		JLabelDriver jLabelDriver = new JLabelDriver(this, named(MainWindow.SNIPER_STATUS_NAME));
		jLabelDriver.hasText(equalTo(status));
	}

}

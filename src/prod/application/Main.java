package prod.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import prod.application.ui.MainWindow;
import prod.application.ui.SniperTableModel;
import prod.application.ui.SwingThreadSniperListener;
import prod.auction.Auction;
import prod.auction.AuctionMessageTranslator;
import prod.auction.XMPPAuction;
import prod.auctionsniper.AuctionSniper;

public class Main {
	@SuppressWarnings("unused")
	private Chat notToBeGarbageCollected;
	private static final int ARG_HOSTNAME = 0;
	private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID = 3;

	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Bid; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: Join;";
	
	private final SniperTableModel snipers = new SniperTableModel();
	
	private MainWindow ui;

	public Main() throws Exception {
		startUserInterface();
	}

	public static void main(String... args) throws Exception {
		Main main = new Main();
		main.joinAuction(connection(args), args[ARG_ITEM_ID]);
	}

	private void joinAuction(XMPPConnection connection, String itemId) throws Exception {
		disconnectWhenUICloses(connection);

		Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);

		Auction auction = new XMPPAuction(chat);
		AuctionSniper auctionSniper = new AuctionSniper(new SwingThreadSniperListener(snipers), auction, itemId);
		chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(),
				auctionSniper));
		this.notToBeGarbageCollected = chat;
		auction.join();
		auctionSniper.notifyJoined();
	}

	private void disconnectWhenUICloses(XMPPConnection connection) {
		ui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				connection.disconnect();
			}
		});
	}

	private static XMPPConnection connection(String... args) throws XMPPException {
		XMPPConnection connection = connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		return connection;
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				ui = new MainWindow(snipers);
			}
		});
	}

	private static XMPPConnection connectTo(String hostname, String username, String password) throws XMPPException {
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE);
		return connection;
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}

	

}

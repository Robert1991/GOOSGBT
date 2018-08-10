package prod.auction;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import prod.application.Main;

public class XMPPAuction implements Auction {
		private final Chat chat;
		
		public XMPPAuction(Chat chat) {
			this.chat = chat;
		}

		@Override
		public void bid(int amount) {
			sendMessage(format(Main.BID_COMMAND_FORMAT, amount));
		}

		public void join() {
			sendMessage(Main.JOIN_COMMAND_FORMAT);
		}

		private void sendMessage(final String message) {
			try {
				chat.sendMessage(message);
			} catch (XMPPException exc) {
				exc.printStackTrace();
			}
		}

		private String format(String format, Object... args) {
			return String.format(format, args);
		}
	}
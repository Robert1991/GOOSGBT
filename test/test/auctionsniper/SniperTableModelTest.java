package test.auctionsniper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import prod.application.ui.Column;
import prod.application.ui.MainWindow;
import prod.application.ui.SniperTableModel;
import prod.auctionsniper.SniperSnapshot;
import prod.auctionsniper.SniperState;

public class SniperTableModelTest {
	@Rule
	public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	@Mock
	private TableModelListener listener = context.mock(TableModelListener.class);
	
	private final SniperTableModel model = new SniperTableModel();
	
	@Before
	public void attachModelListener() {
		model.addTableModelListener(listener);
	}
	
	@Test
	public void hasEnoughColumns() {
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test
	public void setSniperValuesInColumns() {
		context.checking(new Expectations() {{
			exactly(1).of(listener).tableChanged(with(aRowChangedEvent()));
		}});
		
		model.sniperStateChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));
		
		assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
		assertColumnEquals(Column.LAST_PRICE, 555);
		assertColumnEquals(Column.LAST_BID, 666);
		assertColumnEquals(Column.SNIPER_STATE, MainWindow.STATUS_BIDDING);
	}
	
	@Test
	public void setsUpAllColumns() {
		for(Column col : Column.values())
			assertEquals(col.name, model.getColumnName(col.ordinal()));
	}
	
	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0;
		final int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}

	private Matcher<TableModelEvent> aRowChangedEvent() {
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
}

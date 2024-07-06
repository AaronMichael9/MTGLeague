import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class StandingsTab extends Tab {

	private ArrayList<Deck> decks;
	private JTextPane content;
	private JFrame focus;
	private boolean isShort = true;
	private KeyListener listener;

	public StandingsTab(String statsPath, JFrame focus) {
		// System.out.println("starting StandingsTab");
		this.focus = focus;
		this.setLayout(new BorderLayout());
		retrieveStandings(statsPath);
		generateList();
		generateListener();

	}

	private void retrieveStandings(String statsPath) {
		DeckManager dm = new DeckManager(statsPath);
		decks = dm.retrieveStandings();
	}

	private void generateList() {
		content = new JTextPane();
		content.setEditable(false);
		content.setFocusable(false);
		JScrollPane list = new JScrollPane(content);
		list.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		list.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(list);
		generateContentShort();
	}

	private void generateContentShort() {
		decks.sort(new Comparator<Deck>() {
			@Override
			public int compare(Deck o1, Deck o2) {
				if (o1.getMMR() > o2.getMMR())
					return -1;
				if (o1.getMMR() < o2.getMMR())
					return 1;
				return 0;
			}
		});
		String build = "";
		for (Deck deck : decks)
			if (deck.getEnabled())
				build += deck.getName() + "\n";
		content.setText(build);
	}

	private void generateContentLong() {
		decks.sort(new Comparator<Deck>() {
			@Override
			public int compare(Deck o1, Deck o2) {
				if (o1.getMMR() > o2.getMMR())
					return -1;
				if (o1.getMMR() < o2.getMMR())
					return 1;
				return 0;
			}
		});
		String build = "";
//		int total = 0;
		for (Deck deck : decks)
			if (deck.getEnabled()) {
				build += String.format("%04d |%s\n", deck.getMMR(), deck.getName());
//				total+=deck.getMMR();
			}
//		System.out.println("total: "+total);
		content.setText(build);
	}

	private void generateListener() {
		listener = new InputListener();
		focus.addKeyListener(listener);
	}

	private class InputListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyChar() == ' ')
				toggleSpace();
		}

	}

	private void toggleSpace() {
		if (isShort)
			generateContentLong();
		else
			generateContentShort();
		isShort = !isShort;
	}

	public void close() {
		focus.removeKeyListener(listener);
	}
}

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GameSetupTab extends Tab {
	private ArrayList<Deck> outdecks;
	public ArrayList<Deck> indecks;
	private JList<String> left;
	private JList<String> right;
	private boolean rated;
	private String statsPath;
	private DeckSuggester ds;

	public GameSetupTab(String statsPath, ActionListener swapTrigger) {
		this.statsPath = statsPath;
		this.setLayout(new BorderLayout());
		retrieveDecks();
		generateLists();
		generateButtons(swapTrigger);
		ds = new RandomDeckSuggester();
	}

	private void retrieveDecks() {
		DeckManager dm = new DeckManager(statsPath);
		outdecks = dm.retrieveStandings();
		indecks = new ArrayList<>();
		for (int i = 0; i < outdecks.size(); i++)
			if (!outdecks.get(i).getEnabled()) {
				outdecks.remove(i);
				i--;
			}
	}

	private void generateLists() {
		rated = false;
		Container center = new Container();
		this.add(center, BorderLayout.CENTER);
		center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
		left = new JList<>();
		right = new JList<>();
		left.setFocusable(false);
		right.setFocusable(false);
		JScrollPane scrollLeft = new JScrollPane(left);
		scrollLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollPane scrollRight = new JScrollPane(right);
		scrollRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		Container containerL = new Container();
		containerL.setLayout(new BoxLayout(containerL, BoxLayout.Y_AXIS));
		Container containerR = new Container();
		containerR.setLayout(new BoxLayout(containerR, BoxLayout.Y_AXIS));
		containerL.add(new JLabel("roster"));
		containerR.add(new JLabel("participants"));
		containerL.add(scrollLeft);
		containerR.add(scrollRight);
		center.add(containerL);
		center.add(containerR);
		populateLeft();
		populateRight();
		left.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				onLeftSelection();
			}
		});
		right.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				onRightSelection();
			}
		});
	}

	private void populateLeft() {
		String[] names = new String[outdecks.size()];
		outdecks.sort(new DeckComparator());
		for (int i = 0; i < outdecks.size(); i++)
			if (!rated)
				names[i] = outdecks.get(i).getName();
			else
				names[i] = String.format("%04d|%s", outdecks.get(i).getMMR(), outdecks.get(i).getName());
		left.setListData(names);
	}

	private void populateRight() {
		String[] names = new String[indecks.size()];
		indecks.sort(new DeckComparator());
		for (int i = 0; i < indecks.size(); i++)
			if (!rated)
				names[i] = indecks.get(i).getName();
			else
				names[i] = String.format("%04d|%s", indecks.get(i).getMMR(), indecks.get(i).getName());
		right.setListData(names);
	}

	private void onLeftSelection() {
		int[] selected = left.getSelectedIndices();
		for (int i = selected.length - 1; i >= 0; i--)
			indecks.add(outdecks.remove(selected[i]));
		populateLeft();
		populateRight();
	}

	private void onRightSelection() {
		int[] selected = right.getSelectedIndices();
		for (int i = selected.length - 1; i >= 0; i--)
			outdecks.add(indecks.remove(selected[i]));
		populateLeft();
		populateRight();
	}

	private Checkbox ratedBox;

	private void generateButtons(ActionListener swapTrigger) {
		Container container = new Container();
		container.setLayout(new FlowLayout());
		ratedBox = new Checkbox();
		ratedBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				toggleSpace();
			}
		});

		container.add(ratedBox);
		container.add(new JLabel("show ratings"));
		JButton suggestion = new JButton("Suggest Deck");
		suggestion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				suggestDeck();
			}
		});
		JButton start = new JButton("Start Game");
		start.addActionListener(swapTrigger);
		container.add(suggestion);
		this.add(container, BorderLayout.NORTH);
		this.add(start, BorderLayout.SOUTH);
	}

	private void suggestDeck() {
		ArrayList<Deck> suggs = ds.suggestDeck(outdecks, indecks);
		for (Deck deck : suggs) {
			if (!outdecks.contains(deck))
				System.out.println("ERROR: suggested deck (" + deck.getName() + ") is in use or not in roster");
			else {
				outdecks.remove(deck);
				indecks.add(deck);
			}
		}
		populateLeft();
		populateRight();
	}

	private class DeckComparator implements Comparator<Deck> {
		@Override
		public int compare(Deck o1, Deck o2) {
			if (o1.getMMR() > o2.getMMR())
				return -1;
			if (o1.getMMR() < o2.getMMR())
				return 1;
			return 0;
		}
	}

	private void toggleSpace() {
		rated = ratedBox.getState();
		populateLeft();
		populateRight();
	}

	public void close() {
	}
}

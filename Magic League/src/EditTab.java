import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class EditTab extends Tab {

	private ArrayList<Deck> decks;
	private ArrayList<Checkbox> boxes;
	private ArrayList<JTextField> names;
	private Container containerList;
	private String statsPath;

	public EditTab(String statsPath) {
		this.setLayout(new BorderLayout());
		this.statsPath = statsPath;
		retrieveDecks(statsPath);
		generateMiddle();
		generateBottom();

	}

	private void generateMiddle() {
		// this.setBackground(Color.WHITE);
		containerList = new Container();
		JScrollPane scroll = new JScrollPane(containerList);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		int n = decks.size();
		boxes = new ArrayList<Checkbox>();
		names = new ArrayList<JTextField>();
		containerList.setLayout(new BoxLayout(containerList, BoxLayout.Y_AXIS));
		for (int i = 0; i < n; i++) {
			Container containerI = new Container();
			containerI.setLayout(new BoxLayout(containerI, BoxLayout.X_AXIS));
			Checkbox box = new Checkbox();
			JTextField name = new JTextField();

			boxes.add(box);
			box.setState(decks.get(i).getEnabled());
			names.add(name);
			name.setText(decks.get(i).getName());
			if (!box.getState())
				name.setBackground(this.getBackground());
			box.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (box.getState())
						name.setBackground(Color.WHITE);
					else
						name.setBackground(box.getBackground());
				}
			});

			containerI.add(box);
			containerI.add(name);
			containerList.add(containerI);
		}
		this.add(scroll, BorderLayout.CENTER);
	}

	private void generateBottom() {
		Container container = new Container();
		//container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.setLayout(new FlowLayout());
		JButton buttonAdd = new JButton("Add Deck");
		JButton buttonSave = new JButton("Save");
		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addDeck();
			}

		});
		buttonSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		this.add(container, BorderLayout.SOUTH);
		container.add(buttonAdd);
		container.add(buttonSave);
		buttonAdd.setPreferredSize(new Dimension(100,30));
		buttonSave.setPreferredSize(new Dimension(300, 30));
	}

	private void retrieveDecks(String statsPath) {
		DeckManager dm = new DeckManager(statsPath);
		decks = dm.retrieveStandings();
	}

	private void updateOrCreateDecks() {
		int n = decks.size();
		int q = boxes.size();
		for (int i = 0; i < n; i++) {
			decks.get(i).setName(names.get(i).getText());
			decks.get(i).setEnabled(boxes.get(i).getState());
		}
		for(int i=n;i<q;i++) {
			String name = names.get(i).getText();
			if(name!=null && name.trim().length()>0) {
				int id = DeckManager.nextID(decks,i+1);
				decks.add(new Deck(id,name,0,null,boxes.get(i).getState(),DeckManager.DEFAULT_MMR));
			}
		}
	}

	private void addDeck() {
		Container containerI = new Container();
		containerI.setLayout(new BoxLayout(containerI, BoxLayout.X_AXIS));
		Checkbox box = new Checkbox();
		box.setState(true);
		JTextField name = new JTextField();
		boxes.add(box);
		names.add(name);
		box.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (box.getState())
					name.setBackground(Color.WHITE);
				else
					name.setBackground(box.getBackground());
			}
		});
		containerI.add(box);
		containerI.add(name);
		containerList.add(containerI);
		this.revalidate();
	}

	private void save() {
		updateOrCreateDecks();
		DeckManager dm = new DeckManager(statsPath);
		dm.saveStandings(decks);
	}

	public void close() {

	}
}

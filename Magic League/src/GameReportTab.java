import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class GameReportTab extends Tab {
	ArrayList<Deck> decks;
	ActionListener swapTriggerReport;
	ActionListener swapTriggerCancel;
	ArrayList<JComboBox<Integer>> inputs;

	public GameReportTab(ArrayList<Deck> decks, ActionListener swapTriggerReport,ActionListener swapTriggerCancel) {
		this.decks = decks;
		this.swapTriggerReport = swapTriggerReport;
		this.swapTriggerCancel = swapTriggerCancel;
		this.setLayout(new BorderLayout());
		generateTop();
		generateMiddle();
		generateButton();
	}

	private void generateTop() {
		Container container = new Container();
		container.setLayout(new FlowLayout());
		JLabel label = new JLabel("Game in Progress. Please Report Results.");
		label.setAlignmentX(CENTER_ALIGNMENT);
		container.add(label);
		this.add(container,BorderLayout.NORTH);
	}

	private void generateMiddle() {
		inputs = new ArrayList<>();
		Container sizeAdjuster = new Container();
		sizeAdjuster.setLayout(new FlowLayout());
		Container container = new Container();
		JScrollPane pane = new JScrollPane(container);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		Integer[] answers = new Integer[decks.size()];
		for(int i=1;i<=decks.size();i++)
			answers[i-1] = i;
		for (Deck deck : decks) {
			Container containerN = new Container();
			containerN.setLayout(new FlowLayout(FlowLayout.LEFT));
			JComboBox<Integer> box = new JComboBox<>(answers);
			containerN.add(box);
			containerN.add(new JLabel(deck.getName()));
			inputs.add(box);
			container.add(containerN);
		}
		sizeAdjuster.add(pane);
		this.add(sizeAdjuster, BorderLayout.CENTER);
	}

	private void generateButton() {
		JButton report = new JButton("Report Results");
		report.addActionListener(swapTriggerReport);
		JButton cancel = new JButton("Cancel Game");
		cancel.addActionListener(swapTriggerCancel);
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(report);
		container.add(cancel);
		this.add(container, BorderLayout.SOUTH);
	}

	@Override
	public void close() {
	}

}

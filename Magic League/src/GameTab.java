import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameTab extends Tab {

	private String statsPath;
	private String historyPath;
	GameSetupTab setup;
	GameReportTab report;
	private final Commissioner comm = new AnteCommissioner();

	public GameTab(String statsPath, String historyPath) {
		this.statsPath = statsPath;
		this.historyPath = historyPath;
		this.setLayout(new BorderLayout());
		HistoryManager hm = new HistoryManager(historyPath);
		String current = hm.retrieveCurrentGame();
		if (current == null) {
			setup = new GameSetupTab(statsPath, new SwapListener(Direction.STARTGAME));
			this.add(setup, BorderLayout.CENTER);
		} else {
			DeckManager dm = new DeckManager(statsPath);
			ArrayList<Deck> decks = dm.decipherCurrentGame(current);
			report = new GameReportTab(decks, new SwapListener(Direction.REPORTGAME),
					new SwapListener(Direction.CANCELGAME));
			this.add(report, BorderLayout.CENTER);
			this.revalidate();
		}
	}

	private void swapToGame() {
		ArrayList<Deck> decks = setup.indecks;
		if (decks == null || decks.size() < 2) {
			JOptionPane.showMessageDialog(this, "Please select at least 2 decks");
			return;
		}
		setup.close();
		this.remove(setup);
		HistoryManager hm = new HistoryManager(historyPath);
		hm.writeCurrentGame(decks);
		report = new GameReportTab(decks, new SwapListener(Direction.REPORTGAME),
				new SwapListener(Direction.CANCELGAME));
		this.add(report, BorderLayout.CENTER);
		this.revalidate();
	}

	private void reportResult() {
		ArrayList<Integer> results = new ArrayList<>();
		ArrayList<Deck> decks = report.decks;
		for(JComboBox<Integer> combo:report.inputs)
			results.add((Integer) combo.getSelectedItem());
		comm.judgeGame(decks, results);
		DeckManager dm = new DeckManager(statsPath);
		dm.updateMMR(decks);
		HistoryManager hm = new HistoryManager(historyPath);
		hm.writeToHistory(decks, results);
		report.close();
		this.remove(report);
		setup = new GameSetupTab(statsPath, new SwapListener(Direction.STARTGAME));
		this.add(setup, BorderLayout.CENTER);
		this.revalidate();
	}

	private void cancelGame() {
		HistoryManager hm = new HistoryManager(historyPath);
		hm.writeCurrentGame(null);
		report.close();
		this.remove(report);
		setup = new GameSetupTab(statsPath, new SwapListener(Direction.STARTGAME));
		this.add(setup, BorderLayout.CENTER);
		this.revalidate();
	}

	public enum Direction {
		STARTGAME, REPORTGAME, CANCELGAME
	}

	private class SwapListener implements ActionListener {

		private Direction dir;

		public SwapListener(Direction dir) {
			this.dir = dir;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (dir == Direction.STARTGAME)
				swapToGame();
			else if (dir == Direction.REPORTGAME)
				reportResult();
			else if (dir == Direction.CANCELGAME)
				cancelGame();
		}

	}

	public void close() {
	}
}

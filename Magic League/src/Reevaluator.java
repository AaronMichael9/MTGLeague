import java.util.ArrayList;
import java.util.Collections;

public class Reevaluator {

	private static final String statsPath = "League1/stats.txt";
	private static final String historyPath = "League1/history.txt";
	private static final Commissioner algorithm = new AnteCommissioner();

	// WARNING: This will reset all MMR and games played
	// WARNING: Then evaluate all games on record based on the specified
	// Commissioner
	public static void main(String[] args) {
		DeckManager dm = new DeckManager(statsPath);
		ArrayList<Deck> decks = dm.retrieveStandings();
		for (Deck deck : decks) {
			deck.setMMR(1000);
			deck.setGamesPlayed(0);
		}
		HistoryManager hm = new HistoryManager(historyPath);
		ArrayList<String> lines = hm.readHistory();
		Collections.reverse(lines);
		for (String line : lines) {
			if (line.trim().length() != 0) {
				ArrayList<Deck> pod = hm.extractDecksFromHistory(line, decks);
				ArrayList<Integer> results = hm.exrtactResultsFromHistory(line);
				algorithm.judgeGame(pod, results);
			}
		}
		dm.updateMMR(decks);
	}
}

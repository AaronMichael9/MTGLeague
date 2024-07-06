
import java.util.ArrayList;

public class RandomDeckSuggester implements DeckSuggester {

	//returns 1 suggested deck based on unweighted RNG
	@Override
	public ArrayList<Deck> suggestDeck(ArrayList<Deck> roster, ArrayList<Deck> participants) {
		ArrayList<Deck> decks = new ArrayList<>();
		if(roster==null || roster.isEmpty())
			return decks;
		decks.add(roster.get(random(roster.size())));
		return decks;
	}
	
	//returns random integer from [0,n)
	private int random(int n) {
		return (int) Math.floor(n*Math.random());
	}

}

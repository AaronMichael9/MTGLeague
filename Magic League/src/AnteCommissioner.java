import java.util.ArrayList;

public class AnteCommissioner implements Commissioner {

	final double ante = 35;
	final double scale = 1.01;
	final double[][] payoff = { { 0, 1 }, { 0, 0.20, 0.8 }, { 0, 0.1, 0.3, 0.6 }, { 0, 0.05, 0.15, 0.25, 0.55 },
			{ 0, 0.05, 0.12, 0.18, 0.25, 0.4 } };

	@Override
	public void judgeGame(ArrayList<Deck> decks, ArrayList<Integer> results) {
		ArrayList<Integer> offset = getOffsets(decks, results);
		for (int i = 0; i < decks.size(); i++) {
			decks.get(i).setGamesPlayed(decks.get(i).getGamesPlayed() + 1);
			decks.get(i).setMMR(decks.get(i).getMMR() + offset.get(i));
		}
	}

	// returns mmr change for player at index 0
	// algorithm charges players varying amounts of points based on mmr relative to
	// group
	// then rewards points based on position. Consequently, some positions can be
	// either
	// a net mmr gain or loss based on relative mmr.
	// limitations:
	// not intended for more than 6 players
	// not intended for multiple players marked as first
	// multiple players marked in places other than first will give significant
	// points to first
	//representing a tie with the lower placement vs the higher placement is a
	//non-trivial distinction
	public ArrayList<Integer> getOffsets(ArrayList<Deck> decks, ArrayList<Integer> results) {
		ArrayList<Integer> antes = new ArrayList<>();
		ArrayList<Integer> offsets = new ArrayList<>();
		int gmmr = 0;
		int size = decks.size();
		for (Deck deck : decks)
			gmmr += deck.getMMR();
		gmmr /= size;
		int totalAnte = 0;
		for (Deck deck : decks) {
			int a = (int) (ante * Math.pow(scale, (deck.getMMR() - gmmr) / 10.0));
			totalAnte += a;
			antes.add(a);
		}
		double totalPrc = 0;
		for(int i=0;i<size;i++)
			totalPrc += payoff[size-2][size-results.get(i)];
		
		int totalOffset = 0;
		for (int i = 0; i < size; i++) {
			double prc = payoff[decks.size() - 2][size - results.get(i)];
			int offset = (int) (totalAnte * prc/totalPrc - antes.get(i));
			offsets.add(offset);
			totalOffset += offset;
		}
		if (totalOffset != 0) {
			for (int i = 0; i < size; i++) {
				if(results.get(i)==1) {
					offsets.set(i, offsets.get(i)-totalOffset);
					return offsets;
				}
			}
			System.out.println("unable to reconcile "+totalOffset+" points surplus");
		}
		return offsets;
	}
}

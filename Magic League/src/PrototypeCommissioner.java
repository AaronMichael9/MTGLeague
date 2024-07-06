import java.util.ArrayList;

public class PrototypeCommissioner implements Commissioner {
	
	private int[][] payoffs = {{},{},{-80,80},{-90,-10,100},{-100,-30,10,120},{-100,-50,-10,30,130},{-110,-60,-30,0,60,140}};
	
	
	//uses static payoff matrix then adjusts individual payoffs based on the players mmr
	//relative to the game's mean. When every player has equal mmr and no ties exist then
	//net mmr gain is zero. The bias towards lower mmr players may result in either a net
	//gain or a net loss of mmr
	
	@Override
	public void judgeGame(ArrayList<Deck> decks, ArrayList<Integer> results) {
		ArrayList<Integer> offset = new ArrayList<>();
		for(int i=0;i<decks.size();i++) {
			offset.add(judgePlayer(decks,results));
			decks.add(decks.remove(0));
			results.add(results.remove(0));
		}
		for(int i=0;i<decks.size();i++) {
			decks.get(i).setGamesPlayed(decks.get(i).getGamesPlayed()+1);
			decks.get(i).setMMR(decks.get(i).getMMR()+offset.get(i));
		}
	}
	//returns mmr change for player at index 0
	//all games with more than 6 players adjust to fit the payoff for 6 players
	public int judgePlayer(ArrayList<Deck> decks,ArrayList<Integer> results) {
		final double SCALE = 1.01;
		int gmmr = 0;
		for(Deck deck:decks)
			gmmr+=deck.getMMR();
		gmmr/=decks.size();
		double diff = (decks.get(0).getMMR()-gmmr)/10.0;
		int place = decks.size()-results.get(0);
		int size = decks.size();
		if(size>6) {
			place = (int) Math.floor((place+1.0)*6.0/size);
			size = 6;
		}
		int payoff = payoffs[size][place];
		if(payoff>0)
			return (int) (payoff*Math.pow(SCALE,-diff));
		else
			return (int) (payoff*Math.pow(SCALE, diff));
	}

}

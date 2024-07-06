import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DeckManager {
	public static final int DEFAULT_MMR = 1000;
	private String statsPath;

	public DeckManager(String statsPath) {
		this.statsPath = statsPath;
	}

	public ArrayList<Deck> retrieveStandings() {
		ArrayList<Deck> decks = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(statsPath));
			String line = reader.readLine();
			while (line != null) {
				Deck deck = decodeLine(line);
				decks.add(deck);
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("exception while reading standings\ndefaulting to empty league");
			//e.printStackTrace();
		}
		return decks;
	}

	public void saveStandings(ArrayList<Deck> decks) {
		try {
			FileWriter writer = new FileWriter(statsPath);
			for (Deck deck : decks) {
				String line = encodeLine(deck);
				writer.append(line);
			}
			writer.close();
			//System.out.println("saving complete");
		} catch (IOException e) {
			System.out.println("exception while writing standings");
			e.printStackTrace();
		}
	}

	private String encodeLine(Deck deck) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		String date = "N/A";
		if (deck.getLastPlayed() != null)
			date = df.format(deck.getLastPlayed());
		String enabled = "ENABLED";
		if (!deck.getEnabled())
			enabled = "DISABLED";
		String line = String.format("%02d|%s|%d|%s|%s|%d\n", deck.getID(), deck.getName(), deck.getGamesPlayed(), date,
				enabled, deck.getMMR());
		return line;
	}

	private Deck decodeLine(String line) {
		String[] parts = line.split("\\|");
		int id = Integer.parseInt(parts[0]);
		String name = parts[1];
		int gamesPlayed = Integer.parseInt(parts[2]);
		String lastPlayedString = parts[3];
		boolean enabled = parts[4].equals("ENABLED");
		int mmr = Integer.parseInt(parts[5]);
		Date date = null;
		if (!lastPlayedString.equals("N/A") && !lastPlayedString.equals("")) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			try {
				date = df.parse(lastPlayedString);
			} catch (ParseException e) {
				System.out.println("failed to retrieve last played for " + name);
			}
		}
		return new Deck(id, name, gamesPlayed, date, enabled, mmr);
	}
	public ArrayList<Deck> decipherCurrentGame(String game){
		ArrayList<Deck> roster = retrieveStandings();
		ArrayList<Deck> decks = new ArrayList<>();
		String[] parts = game.split("\n");
		Set<Integer> seen = new HashSet<>();
		for(String part:parts) {
			int n = part.indexOf('|');
			n = Integer.parseInt(part.substring(0, n));
			for(Deck deck:roster)
				if(deck.getID()==n) {
					decks.add(deck);
					break;
				}
		}
		return decks;
	}
	public void updateMMR(ArrayList<Deck> decks) {
		ArrayList<Deck> roster = retrieveStandings();
		for(Deck r:roster)
			for(Deck d:decks) 
				if(r.getID()==d.getID()) {
					r.setMMR(d.getMMR());
					r.setLastPlayed(new Date());
					r.setGamesPlayed(d.getGamesPlayed());
				}
		saveStandings(roster);
	}
	
	
	
	public static int nextID(ArrayList<Deck> decks,int id) {
		Set<Integer> seen = new HashSet<>();
		for(Deck deck:decks) {
			if(seen.contains(deck.getID()))
				System.out.println("ERROR : MULTIPLE DECKS DETECTED WITH ID "+deck.getID());
			seen.add(deck.getID());
		}
		while(seen.contains(id))
			id++;
		return id;
	}
	
}

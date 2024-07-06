import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.text.DateFormatter;

public class HistoryManager {

	private final String DELIMITER = "*****";

	private String historyPath;

	public HistoryManager(String historyPath) {
		this.historyPath = historyPath;
	}

	// returns null if no current game
	public String retrieveCurrentGame() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(historyPath));
			String line = reader.readLine();
			String total = "";
			while (line != null && !line.equals("") && !line.equals(DELIMITER)) {
				total += line+"\n";
				line = reader.readLine();
			}
			reader.close();
			if (total.equals("NO GAME IN PROGRESS\n"))
				return null;
			return total;
		} catch (IOException e) {
			System.out.println("ERROR while looking for current game");
			// e.printStackTrace();
		}
		return null;
	}
	//erases current game if decks is null
	public void writeCurrentGame(ArrayList<Deck> decks) {
		String total = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(historyPath));
			String line = reader.readLine();
			while(line!=null && !line.equals(DELIMITER))
				line = reader.readLine();
			line = reader.readLine();
			while(line!=null) {
				total+=line+"\n";
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			System.out.println("ERROR while reading history");
		}
		try {
			FileWriter writer = new FileWriter(historyPath);
			if(decks!=null) 
				for(Deck deck:decks)
					writer.write(String.format("%02d|%s\n",deck.getID(),deck.getName()));
			else
				writer.write("NO GAME IN PROGRESS\n");
			writer.write(DELIMITER+"\n");
			writer.write(total);
			writer.close();
		}
		catch(IOException e) {
			System.out.println("ERROR while writing current game");
		}
	}
	public void writeToHistory(ArrayList<Deck> decks,ArrayList<Integer> results){
		String total = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(historyPath));
			String line = reader.readLine();
			while(line!=null && !line.equals(DELIMITER))
				line = reader.readLine();
			line = reader.readLine();
			while(line!=null) {
				total+=line+"\n";
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException e) {
			System.out.println("ERROR while reading history");
		}
		try {
			FileWriter writer = new FileWriter(historyPath);
			writer.write("NO GAME IN PROGRESS\n");
			writer.write(DELIMITER+"\n");
			writer.write("[");
			for(int i=0;i<decks.size();i++) {
				writer.write(String.format("{%d,%02d,%s}", results.get(i),decks.get(i).getID(),decks.get(i).getName()));
			}
			writer.write("]");
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			writer.write(df.format(new Date()));
			writer.write("\n");
			writer.write(total);
			writer.close();
		}
		catch(IOException e) {
			System.out.println("ERROR while writing history");
		}
	}
	public ArrayList<String> readHistory(){
		ArrayList<String> lines = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(historyPath));
			String line = reader.readLine();
			while(line!=null && !line.equals(DELIMITER)) {
				line = reader.readLine();
			}
			line = reader.readLine();
			while(line!=null) {
				lines.add(line);
				line = reader.readLine();
			}
		}
		catch(IOException e) {
			System.out.println("ERROR while retrieving history");
		}
		return lines;
	}
	//assumes standard formatting of history
	public ArrayList<Deck> extractDecksFromHistory(String line,ArrayList<Deck> decks){
		ArrayList<Deck> answer = new ArrayList<>();
		int a = line.indexOf('{');
		while(a!=-1) {
			int b = line.indexOf(',', a);
			int c = line.indexOf(',',b+1);
			int n = Integer.parseInt(line.substring(b+1,c));
			for(Deck deck:decks)
				if(deck.getID()==n)
					answer.add(deck);
			a = line.indexOf('{',a+1);
		}
		return answer;
	}
	//assumes standard formatting of history
	public ArrayList<Integer> exrtactResultsFromHistory(String line){
		ArrayList<Integer> answer = new ArrayList<>();
		int a = line.indexOf('{');
		while(a!=-1) {
			int b = line.indexOf(',',a);
			int n = Integer.parseInt(line.substring(a+1,b));
			answer.add(n);
			a = line.indexOf('{',a+1);
		}
		return answer;
	}
}
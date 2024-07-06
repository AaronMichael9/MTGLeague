import java.util.Date;

public class Deck {
	
	//"ID|NAME|GAMES PLAYED|LAST PLAYED|ENABLED|MMR"
	
	private int id;
	private String name;
	private int gamesPlayed;
	private Date lastPlayed;
	//	ENABLED/DISABLED
	private boolean enabled;
	private int mmr;
	
	public Deck(int id,String name,int gamesPlayed,Date lastPlayed,boolean enabled,int mmr) {
		this.id = id;
		this.name = name;
		this.gamesPlayed = gamesPlayed;
		this.lastPlayed = lastPlayed;
		this.enabled = enabled;
		this.mmr = mmr;
	}
	public int getID() {return id;}
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public void setGamesPlayed(int g) {this.gamesPlayed = g;}
	public int getGamesPlayed() {return gamesPlayed;}
	public void setLastPlayed(Date date) {lastPlayed = date;}
	public Date getLastPlayed() {return lastPlayed;}
	public void setEnabled(boolean enabled) {this.enabled = enabled;}
	public boolean getEnabled() {return enabled;}
	public void setMMR(int mmr) {this.mmr=mmr;}
	public int getMMR() {return mmr;}
}

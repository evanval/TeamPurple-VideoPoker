package team_purple.final_video_poker;

public class Card {
	private String rank;
	private String suit;
	private boolean held;
	
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public String toString() {
		return rank + " of " + suit;
	}

	public boolean isHeld() {
		return held;
	}
	public void setHeld(boolean held) {
		this.held = held;
	}
}

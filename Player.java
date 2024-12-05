import java.util.ArrayList;
import java.util.List;


public class Player {
	private List<Card> hand;
	
	public Player() {
		hand = new ArrayList<>();
	}
	
	public List<Card> getHand() {
		return hand;
	}
	
	public void setHand(List<Card> hand) {
		this.hand = hand;
	}
	
	public void displayHand() {
		System.out.println("Your Hand is: ");
		for (Card card : hand) {
			System.out.println(card);
		}
		System.out.println((""));
	}
}

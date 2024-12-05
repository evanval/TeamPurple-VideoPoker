package team_purple.final_video_poker;

public class Bank {
	private int bankroll;
	
	public Bank(int initialBankroll) {
		this.bankroll = initialBankroll;
	}
	
	public int getBankroll() {
		return bankroll;
	}
	
	public void addWinnings(int amount) {
		bankroll += amount;
	}
	
	public boolean placeBet(int bet) {
		if (bet <= bankroll) {
			bankroll -= bet;
			return true;
		}
		return false;
	}
	
	public void printBankroll() {
		System.out.println("Your bankroll is: " + bankroll);
	}
}

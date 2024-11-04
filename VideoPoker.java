
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VideoPoker {

    // Method to generate a deck of 52 cards
    public static List<Card> generateDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        List<Card> deck = new ArrayList<>();

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(new Card(rank, suit));
            }
        }

        return deck;
    }

    // Method to shuffle the deck
    public static void shuffleDeck(List<Card> deck) {
        Collections.shuffle(deck, new Random());
    }

    // Method to deal cards (5 cards for video poker)
    public static List<Card> dealHand(List<Card> deck) {
        return new ArrayList<>(deck.subList(0, 5)); // Returns the first 5 cards
    }

    // Simple method to determine if a hand qualifies for "Jacks or Better"
    public static String evaluateHand(List<Card> hand) {
        // TODO: Implement a more complete hand ranking system
        return "No winning hand"; // For now, returns no winning hand
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize the game components
        Player player = new Player();
        Bank bank = new Bank(100); // Start with 100 credits

        System.out.println("Welcome to Video Poker! Your starting bankroll is 100 credits.");

        // Main game loop
        boolean keepPlaying = true;
        while (keepPlaying && bank.getBankroll() > 0) {
            List<Card> deck = generateDeck();
            shuffleDeck(deck);

            // Player places a bet
            System.out.print("Place your bet (between 1 and 5 credits): ");
            int bet = scanner.nextInt();
            if (!bank.placeBet(bet)) {
                System.out.println("Insufficient funds to place that bet.");
                continue;
            }

            // Deal hand to the player
            player.setHand(dealHand(deck));
            player.displayHand();

            // Evaluate hand
            String result = evaluateHand(player.getHand());
            System.out.println(result);

            if (!result.equals("No winning hand")) {
                System.out.println("You won!");
                // Dummy payout for now (bet * 2)
                bank.addWinnings(bet * 2);
            } else {
                System.out.println("You lost this round.");
            }

            // Display updated bankroll
            bank.printBankroll();

            // Ask if the player wants to continue
            System.out.print("Do you want to keep playing? (yes/no): ");
            keepPlaying = scanner.next().equalsIgnoreCase("yes");
        }

        System.out.println("Thanks for playing! Your final bankroll is " + bank.getBankroll());
        scanner.close();
    }
}

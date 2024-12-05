package team_purple.final_video_poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;

public class VideoPoker {

    // Method to generate a deck of 52 cards
    public static List<Card> generateDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
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
    
    //Gets the rank count
    public static Map<String, Integer> getRankCount(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : hand) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        return rankCount;
    }
//Gets the suit count
    public static Map<String, Integer> getSuitCount(List<Card> hand) {
        Map<String, Integer> suitCount = new HashMap<>();
        for (Card card : hand) {
            suitCount.put(card.getSuit(), suitCount.getOrDefault(card.getSuit(), 0) + 1);
        }
        return suitCount;
    }
    
//Check for which hand the user has   
    public static boolean isPair(Map<String, Integer> rankCount) {
        return rankCount.values().contains(2);
    }

    public static boolean isTwoPair(Map<String, Integer> rankCount) {
        int pairCount = 0;
        for (int count : rankCount.values()) {
            if (count == 2) pairCount++;
        }
        return pairCount == 2;
    }

    public static boolean isThreeOfAKind(Map<String, Integer> rankCount) {
        return rankCount.values().contains(3);
    }

    public static boolean isFourOfAKind(Map<String, Integer> rankCount) {
        return rankCount.values().contains(4);
    }
    
    public static boolean isFullHouse(Map<String, Integer> rankCount) {
        return rankCount.values().contains(3) && rankCount.values().contains(2);
    }

    public static boolean isFlush(Map<String, Integer> suitCount) {
        return suitCount.values().contains(5);
    }

    public static boolean isStraight(List<Card> hand) {
        List<Integer> values = new ArrayList<>();
        Map<String, Integer> rankToValue = createRankToValueMap();
        
        for (Card card : hand) {
            values.add(rankToValue.get(card.getRank()));
        }
        
        Collections.sort(values);
        
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i + 1) != values.get(i) + 1) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isStraightFlush(List<Card> hand) {
        return isFlush(getSuitCount(hand)) && isStraight(hand);
    }

    public static boolean isRoyalFlush(List<Card> hand) {
        if (!isFlush(getSuitCount(hand))) return false;

        List<Integer> values = new ArrayList<>();
        Map<String, Integer> rankToValue = createRankToValueMap();
        
        for (Card card : hand) {
            values.add(rankToValue.get(card.getRank()));
        }

        Collections.sort(values);
        // A royal flush is 10, J, Q, K, A
        return values.equals(Arrays.asList(10, 11, 12, 13, 14));
    }

    public static boolean isJacksOrBetter(Map<String, Integer> rankCount) {
        List<String> highRanks = Arrays.asList("Jack", "Queen", "King", "Ace");
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (highRanks.contains(entry.getKey()) && entry.getValue() == 2) {
                return true;
            }
        }
        return false;
    }


    // Helper function to map card ranks to integer values
    public static Map<String, Integer> createRankToValueMap() {
        Map<String, Integer> rankToValue = new HashMap<>();
        rankToValue.put("2", 2);
        rankToValue.put("3", 3);
        rankToValue.put("4", 4);
        rankToValue.put("5", 5);
        rankToValue.put("6", 6);
        rankToValue.put("7", 7);
        rankToValue.put("8", 8);
        rankToValue.put("9", 9);
        rankToValue.put("10", 10);
        rankToValue.put("Jack", 11);
        rankToValue.put("Queen", 12);
        rankToValue.put("King", 13);
        rankToValue.put("Ace", 14);
        return rankToValue;
    }

    

    //Hand evaluation
    public static String evaluateHand(List<Card> hand) {
        Map<String, Integer> rankCount = getRankCount(hand);
        Map<String, Integer> suitCount = getSuitCount(hand);
        
        if (isRoyalFlush(hand)) return "Royal Flush!";
        if (isStraightFlush(hand)) return "Straight Flush!";
        if (isFourOfAKind(rankCount)) return "Four of a Kind!";
        if (isFullHouse(rankCount)) return "Full House!";
        if (isFlush(suitCount)) return "Flush!";
        if (isStraight(hand)) return "Straight!";
        if (isThreeOfAKind(rankCount)) return "Three of a Kind!";
        if (isTwoPair(rankCount)) return "Two Pair!";
        if (isJacksOrBetter(rankCount)) return "Jacks or Better!";
        return "No winning hand";
       }

    public static int calculatePayout(String result, int bet) {
        return switch (result) {
            case "Royal Flush!" -> bet * 250;
            case "Straight Flush!" -> bet * 50;
            case "Four of a Kind!" -> bet * 25;
            case "Full House!" -> bet * 9;
            case "Flush!" -> bet * 6;
            case "Straight!" -> bet * 4;
            case "Three of a Kind!" -> bet * 3;
            case "Two Pair!" -> bet * 2;
            case "Jacks or Better!" -> bet;
            default -> 0;
        };
    }

    public static List<Card> replaceCards(List<Card> hand, List<Card> deck) {
        List<Integer> indicesToHold = new ArrayList<>();
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("Which cards would you like to hold? Enter positions (1-5) seperated by spaces:");
                Scanner scanner = new Scanner(System.in);
                String[] holdIndices = scanner.nextLine().split(" ");
                for (String index : holdIndices) {
                    int position = Integer.parseInt(index) - 1;
                    if (position >= 0 && position < 5) {
                        indicesToHold.add(position);
                    } else {
                        throw new NumberFormatException();
                    }
                }
                validInput = true;

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter numbers between 1 and 5");
            }
        }

        List<Card> newHand = new ArrayList<>(hand);
        int deckIndex = 5; //start from the 6th card in deck
        for (int i = 0; i < hand.size(); i++) {
            if (!indicesToHold.contains(i)) {
                newHand.set(i, deck.get(deckIndex++)); //Replaces card with next one in deck
            }
        }
        return newHand;

    }

    public static Card dealCard(List<Card> deck) {
        if (deck.isEmpty()) {
            System.out.println("Deck is empty.");
            return null;
        }
        //Generate random card from deck
        int index = (int) (Math.random() * deck.size());
        Card card = deck.get(index);
        deck.remove(index);
        return card;
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
            System.out.print("Place your bet (between 1 and "+ bank.getBankroll() + " credits): ");
            int bet = scanner.nextInt();
            if (!bank.placeBet(bet)) {
                System.out.println("Invalid bet amount. Please enter a value between 1 and " + bank.getBankroll());
                continue;
            }

            // Deal hand to the player
            player.setHand(dealHand(deck));
            player.displayHand();

            //Card Replacement
            List<Card> finalHand = replaceCards(player.getHand(), deck);
            player.setHand(finalHand);
            player.displayHand();

            // Evaluate final hand
            String result = evaluateHand(player.getHand());
            System.out.println(result);



            //Payout handling
            int payout = calculatePayout(result, bet);
            if (payout > 0) {
                System.out.println("You won " + payout + " credits!");
                bank.addWinnings(payout);
            } else {
                System.out.println("You lost this round");
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


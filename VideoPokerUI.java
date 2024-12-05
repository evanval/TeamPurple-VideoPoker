package team_purple.final_video_poker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

public class VideoPokerUI extends Application {
    private Player player = new Player();
    private Bank bank = new Bank(1000);
    private List<Card> deck = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();
    private int bet = 0;
    private Stage primaryStage;
    private VBox mainLayout;
    private boolean isRoundInProgress = false;


    @Override
    public void start(Stage primaryStage) {

        // Create the main layout (VBox)
        mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);  // Centers content in the VBox

        // Load the poker table image
        String tableImagePath = "/team_purple/final_video_poker/Images/Poker_Table.jpg";
        Image tableImage = loadImage(tableImagePath);
        if (tableImage != null) {
            BackgroundImage backgroundImage = new BackgroundImage(tableImage, null, null, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            mainLayout.setBackground(new Background(backgroundImage));
        } else {
            System.err.println("Poker table image not found at: " + tableImagePath);
        }

        // Display bankroll
        Label bankrollLabel = new Label("Bankroll: $" + bank.getBankroll());
        bankrollLabel.setStyle("fx-font-size: 20;");
        bankrollLabel.setTextFill(Color.WHITE);
        bankrollLabel.setStyle("-fx-font-weight: bold;");
        mainLayout.getChildren().add(bankrollLabel);

        //Create text box for bet input
        TextField betField = new TextField();
        betField.setPromptText("Enter Bet");
        betField.setPrefWidth(80);
        betField.setMaxWidth(80);
        mainLayout.getChildren().add(betField);

        //Button to start game and deal cards
        Button dealButton = new Button("Deal Cards");
        dealButton.setOnAction(event -> {
            try {
                bet = Integer.parseInt(betField.getText());
                if (bet > 0 && bet <= bank.getBankroll()) {
                    bank.placeBet(bet);
                    startRound();
                    updateBankrollLabel(bankrollLabel);
                    betField.clear();

                } else {
                    showAlert("Invalid bet.",  "Please enter a valid bet.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid input", "Please enter a valid number for the bet.");
            }
        });
        mainLayout.getChildren().add(dealButton);

        // Create a HBox for the hand of cards
        HBox handLayout = new HBox(10);
        handLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(handLayout);

        //Button to replace cards
        Button replaceButton = new Button("Replace Cards");
        replaceButton.setOnAction(event -> {
                replaceCards();
                handLayout.getChildren().clear();
                displayHand(handLayout);

        });

        mainLayout.getChildren().add(replaceButton);


        //Button to evaluate hand and display results
        Button evaluateButton = new Button("Evaluate Cards");
        evaluateButton.setOnAction(event -> {
                evaluateHandAndDisplayResult();

        });

        mainLayout.getChildren().add(evaluateButton);

        //Create scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Video Poker");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Exit Button
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
        mainLayout.getChildren().add(exitButton);
    }

    private void startRound() {
    isRoundInProgress = true;
        if (mainLayout == null) {
            System.err.println("Main layout is not initialized.");
            return;
        }
        //Initialize deck, shuffle, and hand
        deck = VideoPoker.generateDeck();
        VideoPoker.shuffleDeck(deck);
        hand = VideoPoker.dealHand(deck);
        player.setHand(hand);
        player.displayHand();

        //Get Hbox that holds cards and display them
        HBox handLayout = (HBox) mainLayout.getChildren().get(3);
        if (handLayout != null) {
            handLayout.getChildren().clear();
            displayHand(handLayout);
        } else {
            System.err.println("Hand layout is not available.");
        }
    }

    private void replaceCards() {

        List<Card> finalHand = new ArrayList<>();

        for (Card card : player.getHand()) {
            if (card.isHeld()) {
                finalHand.add(card);
            } else {
                Card newCard = VideoPoker.dealCard(deck);
                finalHand.add(newCard);
            }
        }
        player.setHand(finalHand);

        isRoundInProgress = false;
    }

    private void evaluateHandAndDisplayResult() {
        String result = VideoPoker.evaluateHand(player.getHand());
        showAlert("Hand evaluation", result);
        int payout = VideoPoker.calculatePayout(result, bet);
        if (payout > 0) {
            bank.addWinnings(payout);
        }
        Label bankrollLabel = (Label) mainLayout.getChildren().get(0); // 1st child in mainLayout
        updateBankrollLabel(bankrollLabel);
    }

    private void displayHand(HBox handLayout) {
        for (Card card : player.getHand()) {
            String cardImagePath = "/team_purple/final_video_poker/Images/Cards/" + card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
            ImageView cardImageView = new ImageView(loadImage(cardImagePath));
            cardImageView.setFitHeight(140);
            cardImageView.setFitWidth(100);

            //Button for holding cards option
            ToggleButton holdButton = new ToggleButton("Hold");
            holdButton.setStyle("-fx-font-size: 12;");
            holdButton.setSelected(false);

            holdButton.setDisable(!isRoundInProgress);

            holdButton.setOnAction(event -> {
                if (holdButton.isSelected()) {
                    card.setHeld(true);
                } else {
                    card.setHeld(false);
                }
            });

            VBox cardwithButton = new VBox(10, cardImageView, holdButton);
            cardwithButton.setAlignment(Pos.CENTER);
            handLayout.getChildren().add(cardwithButton);
        }
    }

    private Image loadImage(String path) {
        try {
            Image image = new Image(getClass().getResource(path).toExternalForm());
            return image;
        } catch (NullPointerException ex) {
            System.err.println("Image not found at: " + path);
            return null;
        }
    }

    private void updateBankrollLabel(Label bankrollLabel) {
        bankrollLabel.setText("Bankroll: $" + bank.getBankroll());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

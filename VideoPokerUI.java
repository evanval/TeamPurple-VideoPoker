import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;


import java.util.List;

public class VideoPokerUI extends Application {



    private Label bankrollLabel;
    private final ImageView[] cardImages = new ImageView[5]; // To hold images of cards

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize game components
        Bank bank = new Bank(100);  // starting bankroll
        VideoPoker.dealHand(VideoPoker.generateDeck());

        // Create the layout
        VBox mainLayout = new VBox();
        HBox handLayout = new HBox(10);
        mainLayout.getChildren().addAll(bankrollLabel, handLayout);

        // Initialize labels and images
        bankrollLabel = new Label("Bankroll: " + bank.getBankroll());

        for (int i = 0; i < 5; i++) {
            cardImages[i] = new ImageView();
            cardImages[i].setFitHeight(140);
            cardImages[i].setFitWidth(100);
            handLayout.getChildren().add(cardImages[i]);
        }


        //Add poker table as background image to layout
        Image tableBackground = new Image("file:Images/Poker_Table.png");
        BackgroundImage backgroundImage = new BackgroundImage(tableBackground, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        mainLayout.setBackground(new Background(backgroundImage));

        // Buttons for actions
        Button dealButton = new Button("Deal Hand");
        dealButton.setOnAction(e -> dealHand());

        Button placeBetButton = new Button("Place Bet");
        placeBetButton.setOnAction(e -> placeBet());

        // Add buttons to the layout
        mainLayout.getChildren().addAll(placeBetButton, dealButton);

        // Create the scene and show stage
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Poker");
        primaryStage.show();
    }

    private void dealHand() {
        List<Card> hand = VideoPoker.dealHand(VideoPoker.generateDeck());
        for (int i = 0; i < 5; i++) {
            Image cardImage = new Image("file:Images/" + hand.get(i).toString() + ".png"); // Path to your card PNGs
            cardImages[i].setImage(cardImage);
        }
    }

    private void placeBet() {
        // Your bet logic here (e.g., dialog box to input bet amount)
    }
}

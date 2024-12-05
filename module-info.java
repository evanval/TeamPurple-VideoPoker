/**
 *
 */
module team_purple.final_video_poker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens team_purple.final_video_poker to javafx.fxml;
    exports team_purple.final_video_poker;
}
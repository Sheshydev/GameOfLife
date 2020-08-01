package sample;

import javafx.animation.Animation;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;

public class Menu extends Pane {
    public Menu(){
        final String vidPath = "..\\GoLFinal.mp4";
        Media media = new Media(getClass().getResource(vidPath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(100);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(Animation.INDEFINITE);
        mediaView.setFitWidth(2000);
        this.getChildren().add(mediaView);
        mediaView.setEffect(gaussianBlur);
        mediaView.setTranslateX(-50);
        mediaView.setTranslateY(-50);

        GridPane grid = new GridPane();
        Text title = new Text("Game of Life");
        title.setFont(Font.font("Verdana", 100));
        Button btn = new Button("Start simulation");
        btn.setOnMousePressed(mouseEvent -> {
            mediaPlayer.stop();
            Main.getRoot().getChildren().remove(this);
            Main.getRoot().getChildren().add(Main.getSimView());
        });
        grid.addRow(0,title);
        grid.addRow(1,btn);
        grid.setTranslateX(Main.SCREENWIDTH/3);
        grid.setTranslateY(Main.SCREENHEIGHT/3);
        this.getChildren().add(grid);

        grid.setHalignment(title, HPos.CENTER);
        grid.setHalignment(btn, HPos.CENTER);
    }
}

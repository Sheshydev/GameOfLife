package sample;

import javafx.animation.Animation;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.nio.file.Paths;

public class Menu extends Pane {
    public Menu(){
        //sets background video
        Media media = new Media(Paths.get("GoLFinal.mp4").toUri().toString());
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

        //sets menu UI
        GridPane uiGrid = new GridPane();
        uiGrid.setVgap(20);

        Text title = new Text("Game of Life");
        title.setFont(Font.font("Verdana", 100));

        Button btn = new Button("Start simulation");

        btn.setOnMousePressed(mouseEvent -> {
            mediaPlayer.stop();
            Main.getRoot().getChildren().remove(this);
            Main.getRoot().getChildren().add(Main.getSimView());
        });



        uiGrid.addRow(0,title);
        uiGrid.addRow(1,btn);
        uiGrid.setHalignment(title, HPos.CENTER);
        uiGrid.setHalignment(btn, HPos.CENTER);

        //set menu UI in accordance to screen dimensions
        uiGrid.setTranslateX(Main.SCREENWIDTH/3);
        uiGrid.setTranslateY(Main.SCREENHEIGHT/3);

        this.getChildren().add(uiGrid);
    }
}

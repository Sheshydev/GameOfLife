package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

import static sample.Main.*;

public class ControlWindow extends GridPane {
    public ControlWindow(){
        this.setStyle("-fx-background-color: rgba(42,42,42,0.9)");
        this.setWidth(SCREENWIDTH);
        //column allows center alignment of child nodes
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        this.getColumnConstraints().add(col);
        //arranges rows with consistent padding
        for(int i = 0; i < 6; i++){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(7);
            this.getRowConstraints().add(row);
        }

        Text txt = new Text("Control Window");
        txt.setFont(Font.font("Verdana", 20));
        txt.setFill(Color.GHOSTWHITE);

        Button btn = new Button("Show control window");
        btn.setOnMousePressed(mouseEvent -> {
            getSimView().getChildren().add(this);
            getSimView().getChildren().remove(btn);
        });
        btn.setTranslateY(-SCREENHEIGHT * 0.45);
        btn.setTranslateX(SCREENWIDTH * 0.90);

        Button btn1 = new Button("Pause/Play");
        btn1.setOnMousePressed(mouseEvent -> {
            System.out.println("running");
            //spacebar pauses or unpauses simulation
            if(getTimeline().getStatus() == Animation.Status.RUNNING) {
                System.out.println("running");
                for (int y = 0; y < Main.getHeight(); y++) {
                    for (int x = 0; x < Main.getWidth(); x++) {
                        //all cells scan their neighbors
                        getCell(y,x).scanState();
                        getCell(y,x).updateColor();
                    }
                }
                getTimeline().stop();
            }
            else {
                System.out.println("running1");
                getTimeline().play();
            }
        });

        Button btn2 = new Button("Color mode");
        btn2.setOnMousePressed(mouseEvent -> {
            setColored(!isColored());
        });


        Button btn3 = new Button("Edge borders");
        btn3.setOnMousePressed(mouseEvent -> {
            setEdgeBorders(!isEdgeBorders());
        });

        FileChooser fileChooser = new FileChooser();
        Button btn4 = new Button("import file");
        btn4.setOnMousePressed(mouseEvent -> {
            File selectedFile = fileChooser.showOpenDialog(getStage());
        });

        Button btn5 = new Button("Hide control window");
        btn5.setOnMousePressed(mouseEvent -> {
            if(getSimView().getChildren().contains(this)) {
                getSimView().getChildren().remove(this);
                getSimView().getChildren().add(btn);
            }
        });

        this.addRow(0,txt);
        this.addRow(1,btn1);
        this.addRow(2, btn2);;
        this.addRow(3, btn3);
        this.addRow(4, btn4);
        this.addRow(5, btn5);

        setHalignment(txt, HPos.CENTER);
        setHalignment(btn1, HPos.CENTER);
        setHalignment(btn2, HPos.CENTER);
        setHalignment(btn3, HPos.CENTER);
        setHalignment(btn4, HPos.CENTER);
        setHalignment(btn5, HPos.CENTER);
    }
}

package sample;

import javafx.animation.Animation;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

public class ControlWindow extends GridPane {
    public ControlWindow(){
        this.setStyle("-fx-background-color: rgba(42,42,42,0.9)");
        this.setWidth(Main.SCREENWIDTH);
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
            Main.getRoot().getChildren().add(this);
            Main.getRoot().getChildren().remove(btn);
        });
        btn.setTranslateY(-Main.SCREENHEIGHT * 0.45);
        btn.setTranslateX(Main.SCREENWIDTH * 0.90);

        Button btn1 = new Button("Pause/Play");
        btn1.setOnMousePressed(mouseEvent -> {
            //spacebar pauses or unpauses simulation
            if(Main.getTimeline().getStatus() == Animation.Status.RUNNING) {
                for (int y = 0; y < Main.HEIGHT; y++) {
                    for (int x = 0; x < Main.WIDTH; x++) {
                        //all cells scan their neighbors
                        Main.getCell(y,x).scanState();
                        Main.getCell(y,x).updateColor();
                    }
                }
                Main.getTimeline().stop();
            }
            else
                Main.getTimeline().play();
        });

        Button btn2 = new Button("Color mode");
        btn2.setOnMousePressed(mouseEvent -> {
            Main.setColored(!Main.isColored());
        });


        Button btn3 = new Button("Edge borders");
        btn3.setOnMousePressed(mouseEvent -> {
            Main.setEdgeBorders(!Main.isEdgeBorders());
        });

        FileChooser fileChooser = new FileChooser();
        Button btn4 = new Button("import file");
        btn4.setOnMousePressed(mouseEvent -> {
            File selectedFile = fileChooser.showOpenDialog(Main.getStage());
        });

        Button btn5 = new Button("Hide control window");
        btn5.setOnMousePressed(mouseEvent -> {
            if(Main.getRoot().getChildren().contains(this)) {
                Main.getRoot().getChildren().remove(this);
                Main.getRoot().getChildren().add(btn);
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

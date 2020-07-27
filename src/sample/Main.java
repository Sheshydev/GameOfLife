package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static int WIDTH = 180;
    public static int HEIGHT = 120;
    private static Cell cellArr[][] = new Cell[HEIGHT][WIDTH];
    private Pane root = new Pane();

    @Override
    public void start(Stage primaryStage) throws Exception{
        createBoard();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), actionEvent -> {
            for(int y = 0; y < HEIGHT; y++){
                for(int x = 0; x < WIDTH; x++){
                    cellArr[y][x].scanState();
                }
            }
            for(int y = 0; y < HEIGHT; y++){
                for(int x = 0; x < WIDTH; x++){
                    cellArr[y][x].updateState();
                }
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        Scene scene = new Scene(root, WIDTH * 5, HEIGHT * 5);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                timeline.play();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createBoard(){
        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Cell cell = new Cell(y, x);
                cellArr[y][x] = cell;
                root.getChildren().add(cell);
            }
        }
    }

    public static Cell getCell(int y, int x){
        return cellArr[y][x];
    }
}

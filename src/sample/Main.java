package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static int WIDTH = 306;
    public static int HEIGHT = 180;
    public static double SCREENWIDTH = 1920;
    public static double SCREENHEIGHT = 1080;
    private static Cell cellArr[][] = new Cell[HEIGHT][WIDTH];
    private Pane root = new Pane();
    private Group group = new Group();
    private double yMoveSpeed = 5;
    private double xMoveSpeed = 5;

    @Override
    public void start(Stage primaryStage) throws Exception{
        final double SCALE_DELTA = 1.1;
        createBoard();

        root.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor =
                        (event.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;

                group.setScaleX(group.getScaleX() * scaleFactor);
                group.setScaleY(group.getScaleY() * scaleFactor);
            }
        });

        final Delta dragDelta = new Delta();
        group.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = group.getTranslateX() - mouseEvent.getScreenX();
                dragDelta.y = group.getTranslateY() - mouseEvent.getScreenY();
            }
        });
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                group.setTranslateX(mouseEvent.getScreenX() + dragDelta.x);
                group.setTranslateY(mouseEvent.getScreenY() + dragDelta.y);
                /*primaryStage.setX(mouseEvent.getScreenX() + dragDelta.x);
                primaryStage.setY(mouseEvent.getScreenY() + dragDelta.y);*/
            }
        });

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

        root.getChildren().add(group);
        timeline.setCycleCount(Animation.INDEFINITE);
        Scene scene = new Scene(root, SCREENWIDTH, SCREENHEIGHT);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                if(timeline.getStatus() == Animation.Status.RUNNING)
                    timeline.stop();
                else
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
                group.getChildren().add(cell);
            }
        }
    }

    public static Cell getCell(int y, int x){
        return cellArr[y][x];
    }

    class Delta { double x, y; }
}

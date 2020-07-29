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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static int WIDTH = 306; //amount of cells in a row
    public static int HEIGHT = 180; //amount of cells in a column
    public static double SCREENWIDTH = 1920;
    public static double SCREENHEIGHT = 1080;

    private static Cell cellArr[][] = new Cell[HEIGHT][WIDTH];
    private GridPane root = new GridPane();
    private Pane boardScene = new Pane();
    private Group board = new Group();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(root, SCREENWIDTH, SCREENHEIGHT);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);

        final double SCALE_DELTA = 1.05; //zoom factor

        boardScene.setOnScroll(event -> {
            //zoom function
            event.consume();

            if (event.getDeltaY() == 0) {
                return;
            }

            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1/SCALE_DELTA;

            board.setScaleX(board.getScaleX() * scaleFactor);
            board.setScaleY(board.getScaleY() * scaleFactor);
        });

        final Delta dragDelta = new Delta();
        board.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = board.getTranslateX() - mouseEvent.getScreenX();
            dragDelta.y = board.getTranslateY() - mouseEvent.getScreenY();
        });
        board.setOnMouseDragged(mouseEvent -> {
            //drags the boardScene
            board.setTranslateX(mouseEvent.getScreenX() + dragDelta.x);
            board.setTranslateY(mouseEvent.getScreenY() + dragDelta.y);
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), actionEvent -> {
            for(int y = 0; y < HEIGHT; y++){
                for(int x = 0; x < WIDTH; x++){
                    //all cells scan their neighbors
                    cellArr[y][x].scanState();
                }
            }
            for(int y = 0; y < HEIGHT; y++){
                for(int x = 0; x < WIDTH; x++){
                    //all cells update their state after scanning
                    cellArr[y][x].updateState();
                }
            }
        }));
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                if(timeline.getStatus() == Animation.Status.RUNNING)
                    timeline.stop();
                else
                    timeline.play();
            }
        });
        timeline.setCycleCount(Animation.INDEFINITE);

        //divides control window and boardScene
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(85);
        col2.setPercentWidth(15);
        root.getColumnConstraints().add(col1);
        root.getColumnConstraints().add(col2);
        root.addColumn(0,boardScene);
        ControlWindow control = new ControlWindow();
        root.addColumn(1);
        root.getChildren().remove(control);

        createBoard();
        boardScene.getChildren().add(board);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createBoard(){
        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Cell cell = new Cell(y, x);
                cellArr[y][x] = cell;
                board.getChildren().add(cell);
            }
        }
    }

    public static Cell getCell(int y, int x){
        return cellArr[y][x];
    }

    class Delta { double x, y; } //records cursor coordinates
}

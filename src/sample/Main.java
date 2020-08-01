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
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static int WIDTH = 26; //amount of cells in a row
    public static int HEIGHT = 15; //amount of cells in a column
    public static double SCREENWIDTH = 1920;
    public static double SCREENHEIGHT = 1080;

    private static boolean colored = true;
    private static boolean edgeBorders = false;

    private static Stage stage;
    private static Cell cellArr[][] = new Cell[HEIGHT][WIDTH];
    private static Menu menu = new Menu();
    private static StackPane root = new StackPane();
    private static GridPane simView = new GridPane();
    private static Pane boardScene = new Pane();
    private static Group board = new Group();
    private static Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.7), actionEvent -> {
        scanAllCellNeighbors();
        updateAllCellColors();
        updateAllCellStates();
    }));

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(root, SCREENWIDTH, SCREENHEIGHT);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        stage = primaryStage;

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

        scene.setOnKeyPressed(e -> {
            //spacebar pauses or unpauses simulation
            if(e.getCode() == KeyCode.P){
                if(timeline.getStatus() == Animation.Status.RUNNING) {
                    scanAllCellNeighbors();
                    updateAllCellColors();
                    timeline.stop();
                }
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
        simView.getColumnConstraints().add(col1);
        simView.getColumnConstraints().add(col2);
        simView.addColumn(0,boardScene);
        ControlWindow control = new ControlWindow();
        simView.addColumn(1, control);
        root.getChildren().add(simView);

        root.getChildren().remove(simView);
        root.getChildren().add(menu);

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

    public static boolean isColored(){ return colored; }
    public static void setColored(boolean value){ colored = value; }
    public static boolean isEdgeBorders(){ return edgeBorders; }
    public static void setEdgeBorders(boolean value){ edgeBorders = value; }

    public static StackPane getRoot(){ return root; }
    public static Pane getBoardScene(){ return boardScene; }
    public static Group Board(){ return board; }
    public static Timeline getTimeline(){ return timeline; }
    public static GridPane getSimView(){ return simView; }

    public static void scanAllCellNeighbors(){
        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                //all cells scan their neighbors
                cellArr[y][x].scanState();
            }
        }
    }
    public static void updateAllCellColors(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                //all cells update their colors
                cellArr[y][x].updateColor();
            }
        }
    }
    public static void updateAllCellStates(){
        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                //all cells update their state after scanning
                cellArr[y][x].updateState();
            }
        }
    }

    public static Stage getStage(){
        return stage;
    }

}

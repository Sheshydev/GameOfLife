package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle
{
    private int state = 0;
    private int total = 0; // total neighbors
    private int newState;
    private int x;
    private int y;
    public Cell(int y, int x){
        this.setFill(Color.GRAY);
        this.setOnMouseEntered(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.isShiftDown()) {
                    live();
                    Main.scanAllCellNeighbors();
                    Main.updateAllCellColors();
                }
                if(mouseEvent.isControlDown()) {
                    die();
                    Main.scanAllCellNeighbors();
                    Main.updateAllCellColors();
                }
            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.isPrimaryButtonDown()) {
                    live();
                    Main.scanAllCellNeighbors();
                    Main.updateAllCellColors();
                }
                else{
                    die();
                    Main.scanAllCellNeighbors();
                    Main.updateAllCellColors();
                }
            }
        });
        this.setX(x * (Main.SCREENWIDTH/Main.getWidth()));
        this.setY(y * (Main.SCREENHEIGHT/Main.getHeight()));
        this.setWidth(Main.SCREENWIDTH/Main.getWidth());
        this.setHeight(Main.SCREENHEIGHT/Main.getHeight());
        this.x = x;
        this.y = y;
    }
    public void live(){
        state = 1;
        updateColor();
    }
    public void die(){
        state = 0;
        updateColor();
    }
    public void updateColor(){
        Color color = state == 1? Color.WHITE : Color.GRAY;
        if (state == 1 && Main.isColored() == true) {
            switch (total) {
                case 3:
                    color = Color.LIGHTGREEN;
                    break;
                case 4:
                    color = Color.LIMEGREEN;
                    break;
                case 5:
                    color = Color.YELLOW;
                    break;
                case 6:
                    color = Color.ORANGE;
                    break;
                case 7:
                    color = Color.RED;
                    break;
                case 8:
                    color = Color.DARKRED;
                    break;
            }
        }
        this.setFill(color);
    }
    public void scanState(){
        int neighborY;
        int neighborX;
        total = 0;
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(!(i == 0 && j == 0)){
                    neighborY = y + i;
                    neighborX = x + j;
                    if(Main.isEdgeBorders()) {
                        if(neighborX < 0 || neighborX >= Main.getWidth() || neighborY < 0 || neighborY >= Main.getHeight())
                            break;
                        else
                            total += Main.getCell(neighborY, neighborX).getState();
                    } else{
                        if (neighborX < 0)
                            neighborX = Main.getWidth() - 1;
                        if (neighborX >= Main.getWidth())
                            neighborX = 0;
                        if (neighborY < 0)
                            neighborY = Main.getHeight() - 1;
                        if (neighborY >= Main.getHeight())
                            neighborY = 0;
                        total += Main.getCell(neighborY, neighborX).getState();
                    }
                }
            }
        }
        if(((total >= 2 && total <= 3) && state == 1) || (total == 3 && state == 0))
            newState = 1;
        else
            newState = 0;
    }
    public void updateState(){
        state = newState;
    }
    public int getState(){
        return state;
    }
}

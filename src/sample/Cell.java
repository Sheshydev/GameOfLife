package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle
{
    private int state = 0;
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
                if(mouseEvent.isControlDown())
                    live();
                if(mouseEvent.isShiftDown())
                    die();
            }
        });
        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if(mouseEvent.isPrimaryButtonDown())
                    live();
                else
                    die();
            }
        });
        this.setX(x * 5);
        this.setY(y * 5);
        this.setWidth(5);
        this.setHeight(5);
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
        this.setFill(color);
    }
    public void scanState(){
        int total = 0;
        int neighborY;
        int neighborX;
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(!(i == 0 && j == 0)){
                    neighborY = y + i;
                    neighborX = x + j;
                    if(neighborX < 0)
                        neighborX = Main.WIDTH - 1;
                    if(neighborX >= Main.WIDTH)
                        neighborX = 0;
                    if(neighborY < 0)
                        neighborY = Main.HEIGHT - 1;
                    if(neighborY >= Main.HEIGHT)
                        neighborY = 0;
                    total += Main.getCell(neighborY, neighborX).getState();
                }
            }
        }
        if((total >= 2 && total <= 3) && state == 1)
            newState = 1;
        else if(total == 3 && state == 0)
            newState = 1;
        else
            newState = 0;
    }
    public void updateState(){
        state = newState;
        updateColor();
    }
    public int getState(){
        return state;
    }
}

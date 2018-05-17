/*
 *
 */
package snake;
import snakenet.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.google.gson.*;
import java.util.Random;
/**
 *
 * @author pmaclean
 */
public class PlaySnake extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        //new Evolver([population size], [former kept size (currently not used, can be any int)], [mutation rate], [game size for testing])        
       /* Evolver ev = new Evolver(100, 25, 0.2, 15);
        NeuralNet temp = ev.evolve(1500);
        final Sai snaek = temp.clone();
        */
        int gridx = 50;
        int gridy = 50;
        int winx = gridx*12 + 500;
        int winy = (gridy+1)*12;
        int h = winy/2;
        Pane root = new Pane();
        Scene scene = new Scene(root, winx,winy);
        Canvas c = new Canvas(winx,winy);
        root.getChildren().add(c);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFont(new Font("Times New Roman", 50));
        final long snt = System.nanoTime();
        Game game = new Game(gridx,gridy, System.currentTimeMillis());
        Random r = new Random(winy + System.currentTimeMillis());
        class GetSet{
            int d;
            public int get(){
                return d;
            }
            public void set(int x){
                d = x;
            }
        }
        
        GetSet direction = new GetSet();
        direction.set(-1);
        GetSet counter = new GetSet();
        counter.set(0);
        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long cnt) {
                double t = (cnt - snt) / 1000000000.0;
                gc.setFill(Color.DARKGREY);
                gc.fillRect(0,0,winx,winy);
                gc.setFill(Color.WHITE);
                gc.fillRect(488, 0, winx, winy);
                GameItem cur;
                GameState gs = game.getState();
                //int move = snaek.move(gs);                
                Board b = gs.board;
                for(int i=0;i<gridx-1;i++){
                    for(int j=0;j<gridy-1;j++){
                        cur = b.itemAt(i,j);
                        if(cur != null){
                        switch(cur.color){
                            case 'w':
                                gc.setFill(Color.WHITE);
                                break;
                            case 'r':
                                gc.setFill(Color.RED);
                                break;
                            default:
                                gc.setFill(Color.GREY);
                                break;
                            }
                        } else {
                            gc.setFill(Color.GREY);
                        }
                        gc.fillRect((i*12)+500, j*12 + 12, 12, 12);
                    }
                }
                gc.fillText("Score: "+gs.score, 50, h);
                int dir = direction.get();
                int count = counter.get();
                
                if(dir!=-1){
                if(count%3==0){
                    
                //    game.tick(move);
                    game.tick(dir);
                }
                counter.set(count>100?0:count+1);
                }
            }
        };
        scene.setOnKeyPressed((KeyEvent e) -> {
            //game.reset();
            char key = e.getCode().toString().charAt(0);
            switch(key){
                case 'W':
                    direction.set(0);
                    break;
                case 'A':
                    direction.set(3);
                    break;
                case 'S':
                    direction.set(2);
                    break;
                case 'D':
                    direction.set(1);
                    break;
                case 'R':
                    game.reset();
                    direction.set(-1);
                    break;
            }
            
        });
        animator.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);        
        //SnakeNet.main(args);
    }
    
}

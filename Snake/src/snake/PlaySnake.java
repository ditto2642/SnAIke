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
import java.util.Arrays;
import java.util.Random;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PlaySnake extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        //new Evolver([population size], [former kept size (currently not used, can be any int)], [mutation rate], [game size for testing])        
        /*Evolver ev = new Evolver(100, 30, 0.2, 50);
        NeuralNet temp = ev.evolve(150);
        final Sai snaek = temp.clone();*/
        
        //layout setup
        int gridx = 50;
        int gridy = 50;
        int winx = (gridx+2)*12;
        int winy = (gridy+2)*12;
        int h = winy/2;
        Pane root = new Pane();
        Scene scene = new Scene(root, winx+512,winy);
        Canvas c = new Canvas(winx,winy);
        HBox m = new HBox();
        root.getChildren().add(m);
        VBox vb = new VBox();
        vb.setMinWidth(512);
        Slider uGens = new Slider();
        uGens.setMaxWidth(500);
        uGens.setMin(0);
        uGens.setMax(1000);
        m.getChildren().add(vb);
        m.getChildren().add(c);
        vb.setSpacing(10);
        Text title = new Text("Snake");
        title.setFont(new Font(40));
        Text tgen = new Text("Generations to evolve:");
        tgen.setFont(new Font(15));
        Text score = new Text("Score: 0");
        score.setFont(new Font(25));
        Button play = new Button("Click here or press R to reset");
        Button evolve = new Button("Click here to start evolving (WILL HANG)");
        Button aiplay = new Button("Click Here to have the current AI play");
        Button useplay = new Button("Click Here to use the keyboard to play");
        vb.getChildren().add(title);
        vb.getChildren().add(score);
        vb.getChildren().add(play);
        vb.getChildren().add(tgen);
        uGens.setShowTickLabels(true);
        uGens.setMajorTickUnit(50);
        vb.getChildren().add(uGens);
        vb.getChildren().add(evolve);
        vb.getChildren().add(aiplay);
        vb.getChildren().add(useplay);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFont(new Font("Times New Roman", 50));
        final long snt = System.nanoTime();
        Game game = new Game(gridx,gridy, System.currentTimeMillis());
        Random r = new Random(winy + System.currentTimeMillis());
        
        //class so i can have numbers that are used that i can change
        class GetSet{
            int d;
            public int get(){
                return d;
            }
            public void set(int x){
                d = x;
            }
        }
        class GetSnek{
            Sai d;
            public Sai get(){
                return d;
            }
            public void set(Sai x){
                d = x;
            }
        }
        GetSet ai = new GetSet();
        ai.set(0);
        GetSet gens = new GetSet();
        gens.set(50);
        GetSnek snaek = new GetSnek();
        snaek.set(new NeuralNet(1,1,1,1));
        GetSet direction = new GetSet();
        direction.set(-1);
        GetSet counter = new GetSet();
        counter.set(0);
        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long cnt) {
                double t = (cnt - snt) / 1000000000.0;
                /*gc.setFill(Color.DARKGREY);
                gc.fillRect(0,0,winx,winy);*/
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, winx, winy);
                /*gc.setFill(Color.DARKGREY);
                gc.fillRect(0, 0, winx-12, winy-24);*/
                GameItem cur;
                GameState gs = game.getState();
                int move = snaek.get().move(gs);                
                Board b = gs.board;
                //draw all squares
                for(int i=0;i<gridx;i++){
                    for(int j=0;j<gridy;j++){
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
                        gc.fillRect((i*12)+12, j*12 + 11, 11, 11);
                    }
                }
                //gc.fillText("Score: "+gs.score, 50, h);
                score.setText("Score: "+ gs.score);
                int dir = direction.get();
                int count = counter.get();
                
                if(dir!=-1){
                if(count%3==0){
                    if(ai.get()==1){
                    game.tick(move);
                    }else{
                    game.tick(dir);
                    }
                }
                counter.set(count>100?0:count+1);
                }
            }
        };
        
        evolve.setOnAction((e)->{
            Evolver ev = new Evolver(100, 30, 0.2, 50);
            NeuralNet temp = ev.evolve(gens.get());            
            snaek.set(temp.clone());
        });
        play.setOnAction((e)->{
            game.reset();
        });
        aiplay.setOnAction((e)->{
            ai.set(1);
        });
        useplay.setOnAction((e)->{
            ai.set(0);
        });
        scene.setOnKeyPressed((KeyEvent e) -> {
            //game.reset();
            //keypress handling
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
        /*Evolver ev = new Evolver(100, 30, 0.2, 50);
        NeuralNet temp = ev.evolve(500);
        NeuralNet sn = temp.clone();
        Game g = new Game(50,50,System.currentTimeMillis());
        g.tick(1);
        g.tick(1);
        g.tick(1);
        GameState gs = g.getState();
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        g.tick(1);
        gs = g.getState();
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        for(int i=0;i<100;i++){
        g.tick((i%16)%4);
        }
        gs = g.getState();
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        System.out.println(Arrays.toString(sn.decision(NeuralNet.look(gs))));
        */
    }
    
}

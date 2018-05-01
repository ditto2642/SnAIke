/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakenet;
import java.util.Arrays;
import java.util.Random;
import snake.*;
/**
 *
 * @author pmaclean
 */
public class SnakeNet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Game g = new Game(25,25);        
        g.tick(0);
        System.out.println("game made and ticked");
        System.out.println(Arrays.toString(NeuralNet.look(g.getState())));
        String[] bs = g.getState().board.toString().split("], ");        
        for(String s : bs){
            System.out.println(s);
        }
        System.out.println("done?");
        NeuralNet n = new NeuralNet(24,16,4);
        for(int i=0;i<20&&g.getState().alive;i++){
            int m = n.move(g.getState());
            System.out.println(m);
            g.tick(m);
            
        }
        bs = g.getState().board.toString().split("], ");        
            for(String s : bs){
                System.out.println(s);
            }
        return;*/
        Game g = new Game(5,5,System.currentTimeMillis());
        g.tick(0);
        
        String[] bs = g.getState().board.toString().split("], ");        
        for(String s : bs){
            System.out.println(s);
        }
        g.tick(5);
        bs = g.getState().board.toString().split("], ");        
        for(String s : bs){
            System.out.println(s);
        }
        NeuralNet n = new NeuralNet(12,12,4,System.currentTimeMillis());
        n.move(g.getState());
        
    }
    
}

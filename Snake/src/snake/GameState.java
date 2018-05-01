/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

/**
 *
 * @author pmaclean
 */
public class GameState {
    public int score;
    public boolean alive;
    public Board board;
    public int[] pos;
    public int[] food;
    public int[] prev;
    
    GameState(int s, boolean a, Board b, int[] p, int[] f, int[] pr){
        this.score = s;
        this.alive = a;
        this.board = b;
        this.pos = p;
        this.food = f;
        this.prev = pr;
    }
}

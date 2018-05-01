/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.util.Arrays;

/**
 *
 * @author pmaclean
 */
public class Board {
    private GameItem[][] board;
    private int w;
    private int h;
    
    Board(int w,int h){
        this.board = new GameItem[h][w];
        this.w = w;
        this.h = h;
    }
    
    Board(GameItem[][] b){
        this.board = b;
        this.w = b[0].length;
        this.h = b.length;
    }
    
    public GameItem itemAt(int x,int y){
        return this.board[y][x];
    }
    
    public int[] getSize(){
        int[] wh = {w,h};
        return wh;
    }
    
    @Override
    public String toString(){
        return Arrays.deepToString(board);
    }
}

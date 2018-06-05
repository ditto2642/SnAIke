/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.util.Random;

/**
 *
 * @author pmaclean
 */
public class Game {
    private GameItem[][] b;
    int score, w, h;
    int[] head;
    boolean alive;
    Random r;
    int prev;
    int[] food;
    
    public Game(int w, int h, long salt){
        this.w = w;
        this.h = h;
        this.b = new GameItem[h][w];
        this.score = 0;
        this.alive = true;
        r = new Random(System.currentTimeMillis()+salt);
        
        int[] f = randCoords(this.w,this.h);
        this.b[f[1]][f[0]] = new Food();
        food = f;
        
        int[] start = randCoords(this.w,this.h);
        this.head = start;
        this.b[start[1]][start[0]] = new Snake();
    }
    
    public GameState getState(){
        int[] next = head.clone();
        next[(prev+1)%2] += prev==1||prev==2?1:-1;
        return new GameState(score, alive, new Board(b), head, food, next);
    }
    
    private static int sToL(int x){        
        return (int) Math.floor(x<=6?(x+5):(1/2*(x*x)+11));
    }
    
    private int[] randCoords(int xmax, int ymax){
        int[] temp = {r.nextInt(xmax-1), r.nextInt(ymax-1)};
        return temp;
    }
    
    private int rev(int x){
        switch(x){
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 0;
            case 3:
                return 1;
        }
        
        return -1;
                
    }
       
    public void tick(int dir){
        //this used to make it so if you went back on yourself, you just ontinued in the other direction
        //but i removed it to maybe make the snake better
        /*if(dir == rev(prev)) dir = prev;
        prev = dir;*/
        if(!alive) return;
        int[] next = new int[2];
        next = head.clone();
        next[(dir+1)%2] += dir==1||dir==2?1:-1;
        
        //check if hit wall
        if(next[1]> h-1||next[1]<0||next[0]> w-1||next[0]<0){
            alive = false;
            return;
        }
        GameItem ref = (b[next[1]][next[0]]);
        
        //check if hit tail
        if(ref instanceof Snake){
                alive = false;
                return;            
        }
        
        //check if hit food
        if(ref instanceof Food){
            score++;
            int[] f = randCoords(this.w,this.h);
            this.b[f[1]][f[0]] = new Food();
            food = f;
        }
        
        int length = sToL(score);
        head = next;
        b[next[1]][next[0]] = new Snake();
        
        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                if(b[i][j] != null){
                    if(b[i][j].update(length)){
                        b[i][j] = null;
                    }
                }
            }
        }
    }
    public void reset(){
        this.w = w;
        this.h = h;
        this.b = new GameItem[h][w];
        this.score = 0;
        this.alive = true;
        r = new Random(System.currentTimeMillis());
        
        int[] food = randCoords(this.w,this.h);
        this.b[food[1]][food[0]] = new Food();
        
        int[] start = randCoords(this.w,this.h);
        this.head = start;
        this.b[start[1]][start[0]] = new Snake();
    }
}

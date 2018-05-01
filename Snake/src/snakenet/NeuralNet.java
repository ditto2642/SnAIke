/*
 * Note: the structure for the matrix and neural net classes are written by me but are based on the ones used in codebullets snake ai: https://github.com/Code-Bullet/SnakeFusion/blob/master/SmartSnakesCombine/NeuralNet.pde
 * Effectively everything else is me 
*/
package snakenet;
import java.util.Arrays;
import snake.*;
/**
 *
 * @author pmaclean
 */
public class NeuralNet {
    int inN;
    int hidN;
    int outN;
    
    //these var names same as in codeBullets b/c im lazy
    Matrix whi;
    Matrix whh;
    Matrix woh;
    
    int score = 0;
    int ticks = 0;
    int testCount = 0;
    
    NeuralNet(int inputs, int hidden, int outputs, long seed){
        inN = inputs;
        hidN = hidden;
        outN = outputs;
        
        whi = new Matrix(hidN, inN+1, seed);
        whh = new Matrix(hidN, hidN+1, seed);
        woh = new Matrix(outN, hidN+1, seed);
        whi.randomize();
        whh.randomize();
        woh.randomize();
    }
    
    //sets values when game is over for purpose of calculating fitness
    public void fin(GameState gs){
        testCount++;
        score+=gs.score;
    }
    
    //called to get the move of this net based on a game board
    public int move(GameState gs){
        ticks++;
        float[] distances = look(gs);
        float[] out = decision(distances);
        System.out.println(Arrays.toString(distances));
        return (int) max(out);
    }
    
    public static float[] look(GameState gs){
        float[] dis = new float[24];
        int index;
        Board b = gs.board;
        int[] p = gs.pos;
        float[] cur;
        for(int i=0;i<8;i++){
            index = i*3;
            cur = lookInDir(b, i, p);
            dis[index] = cur[0];
            dis[index+1] = cur[1];
            dis[index+2] = cur[2];
        }
        return dis;
    }
    
    public static float[] lookInDir(Board b, int dir, int[] pos){
        float[] distances = {0,0,0};
        int[] vector = new int[2];
        int[] cur = pos.clone();
        switch(dir){
            case 0:
                vector[0] = 0;
                vector[1] = -1;
                break;
            case 1:
                vector[0] = 1;
                vector[1] = -1;
                break;
            case 2:
                vector[0] = 1;
                vector[1] = 0;
                break;
            case 3:
                vector[0] = 1;
                vector[1] = 1;
                break;
            case 4:
                vector[0] = 0;
                vector[1] = 1;
                break;
            case 5:
                vector[0] = -1;
                vector[1] = 1;
                break;
            case 6:
                vector[0] = -1;
                vector[1] = 0;
                break;
            case 7:
                vector[0] = -1;
                vector[1] = -1;
                break;
        }
        int count = 1;
        cur[0] += vector[0];
        cur[1] += vector[1];
        while(cur[0]>=0&&cur[1]>=0&&cur[0]<b.getSize()[0]&&cur[1]<b.getSize()[1]){            
            GameItem ref = b.itemAt(cur[0], cur[1]);
            if(distances[0]==-1&&ref instanceof Snake){
                distances[0] = count;
            }
            if(distances[1]==-1&&ref instanceof Food){
                distances[1] = count;
            }
            cur[0] += vector[0];
            cur[1] += vector[1];
            count++;
        }
        distances[2] = count;
        return distances;
    }
    
    public static int max(float[] a){
        int max = 0;
        for(int i=1;i<a.length;i++){
            if(a[i]>a[max]) max = i;
        }
        return max;
    }
    
    //feeds through neural net to get outputs, based on 24 inputs which are the distances between it and each significant object (tail, food, wall)
    public float[] decision(float[] in){
        Matrix inputs = Matrix.matrixFrom1d(in);        
        Matrix inputBias = inputs.addBias();
        
        Matrix hidden1in = whi.dot(inputBias);
        Matrix hidden1out = hidden1in.activate();
        Matrix h1outBias = hidden1out.addBias();
        
        Matrix h2in = whh.dot(h1outBias);
        Matrix h2out = h2in.activate();
        Matrix h2outBias = h2out.addBias();
        
        Matrix outIn = woh.dot(h2outBias);
        Matrix out = outIn.activate();
        
        return out.toArray();
        
    }

    //mutates all weights based on given rate
    public void mutate(double mrate){
        whi.mutate(mrate);
        whh.mutate(mrate);
        woh.mutate(mrate);
    }
    
    //clones everything
    @Override
    public NeuralNet clone(){
        NeuralNet c = new NeuralNet(inN,hidN,outN, (long)whi.matrix[0][0]);
        c.whi = whi.clone(); 
        c.whh = whh.clone();    
        c.woh = woh.clone(); 
        c.ticks = ticks;
        c.score = score;
        return c;
    }
    
    //clones weights but not score and time alive
    public NeuralNet shallowClone(){
        NeuralNet c = new NeuralNet(inN,hidN,outN, (long)whi.matrix[0][0]);
        c.whi = whi.clone(); 
        c.whh = whh.clone();    
        c.woh = woh.clone(); 
        return c;
    }
    
    //cross this net with another one
    public NeuralNet crossover(NeuralNet n){
        NeuralNet c = new NeuralNet(inN, hidN, outN,(long)whi.matrix[0][0]);
        c.whi = whi.crossover(n.whi);
        c.whh = whh.crossover(n.whh);
        c.woh = woh.crossover(n.woh);
        return c;
    }
}

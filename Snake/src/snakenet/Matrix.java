/*
 * Note: the structure for the matrix and neural net classes are written by me but are based on the ones used in codebullets snake ai: https://github.com/Code-Bullet/SnakeFusion/blob/master/SmartSnakesCombine/Matrix.pde
 * effectively everything else by is me
 */
package snakenet;

import java.util.Random;

/**
 *
 * @author pmaclean
 */
public class Matrix {
    float[][] matrix;
    int rows;
    int cols;
    Random rand;
    
    Matrix(int r,int c, long salt){
        rows = r;
        cols = c;
        matrix = new float[r][c];
        rand = new Random(System.currentTimeMillis()+salt);
    }
    
    Matrix(float[][] a){
        matrix = a;
        rows = a.length;
        cols = a[0].length;
        rand = new Random(System.currentTimeMillis());
    }
    
    //completely randomizes this matrix
    public void randomize(){
        for(int i = 0;i<rows;i++){
            for(int j = 0;j<cols;j++){
                matrix[i][j] = (rand.nextFloat()*2) -1;
            }
        }
    }
    
    //activate sigmoid function on all elements
    public Matrix activate(){
        Matrix n = new Matrix(rows,cols, rand.nextInt());
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                n.matrix[i][j] = sigmoid(matrix[i][j]);
            }
        }
        return n;
    }
    
    //make this matrix and some other matrix make matrix babies
    public Matrix crossover(Matrix p){
        Matrix c = new Matrix(rows,cols, rand.nextInt());
        int rr = rand.nextInt(rows);
        int cr = rand.nextInt(cols);
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(i<rr||(i==rr&&j<=cr)){
                    c.matrix[i][j] = matrix[i][j];
                } else {
                     c.matrix[i][j] = p.matrix[i][j];
                }
            }
        }
        return c;
    }
    
    //creates a matrix from a 1d array
    public static Matrix matrixFrom1d(float[] a){
        Matrix n = new Matrix(a.length, 1, (long) a[0]);
        for (int i = 0; i< a.length; i++) {
            n.matrix[i][0] = a[i];
        }
        return n;
    }
    
    //deep copy
    @Override
    public Matrix clone(){        
        Matrix c = new Matrix(rows,cols, rand.nextInt());
        for (int i =0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                c.matrix[i][j] = matrix[i][j];
            }
        }
        return c;
    }
    
    //return this matrix dot product another
    public Matrix dot(Matrix c){
        Matrix res = new Matrix(rows,c.cols, rand.nextInt());        
        if(cols == c.rows){
            for (int i =0; i<rows; i++) {
                for (int j = 0; j<c.cols; j++) {
                    float sum = 0;
                    for(int k=0;k<cols;k++){                       
                            sum += matrix[i][k]*c.matrix[k][j];                        
                    }
                    res.matrix[i][j] = sum;
                }
            }
        }
        return res;
    }

    float[] toArray() {
        float[] a = new float[rows*cols];
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j< cols; j++) {
                a[j+i*cols] = matrix[i][j];
            }
        }
        return a;
  }

    //cant explain b/c i dont really know how ALL of this works, if someone can, pull request to change it
    public Matrix addBias(){
        Matrix res = new Matrix(rows+1,1, rand.nextInt());
        for(int i=0;i<rows;i++){
            res.matrix[i][0] = matrix[i][0];
        }
        res.matrix[rows][0] = 1;
        return res;
    }
    
    //mutates at a given mutation rate
    public void mutate(double mrate){
        for (int i =0; i<rows; i++) {
            for (int j = 0; j<cols; j++) {
                if(rand.nextDouble()<mrate){
                    matrix[i][j] += rand.nextGaussian()/5;
                    if(matrix[i][j]>1) matrix[i][j] = 1;
                    if(matrix[i][j]<-1) matrix[i][j] = -1;
                }                
            }        
        }
    }
    
    //activation sigmoid, returns value between 0 and 1
    private float sigmoid(float x){
        return (float)(1/(Math.pow(Math.E, -x)+1));
    }
    
}

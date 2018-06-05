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
public class Snake extends GameItem {
    private int age;
    Snake(){
        this.color = 'w';
        this.age = 0;
    }

    @Override
    public boolean update(int length) {
        age++;
        return age>=length;
    }

    @Override
    public int getAge(){
        return age;
    }
    
    @Override
    public String toString(){
        return "snek";
    }  
    
    
}

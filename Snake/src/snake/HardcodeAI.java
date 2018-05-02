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
public class HardcodeAI implements Sai {
    
    @Override
    public int move(GameState gs){    
        int[] dim = gs.board.getSize();
        return 0;
    }    
}

/**
 * TicTacToe 
 * Robert Liberski
 * 05.2016
 */
package ox;

/**
 * Manage AI Game
 * @author R.Liberski
 */
public class AIGame implements Runnable{

    private Board parent;
    private boolean yourTurn=true;
    private boolean end=false;
    
    /**
     * Creates new AI Game
     * @param parent 
     *        Board - parent
     */
    public AIGame(Board parent){
        this.parent=parent;
    }
    
    /**
     * Creates a thread for game and run it
     * @see #run
     */
    public void play(){
        Thread newGame = new Thread(this);
        (new Thread(newGame)).start();
    }
    
    /**
     * End game
     */
    public void finish(){
        end=true;
    }
    
    /**
     * Game loop
     * @see ox.Board#updateStatus() 
     * @see #opponentTurn() 
     */    
    @Override
    public void run(){
        while(!end){
            try{
                parent.updateStatus();
                Thread.sleep(100);
                opponentTurn();
                Thread.sleep(100);
            }
            catch(Exception x){
            }
            parent.repaint();
        }
    }
   
    /**
     * Return true if it's player turn
     * @return true - if it's player turn
     */
    public boolean isYourTurn(){
        return yourTurn;
    }
    
    /**
     * Set a turn for player or opponent
     * @param t - true lub false
     */
    public void setYourTurn(boolean t){
        yourTurn=t;
    }
    
    /**
     * Return true if game is finished
     * @return true - if game is finished
     */
    public boolean isFinished(){
        return end;
    }
    
    /**
     * Steering player move
     * @see ox.Board#lookForGoodMove() 
     * @see ox.Board#checkEnemy()
     * 
     */
    public void opponentTurn(){
        if(!yourTurn && !end){                   
                parent.lookForGoodMove();
                parent.checkEnemy();
                yourTurn=true;
            
        }
    }
    
}

/**
 * OX 
 * Robert Liberski
 * 05.2016
 */
package ox;

/**
 * Manage Hot Seat Game
 * @author R.Liberski
 */
public class HotSeatGame implements Runnable{

    private Board parent;
    private boolean circleTurn=true;
    private boolean end=false;
    
    /**
     * Create new Hot Seat Game
     * @param parent 
     *        Board - parent
     */
    public HotSeatGame(Board parent){
        this.parent=parent;
    }
    
    /**
     * Create new thread for Hot Seat Game and run it
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
     */    
    @Override
    public void run(){
        while(!end){  
            try{
                parent.updateStatus();
                Thread.sleep(100);
                parent.repaint();
            }
            catch(Exception x){
            }
        }
    }
   
    /**
     * @return
     */
    public boolean isCircleTurn(){
        return circleTurn;
    }
    
    /**
     * Set circle turn
     * 3@param t - true or false
     */
    public void setCircleTurn(boolean t){
        circleTurn=t;
    }
    
}

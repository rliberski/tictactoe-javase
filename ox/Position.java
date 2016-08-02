/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */
package ox;

/**
 * @author R.Liberski
 */
public class Position {
    
    private int x;
    private int y;
    
    /**
     * Create new position
     * @param x - index x
     * @param y - index y
     */
    public Position(int x,int y){
        this.x=x;
        this.y=y;
    }
    
    /**
     * @param x - index x. 
     */
    public void setX(int x){
        this.x=x;
    }
    
    /**
     * @param y - index y. 
     */
    public void setY(int y){
        this.y=y;
    }
    
    /**
     * @return index x.
     */
    public int getX(){
        return x;
    }
    /**
     * @return index y.
     */
    public int getY(){
        return y;
    }
}

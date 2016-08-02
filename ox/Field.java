/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */

package ox;

//imports
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
//end of imports

/**
 * @author R.Liberski
 */
public class Field extends JButton implements ActionListener{
    
    /**
     * Field state
     * @see ox.Field
     */
    public enum State{
        N,         //empty
        O,        
        X;           
    }
    
    
    private BufferedImage graphic;
    private Board parent;
    private State state;
    private int posX;
    private int posY;
    
    /**
     * Create new field
     * @param parent
     *        Board - parent
     * @param posX
     *        X position
     * @param posY 
     *        Y position
     * @see ox.NetworkGame
     * @see ox.Board
     */
    public Field(Board parent,int posX, int posY){
        super(" ");
        
        setFocusPainted(false);
        setFocusable(false);
        graphic=readGraphic(getClass().getClassLoader().getResource("resources/field.png"));
        Dimension dimension = new Dimension(graphic.getWidth(),graphic.getHeight());
        setPreferredSize(dimension);
        
        this.addActionListener(this);
        
        this.parent=parent;
        this.posX=posX;
        this.posY=posY;
        this.state=State.N;
    }
    
    /**
     * Draw field
     * @param g -
     */
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.graphic,0,0,this);
    }
    
    /**
     * Read graphic from url
     * @param a
     *        url to file
     * @return Nowa grafika pola
     */
    public BufferedImage readGraphic(URL a){
        BufferedImage image;
        try{
            image=ImageIO.read(a);
            return image;
        }
        catch(IOException e){
            
            JOptionPane.showMessageDialog(
                    getParent(),
                    "Cannnot open graphic files!",
                    "Error!",
                    JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
            
            return null;
        }
    }
    
    /**
     * Press field actions
     * @param e 
     *        Event
     * @see ox.NetworkGame#isYourTurn()
     * @see ox.NetworkGame#haveCircle() 
     * @see #setO
     * @see #setX
     * @see ox.NetworkGame#setYourTurn(boolean)
     * @see ox.Board#updateStatus()
     * @see ox.NetworkGame#send(ox.Field) 
     * @see ox.Board#check()
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(!parent.hsGameStarted()){
            if(parent.isYourTurn() && isEmpty()){
                if(parent.haveCircle())
                    setO();
                else
                    setX();
                parent.setYourTurn(false);
                parent.updateStatus();
                repaint();
                Toolkit.getDefaultToolkit().sync();
                parent.send(this);
                parent.check();
            }
        }
        else{
            if(parent.isCircleTurn()){
                setO();
                parent.check();
                parent.setCircleTurn(false);
            }
            else{
                setX();
                parent.checkEnemy();
                parent.setCircleTurn(true);
            }
        }
        
    }
    
    /**
     * Change state to X
     * @see ox.Field.State
     */
    public void setX(){
        if(state.equals(State.N)){
            this.state=State.X;
            graphic=readGraphic(getClass().getClassLoader().getResource("resources/fieldX.png"));
        }
        repaint();
    }
    
    /**
     * Change state to O
     * @see ox.Field.State
     */
    public void setO(){
        if(state.equals(State.N)){
            this.state=State.O;
            graphic=readGraphic(getClass().getClassLoader().getResource("resources/fieldO.png"));
        }
        repaint();
    }
    
    /**
     * @return
     * @see ox.Field.State
     */
    public boolean isO(){
        return state.equals(State.O);
    }
    
    /**
     * @return 
     * @see ox.Field.State
     */
    public boolean isX(){
        return state.equals(State.X);
    }
    
    /**
     * @return True
     * @see ox.Field.State
     */
    public boolean isEmpty(){
        return state.equals(State.N);
    }
    
    /**
     * @return x position
     */
    public int getPosX(){
        return posX;
    }
    
    /**
     * @return y position
     */
    public int getPosY(){
        return posY;
    }
    
}

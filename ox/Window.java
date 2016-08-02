/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */
package ox;

//imports
import javax.swing.JFrame;
import java.awt.Dimension;
//end of imports

/**
 * Game window
 * @author R. Liberski
 * @see ox.Board
 * @see ox.StartMenu
 * @see ox.OptionsMenu
 * @see ox.NetworkGame
 */
public class Window extends JFrame{
    
    private StartMenu startMenu;
    private OptionsMenu optionsMenu;
    private Board board;
    private int fields=3;   //board size with default value
    private int win=3;      //fields needed to win with default value
    private String ip="localhost";
    private int port=12345;
    
    /**
     * Create new window
     * @see #setContent
     */
    public Window(){
        super("TicTacToe");
        
        Dimension dimension = new Dimension(500,500);
        setMinimumSize(dimension);
        //super.setResizable(false);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContent();
        
        setVisible(true);
    }
    
    /**
     * Content settings
     * @see ox.StartMenu
     * @see ox.OptionsMenu
     */
    protected void setContent(){
        
        startMenu=new StartMenu(this);
        optionsMenu=new OptionsMenu(this);
        
        add(startMenu);
        add(optionsMenu);
        
        setContentPane(startMenu);
        revalidate();
        
    }

    /**
     * Change view to start menu
     * @see ox.StartMenu
     */
    public void toStartMenu(){
        startMenu.setVisible(true);
        setContentPane(startMenu);
        revalidate();
    }
    
    /**
     * Change view to options menu
     * @see ox.OptionsMenu
     */
    public void toOptionsMenu(){
        optionsMenu.setVisible(true);
        setContentPane(optionsMenu);
        revalidate();
    }
    
    /** 
     * Start network game
     * @see ox.Board
     * @see ox.Board#startNetworkGame
     * @see ox.StartMenu
     */
    public void startNetwork(){
        if(board!=null){
            board.removeAll();
            board=null;
        }
        board=new Board(this,fields,win);
        add(board);
        setContentPane(board);
        board.setVisible(true);
        revalidate();
        
        board.startNetworkGame(ip,port);
    }
    
    /**
     * Start AI Game
     * @see ox.Board
     * @see ox.StartMenu
     * @see ox.Board#startAIGame()
     * 
     */
    public void startAI(){
        if(board!=null){
            board.removeAll();
            board=null;
        }
        board=new Board(this,fields,win);
        add(board);
        setContentPane(board);
        board.setVisible(true);
        revalidate();
        
        board.startAIGame();
    }
    
    /**
     * Start Hot Seat Game
     * @see ox.Board
     * @see ox.StartMenu
     * @see ox.Board#startHSGame()
     * 
     */
    public void startHS(){
        if(board!=null){
            board.removeAll();
            board=null;
        }
        board=new Board(this,fields,win);
        add(board);
        setContentPane(board);
        board.setVisible(true);
        revalidate();
        
        board.startHSGame();
    }
    
    /**
     * Save options
     * @param fields
     * @param win
     * @param ip
     * @param port 
     * @see ox.OptionsMenu
     */
    public void saveBoardData(int fields,int win, String ip, int port){
        this.fields=fields;
        this.win=win;
        this.ip=ip;
        this.port=port;
    }
    
}

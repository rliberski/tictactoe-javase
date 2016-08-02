/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */
package ox;

//imports
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
//end of imports

/**
 * @author R. Liberski
 */
public class NetworkGame implements Runnable{

    private Board parent;
    private boolean end=false;
    private boolean yourTurn=false;
    private boolean unableToConnect=false;
    private boolean youHaveCircle=true;
    private boolean requestAccepted=false;
    private String ip;
    private int port;
    private int lostConnection=0;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    
    /**
     * Create new Network Game
     * @param parent
     * @param ip
     * @param port
     */
    public NetworkGame(Board parent,String ip,int port){
        this.parent=parent;
        this.ip=ip;
        this.port=port;
    }
    
    /**
     * Run Game
     * @see #connect()
     * @see #initializeServer()
     * 
     */
    public void play(){
        if(!connect()) 
            initializeServer();
        Thread newGame = new Thread(this);
        (new Thread(newGame)).start();
    }
    
    /**
     * Game Loop
     * @see ox.Board#updateStatus()
     * @see #listenForRequest()
     * @see #checkConnection()
     * @see #opponentTurn()
     */
    @Override
    public void run(){
        while(!end){
            try{
                parent.updateStatus();
                Thread.sleep(100);
                if (!youHaveCircle && !requestAccepted){
                    listenForRequest();
                }
                else{
                    checkConnection();
                    opponentTurn();
                }
            }
            catch(Exception x){
            }
            parent.repaint();
        }
    }
    
    /**
     * End Game
     */
    public void finish(){
        yourTurn=false;
        end=true;
        unableToConnect=false;
        youHaveCircle=true;
        requestAccepted=false;
        lostConnection=0;
        
        try{
            if(dataOutput!=null)
                dataOutput.close();
            if(dataInput!=null)
                dataInput.close();
            if(socket!=null)
                socket.close();
            if(serverSocket!=null)
                serverSocket.close();
        }
        
        catch(IOException e){
            
        }
    }
    
    /**
     * Connect
     * @return True - if connected
     * @see #readBoardSize()
     */
    public boolean connect(){
        try {
            socket = new Socket(ip, port);
            dataOutput = new DataOutputStream(socket.getOutputStream());
            dataInput = new DataInputStream(socket.getInputStream());
            requestAccepted = true;
            readBoardSize();
            System.out.println("Connected!"); //**************************************
        } 
        catch (IOException e){
            return false;
	}
	return true;
    }
    
    /**
     * Initialize server if not connected
     */
    public void initializeServer(){
        try {
            serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
        } 
        catch (IOException e){}
        yourTurn = true;
	youHaveCircle = false;
        System.out.println("Server Initialized!"); //**************************************
    }
    
    /**
     * Listen for server request
     * @see #sendBoardSize()
     */
    public void listenForRequest(){
        try {
            socket = serverSocket.accept();
            dataOutput = new DataOutputStream(socket.getOutputStream());
            dataInput = new DataInputStream(socket.getInputStream());
            requestAccepted = true;
            sendBoardSize();
	} 
        catch (IOException e) {}
        System.out.println("Request accepted!"); //**************************************
    }
    
    /**
     * Check connection
     * @see ox.Board#endGame()
     */
    public void checkConnection(){
        if(lostConnection>=20){
            unableToConnect=true;
            JOptionPane.showMessageDialog(parent,"Disconnect!","Error!",JOptionPane.ERROR_MESSAGE);
            parent.endGame();
        }
    }
    
    /**
     * Opponent move service
     * @see ox.Board#setFieldX
     * @see ox.Board#setFieldO
     * @see ox.Board#checkEnemy
     * @see ox.Board#updateStatus
     */
    public void opponentTurn(){
        if(!yourTurn){
            try{                  
                if(youHaveCircle)
                    parent.setFieldX(dataInput.readInt());
                else
                    parent.setFieldO(dataInput.readInt());
                parent.checkEnemy();
                yourTurn=true;
                parent.updateStatus();
            }
            catch(IOException e){
                lostConnection++;
            }
        }
    }
    
    /**
     * @return
     */
    public boolean isYourTurn(){
        return yourTurn && requestAccepted && !unableToConnect;
    }
    
    /**
     * @param t
     */
    public void setYourTurn(boolean t){
        yourTurn=t;
    }
    
    /**
     * @return
     */
    public boolean haveCircle(){
        return youHaveCircle;
    }
    
    /**
     * Send field data
     * @param f 
     *        Activated field
     */
    public void send(Field f){
        try {
            int x=100 + (f.getPosX()*10)+f.getPosY();
            dataOutput.writeInt(x);
            dataOutput.flush();
        }
        catch (IOException e) {
            lostConnection++;
	}     
    }
    
    /**
     * Send board size to opponent
     */
    public void sendBoardSize(){
        try {
            dataOutput.writeInt(parent.getFields()*10+parent.getWin());
            dataOutput.flush();
        }
        catch (IOException e1) {
            lostConnection++;
	}     
    }
    
    /**
     * Read board size from opponent
     */
    public void readBoardSize(){
        try {
            int x=dataInput.readInt();
            int flds=x/10;
            int ntw=x%10;
            if(!parent.checkBoardSize(flds,ntw))
                parent.newBoard(flds,ntw);
        }
        catch (IOException e1) {
            lostConnection++;
	}     
    }
    
}

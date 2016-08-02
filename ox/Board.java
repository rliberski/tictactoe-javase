/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */

package ox;

//Imports
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
//End of imports

/**
 *Game Board
 *@author R. Liberski
 *@see Field
 *@see NetworkGame
 *@see Window
 *@see CustomButton
 */
public class Board extends JPanel implements ActionListener{

    private final Window parent;
    private int fields;
    private int win;
    private Field[][] mainFields;
    private Position[][] mainLines;
    private final CustomButton back=new CustomButton("Back");
    private JLabel status=new JLabel("...");
    private int maxMoves;
    private int currMoves;
    private NetworkGame ng;
    private AIGame aig;
    private HotSeatGame hsg;
    private boolean aiGameStarted=false;
    private boolean networkGameStarted=false;
    private boolean hotSeatGameStarted=false;
    
    /**
     * Create a board
     * @param parent
     *        Window - parent
     * @param fields
     *        Size of board (length of side) 
     * @param win 
     *        Needed 'o' or 'x' to win
     * @see ox.Window
     * @see ox.Field
     * 
     */
    public Board(Window parent,int fields,int win){
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBackground(new Color(200,200,255));
        
        this.parent=parent;
        this.fields=fields;
        this.win=win;
        this.mainFields=new Field[fields][fields];
                
        for(int i = 0 ;i<fields;i++){
            for(int j=0;j<fields;j++){
                c.gridx=i;
                c.gridy=j;
                this.mainFields[i][j]=new Field(this,i,j);
                add(this.mainFields[i][j],c);
            }
        }
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = fields-1;
        c.gridx=0;
        c.gridy=fields;
        add(this.status,c);
        
        back.setMinimumSize(new Dimension(100,30));
        back.setPreferredSize(new Dimension(100,30));
        back.setMaximumSize(new Dimension(100,30));
        back.setBackground(new Color(50,50,255));
        back.addActionListener(this);
        
        c.gridwidth=1;
        c.gridx=fields-1;
        c.gridy=fields;
        add(this.back,c);
        
        maxMoves=fields*fields;
        currMoves=0;
    }
    
    /**
     * Button "Back" action
     * @param e 
     *      Event
     * @see ox.Board#endGame()
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==back){
            endGame();
        }
    }
    
    /**
     * End of game
     * @see ox.NetworkGame#finish()
     * @see ox.AIGame#finish()
     * @see ox.Window#toStartMenu()
     */
    public void endGame(){
        if(ng!=null){
            ng.finish();
            ng=null;
            networkGameStarted=false;
        }
        if(aig!=null){
            aig.finish();
            aig=null;
            aiGameStarted=false;
        }
        if(hsg!=null){
            hsg.finish();
            hsg=null;
            hotSeatGameStarted=false;
        }
        parent.toStartMenu();
    }
    
    /**
     * Start network game
     * @param ip
     *        IP Address
     * @param port 
     *        Port
     * @see ox.NetworkGame
     * @see ox.NetworkGame#play()
     */
    public void startNetworkGame(String ip,int port){
        ng=new NetworkGame(this,ip,port);
        networkGameStarted=true;
        ng.play();
        
    }
    
    /**
     * Start AI Game
     * @see ox.AIGame
     * @see ox.AIGame#play
     */
    public void startAIGame(){
        
        fillLinesTable();
        
        aig=new AIGame(this);
        aiGameStarted=true;
        aig.play();
    }
    
    /**
     * Start Hot Seat Game
     * @see ox.AIGame
     * @see ox.AIGame#play
     */
    public void startHSGame(){
        
        hsg=new HotSeatGame(this);
        hotSeatGameStarted=true;
        hsg.play();
    }
    
    /**
     * Set field as 'x'
     * @param x 
     *        Field position
     * @see ox.Field#setX() 
     */
    public void setFieldX(int x){
        int a=x/10-10;
        int b=x%10;
        mainFields[a][b].setX();
    }
    
    /**
     * Set field as 'o'
     * @param x 
     *        Field position
     * @see ox.Field#setO()
     */
    public void setFieldO(int x){
        int a=x/10-10;
        int b=x%10;
        mainFields[a][b].setO();
    }
    
    /**
     * Update game status
     * @see ox.NetworkGame#isYourTurn()
     * @see ox.AIGame#isYourTurn()
     */
    public void updateStatus(){
        
        if(networkGameStarted){
            if(ng.isYourTurn())
                status.setText("Your turn");
            else
                status.setText("Wait for opponent");
        }
        if(aiGameStarted){
            if(aig.isYourTurn())
                status.setText("Your turn");
            else
                status.setText("Wait for opponent");
        }
        if(hotSeatGameStarted){
            if(hsg.isCircleTurn())
                status.setText("Circle turn");
            else
                status.setText("Cross turn");
        }
        
    }
    
    /**
     * Check game state afer player move
     * @see ox.NetworkGame#haveCircle()
     * @see #checkWinO
     * @see #checkWinX
     * @see #win()
     * @see #draw()
     */
    public void check(){
        currMoves++;
        if(networkGameStarted){
            if(ng.haveCircle()){
                if(checkWinO(win)){
                    win();
                }
                else if(currMoves==maxMoves){
                    draw();
                }
            }
            else{
                if(checkWinX(win)){
                    win();
                }
                else if(currMoves==maxMoves){
                    draw();
                }
            }
        }
        else if(aiGameStarted || hotSeatGameStarted){
            if(checkWinO(win))
                win();
            else if(currMoves==maxMoves)
                draw();
        }
    }
   
    /**
     * Check state game after enemy move
     * @see ox.NetworkGame#haveCircle()
     * @see #checkWinO
     * @see #checkWinX
     * @see #lose()
     * @see #draw()
     */
    public void checkEnemy(){
        currMoves++;
        if(networkGameStarted){
            if(!ng.haveCircle()){
                if(checkWinO(win)){
                    lose();
                }
                else if(currMoves==maxMoves){
                    draw();
                }
            }
            else{
                if(checkWinX(win)){
                    lose();
                }
                else if(currMoves==maxMoves){
                    draw();
                }
            }
        }
        else if(aiGameStarted){
            if(checkWinX(win))
                lose();
            else if(currMoves==maxMoves)
                draw();
        }
        else if(hotSeatGameStarted){
            if(checkWinX(win))
                win();
            else if(currMoves==maxMoves)
                draw();
        }
    }
    
    /**
     * Check if circles won
     * @param n
     *        Needed 'o' or 'x' to win
     * @return 
     *        True - if circles won
     */
    public boolean checkWinO(int n){
        
        for(int i=0;i<fields;i++){
            int counter=0;
            for(int j=0;j<fields;j++){
                if(mainFields[i][j].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields;i++){
            int counter=0;
            for(int j=0;j<fields;j++){
                if(mainFields[j][i].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[j][fields-1-i+j].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=fields-1;i>=0;i--){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[fields-1-i+j][j].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[j][i-j].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[fields-1-j][fields-1-i+j].isO()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        int counter=0;
        for(int i=0;i<fields;i++){
            if(mainFields[i][fields-1-i].isO()){
                counter++;
            }
            else
                counter=0;
            if(counter==n)
                return true;
        }
        
        counter=0;
        for(int j=0;j<fields;j++){
            if(mainFields[j][j].isO()){
                counter++;
            }
            else
                counter=0;
            if(counter==n)
                return true;
        }
        return false;
    }
    
    /**
     * Check if crosses won
     * @param n
     *        Needed 'o' or 'x' to win
     * @return 
     *        True - if cross wins
     */
    public boolean checkWinX(int n){
       
        for(int i=0;i<fields;i++){
            int counter=0;
            for(int j=0;j<fields;j++){
                if(mainFields[i][j].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields;i++){
            int counter=0;
            for(int j=0;j<fields;j++){
                if(mainFields[j][i].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[j][fields-1-i+j].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=fields-1;i>=0;i--){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[fields-1-i+j][j].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[j][i-j].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        for(int i=0;i<fields-1;i++){
            int counter=0;
            for(int j=0;j<i+1;j++){
                if(mainFields[fields-1-j][fields-1-i+j].isX()){
                    counter++;
                }
                else
                    counter=0;
                if(counter==n)
                    return true;
            }
        }
        
        int counter=0;
        for(int i=0;i<fields;i++){
            if(mainFields[i][fields-1-i].isX()){
                counter++;
            }
            else
                counter=0;
            if(counter==n)
                return true;
        }
        
        counter=0;
        for(int j=0;j<fields;j++){
            if(mainFields[j][j].isX()){
                counter++;
            }
            else
                counter=0;
            if(counter==n)
                return true;
        }
        return false;
    }
    
    /**
     * Player win service
     * @see #endGame
     */
    public void win(){
        if(aiGameStarted){
            aig.finish();
        }
        if(hotSeatGameStarted){
            if(hsg.isCircleTurn())
                JOptionPane.showMessageDialog(this,"Circle Win!");
            else
                JOptionPane.showMessageDialog(this,"Cross Win!");
        }
        else
            JOptionPane.showMessageDialog(this,"You Win!");
        endGame();
    }
    
    /**
     * Player lose service
     * @see #endGame
     */
    public void lose(){
        if(aiGameStarted){
            aig.finish();
        }
        JOptionPane.showMessageDialog(this,"You lose!");
        endGame();
    }
    
    /**
     * Draw service
     * @see #endGame
     */
    public void draw(){
        if(aiGameStarted){
            aig.finish();
        }
        JOptionPane.showMessageDialog(this,"Draw!");
        endGame();
    }
    
    /**
     * @return 
     * @see ox.NetworkGame#isYourTurn
     * @see ox.AIGame#isYourTurn
     * @see ox.Field
     * @see ox.NetworkGame
     * @see ox.AIGame
     */
    public boolean isYourTurn(){
        if(networkGameStarted)
            return ng.isYourTurn();
        else if(aiGameStarted)
            return aig.isYourTurn();
        else 
            return false;
    }
    
    /**
     * @param t
     * @see ox.NetworkGame#setYourTurn
     * @see ox.Field
     * @see ox.NetworkGame
     */
    public void setYourTurn(boolean t){
        if(networkGameStarted)
            ng.setYourTurn(t);
        else if(aiGameStarted)
            aig.setYourTurn(t);
    }
    
    /**
     * @return
     * @see ox.NetworkGame#haveCircle
     * @see ox.Field
     * @see ox.NetworkGame
     */
    public boolean haveCircle(){
        if(networkGameStarted)
            return ng.haveCircle();
        else if(aiGameStarted)
            return true;
        else 
            return false;
    }
    
    /** 
     * @param f see ox.NetworkGame#send
     * @see ox.Field
     * @see ox.NetworkGame
     */
    public void send(Field f){
        if(networkGameStarted)
            ng.send(f);
    }
    
    /**
     * @return fields
     */
    public int getFields(){
        return fields;
    }
    
    /**
     * @return win
     */
    public int getWin(){
        return win;
    }
    
    /**
     * Check if board equals to given parameters
     * @param f
     *        Size of board (length of side)
     * @param n
     *        Needed 'o' or 'x' to win
     * @return 
     *        True - if board equals to given parameters
     */
    public boolean checkBoardSize(int f,int n){
        if(fields!=f || win!=n)
            return false;
        else
            return true;
    }
    
    /**
     * Create new board (called if it's not first board from when app started)
     * @param f
     *        Size of board (length of side)
     * @param w
     *        Needed 'o' or 'x' to win
     */
    public void newBoard(int f,int w){
        
        if(fields==f){
            this.win=w;
            return;
        }            
        
        for(int i=0;i<fields;i++){
            for(int j=0;j<fields;j++){
                mainFields[i][j].setVisible(false);
                mainFields[i][j].removeAll();
                mainFields[i][j]=null;
            }
        }
        
        this.fields=f;
        this.win=w;
        this.mainFields=new Field[fields][fields];
                
        GridBagConstraints c = new GridBagConstraints();
        
        for(int i = 0 ;i<fields;i++){
            for(int j=0;j<fields;j++){
                c.gridx=i;
                c.gridy=j;
                this.mainFields[i][j]=new Field(this,i,j);
                add(this.mainFields[i][j],c);
            }
        }
            
    
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = fields-2;
        c.gridx=0;
        c.gridy=fields;
        add(this.status,c);
        
        back.setMinimumSize(new Dimension(100,100));
        back.setMaximumSize(new Dimension(100,100));
        back.addActionListener(this);
        c.gridwidth=1;
        c.gridx=fields-1;
        c.gridy=fields;
        add(this.back,c);
        
        maxMoves=fields*fields;
        currMoves=0;
        
        repaint();
    }
    
    /**
     * Fill table, which contains all lines in board
     */    
    public void fillLinesTable(){
        if(mainLines!=null)
            mainLines=null;
        
        mainLines=new Position[fields*6-2][fields];
        
        for(int i=0;i<fields;i++){
            for(int j=0;j<fields;j++){
                mainLines[i][j]=new Position(i,j);
            }
        }
        
        for(int i=0;i<fields;i++){
            for(int j=0;j<fields;j++){
                mainLines[i+fields][j]=new Position(j,i);
            }
        }
        
        for(int i=0;i<fields*2-1;i++){
            for(int j=0;j<fields;j++){
                if(isInTable(j,-fields+1+i+j)){
                    mainLines[fields*2+i][j]=new Position(j,-fields+1+i+j);
                }                    
                else{
                    mainLines[fields*2+i][j]=new Position(-1,-1);
                }
            }
        }
        
        for(int i=0;i<fields*2-1;i++){
            for(int j=0;j<fields;j++){
                if(isInTable(i-j,j)){
                    mainLines[fields*4-1+i][j]=new Position(i-j,j);
                }
                else{
                    mainLines[fields*4+i-1][j]=new Position(-1,-1);
                }
            }
        }
        
    }
    
    /**
     * Helpful for fillLinesTable()
     * @param x - indeks X
     * @param y - indeks Y
     * @return true - if index found
     * @see #fillLinesTable
     */
    public boolean isInTable(int x,int y){
        return (x>=0 && x<fields && y>=0 && y<fields);
    }
    
    /**
     * Search board for good move for opponent
     * @see #finalMove
     * @see #blockingMove
     * @see #otherGoodMove
     * @see #anyMove
     */
    public void lookForGoodMove(){
        
        boolean moveDone=false;
        
        Position[] winningLine=null;
        Position[] playerWinningLine=null;
        Position[] possibleGoodLine=null;
        
        for(Position[] line:mainLines){
            int xCounter=0;
            int oCounter=0;
            int emptyCounter=0;
            
            for (Position pos : line) {
                int x = pos.getX();
                int y = pos.getY();

                if (x != -1) {
                    if (mainFields[x][y].isO()) {
                        oCounter++;
                    }
                    if (mainFields[x][y].isX()) {
                        xCounter++;
                    }
                    if (mainFields[x][y].isEmpty()) {
                        emptyCounter++;
                    }
                }
                else{
                    emptyCounter--;
                }
            }

            if (xCounter == win - 1 && oCounter<=fields-win && emptyCounter>=1) {
                    winningLine = line;
            } else if (oCounter == win-1 && xCounter<=fields-win && emptyCounter>=1) {
                    playerWinningLine = line;
            } else if (emptyCounter + xCounter >= win && xCounter >= 1) {
                    possibleGoodLine = line;
            }
  
        }
        
        if (winningLine != null && !moveDone) {
            moveDone = finalMove(winningLine);
        }
        if (playerWinningLine != null && !moveDone) {
            moveDone = blockingMove(playerWinningLine);
        }
        if (possibleGoodLine != null && !moveDone) {
            moveDone = otherGoodMove(possibleGoodLine);
        }
        if (!moveDone) {
            anyMove();
        }
        
    }

    /**
     * Look for final move
     * @param line - tablica pozycji danej linii
     * @return true - jeśli wykonano ruch.
     * @see #lookForGoodMove() 
     */
    public boolean finalMove(Position[] line){
        for(int i=0;i<fields;i++){
            int x=line[i].getX();
            int y=line[i].getY();
            
            if(x!=-1){
                if(i==0){
                    int secondX=line[i+1].getX();
                    int secondY=line[i+1].getY();
                    
                    if(mainFields[secondX][secondY].isX()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                }
                else if(i==fields-1){
                    int firstX=line[i-1].getX();
                    int firstY=line[i-1].getY();
                    
                    if(mainFields[firstX][firstY].isX()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                        
                }
                else{
                    int firstX=line[i-1].getX();
                    int secondX=line[i+1].getX();
                    int firstY=line[i-1].getY();
                    int secondY=line[i+1].getY();
                    
                    if(mainFields[firstX][firstY].isX() || mainFields[secondX][secondY].isX()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                        
                }
                
            }
        }
        
        return false;
    }
    
    /**
     * Look for blocking move
     * @param line - tablica pozycji danej linii
     * @return true - jeśli wykonano ruch.
     * @see #lookForGoodMove() 
     */
    public boolean blockingMove(Position[] line){
        for(int i=0;i<fields;i++){
            int x=line[i].getX();
            int y=line[i].getY();
            
            if(x!=-1){
                if(i==0){
                    int secondX=line[i+1].getX();
                    int secondY=line[i+1].getY();
                    
                    if(mainFields[secondX][secondY].isO()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                }
                else if(i==fields-1){
                    int firstX=line[i-1].getX();
                    int firstY=line[i-1].getY();
                    
                    if(mainFields[firstX][firstY].isO()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                        
                }
                else{
                    int firstX=line[i-1].getX();
                    int secondX=line[i+1].getX();
                    int firstY=line[i-1].getY();
                    int secondY=line[i+1].getY();
                    
                    if(mainFields[firstX][firstY].isO() || mainFields[secondX][secondY].isO()){
                        if(mainFields[x][y].isEmpty()){
                            mainFields[x][y].setX();
                            return true;
                        }
                    }
                        
                }
                
            }
        }
        
        return false;
    }
    
    /**
     * Look for other good move
     * @param line - tablica pozycji danej linii
     * @return true - jeśli wykonano ruch.
     * @see #lookForGoodMove() 
     */
    public boolean otherGoodMove(Position[] line){
        for(Position pos:line){
            int x=pos.getX();
            int y=pos.getY();
            if(mainFields[x][y].isEmpty()){
                mainFields[x][y].setX();
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Do move on any empty field
     * @see #lookForGoodMove() 
     */
    public void anyMove(){
        for(Field[] row:mainFields)
            for(Field f:row)
                if(f.isEmpty()){
                    f.setX();
                    return;
                }
    }
    
    /**
     * @return
     */
    public boolean hsGameStarted(){
        return hotSeatGameStarted;
    }
    
    /**
     * @return
     */
    public boolean isCircleTurn(){
        return hsg.isCircleTurn();
    }
    
    /**
     * Set turn for hot seat game
     * @param t true or false
     */
    public void setCircleTurn(boolean t){
        if(hsg!=null)
            hsg.setCircleTurn(t);
    }
}

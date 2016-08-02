/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */

package ox;

//imports
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
//end of imports

/**
 * Options menu
 * @author R. Liberski
 * @see ox.Board
 */
public class OptionsMenu extends JPanel implements ActionListener{
    
    private final Window parent;
    private final JLabel boardSize = new JLabel("Size of board: ");
    private final JLabel needToWin = new JLabel("Fields needed to win: ");
    private final JTextField boardSizeT = new JTextField("3");
    private final JTextField needToWinT = new JTextField("3");
    private final JLabel ip = new JLabel("IP:");
    private final JTextField ipT = new JTextField("localhost");
    private final JLabel port = new JLabel("Port:");
    private final JTextField portT = new JTextField("12345");
    private final CustomButton ok=new CustomButton("Ok");
    private final CustomButton cancel=new CustomButton("Cancel");
    
    /**
     * Create options menu
     * @param parent 
     */
    public OptionsMenu(Window parent){
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();  
        setBackground(new Color(200,200,255));
        
        this.parent=parent;
        
        boardSizeT.setMaximumSize(new Dimension(60,25));
        needToWinT.setMaximumSize(new Dimension(60,25));
        ipT.setMaximumSize(new Dimension(180,25));
        portT.setMaximumSize(new Dimension(100,25));
        
        boardSizeT.setColumns(18);
        needToWinT.setColumns(18);
        ipT.setColumns(18);
        portT.setColumns(18);
        
        ok.addActionListener(this);
        cancel.addActionListener(this);
        
        boardSize.setAlignmentX(Component.RIGHT_ALIGNMENT);
        boardSizeT.setAlignmentX(Component.CENTER_ALIGNMENT);
        needToWin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        needToWinT.setAlignmentX(Component.CENTER_ALIGNMENT);
        ip.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ipT.setAlignmentX(Component.CENTER_ALIGNMENT);
        port.setAlignmentX(Component.RIGHT_ALIGNMENT);
        portT.setAlignmentX(Component.CENTER_ALIGNMENT);

        c.gridx=0;
        c.gridy=0;
        add(boardSize,c);
        c.gridx=1;
        add(boardSizeT,c);
        
        c.gridx=0;
        c.gridy=1;
        add(needToWin,c);
        c.gridx=1;
        add(needToWinT,c);
        
        c.gridx=0;
        c.gridy=2;
        add(ip,c);
        c.gridx=1;
        add(ipT,c);
        
        c.gridx=0;
        c.gridy=3;
        add(port,c);
        c.gridx=1;
        add(portT,c);
        
        c.gridx=0;
        c.gridy=4;
        add(ok,c);
        c.gridx=1;
        add(cancel,c);
    }
    
    /**
     * Buttons actions
     * @param e Event
     * @see ox.StartMenu
     * @see #checkOptions()
     * @see #saveOptions()
     * @see ox.Window#toStartMenu()
     */
    @Override
    public void actionPerformed(ActionEvent e){
        Object object=e.getSource();
        
        if(object==ok){
            if(checkOptions()){
                saveOptions();
                parent.toStartMenu();
            }
        }
        else if(object==cancel){
            parent.toStartMenu();
        }
        
    }
    
    
    /**
     * Check if data is correct
     * @return True - if data is correct.
     */
    protected boolean checkOptions(){
        
        if(!boardSizeT.getText().isEmpty() && !needToWinT.getText().isEmpty()){
           try{
                //int a = Integer.parseInt(boardSizeT.getText());
                //int b = Integer.parseInt(needToWin.getText());
                
                int a=new Integer(boardSizeT.getText());
                int b=new Integer(needToWinT.getText());
                
                if(a>6)
                    JOptionPane.showMessageDialog(this,"Maximum size of board is 6!","Error",JOptionPane.ERROR_MESSAGE);
                else if(b>a)
                    JOptionPane.showMessageDialog(this,"Fields needed to win can not be bigger than board size!","Error",JOptionPane.ERROR_MESSAGE);
                else if(a<3)
                    JOptionPane.showMessageDialog(this,"Minimum size of board is 3!","Error",JOptionPane.ERROR_MESSAGE);
                else if(b<3)
                    JOptionPane.showMessageDialog(this,"Minimum fields needed to win is 3!","Error",JOptionPane.ERROR_MESSAGE);
                else
                    return true;
                
                return false;
                
           }
           catch(NumberFormatException e){
               JOptionPane.showMessageDialog(this,"Incorrect data!","Error",JOptionPane.ERROR_MESSAGE);
               return false;
           }
        }
        else{
            JOptionPane.showMessageDialog(this,"Please insert data!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Save options
     * @see ox.Window#saveBoardData
     */
    protected void saveOptions(){
        parent.saveBoardData(
                new Integer(boardSizeT.getText()),
                new Integer(needToWinT.getText()),
                ipT.getText(),
                new Integer(portT.getText()));
    }
    
    
    
}

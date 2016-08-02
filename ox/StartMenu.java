/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */
package ox;

//imports
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//end of imports

/**
 * Start menu
 * @author R. Liberski
 * @see ox.OptionsMenu
 * @see ox.Board
 */
public class StartMenu extends JPanel implements ActionListener{

    private Window parent;
    private final CustomButton start=new CustomButton("Start Network Game");
    private final CustomButton startAI=new CustomButton("Start AI Game");
    private final CustomButton startHS=new CustomButton("Start HotSeat Game");
    private final CustomButton options=new CustomButton("Options");
    private final CustomButton quit=new CustomButton("Quit");
    
    /**
     * Create start menu
     * @param parent 
     * @see ox.CustomButton
     */
    public StartMenu(Window parent){
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setBackground(new Color(200,200,255));
        //setBackground(Color.WHITE);
        
        this.parent=parent;
        
        start.addActionListener(this);
        startAI.addActionListener(this);
        startHS.addActionListener(this);
        options.addActionListener(this);
        quit.addActionListener(this);
        
        c.gridx=0;
        c.gridy=0;
        add(start,c);
        c.gridy=1;
        add(startAI,c);
        c.gridy=2;
        add(startHS,c);
        c.gridy=3;
        add(options,c);
        c.gridy=4;
        add(quit,c);
    }
    
    /**
     * Buttons actions
     * @param e Event
     * @see ox.Board
     * @see ox.OptionsMenu
     */
    @Override
    public void actionPerformed(ActionEvent e){
        Object object=e.getSource();
        if(object==start){
            parent.startNetwork();
        }
        else if(object==startAI){
            parent.startAI();
        }
        else if(object==startHS){
            parent.startHS();
        }
        else if(object==options){
            parent.toOptionsMenu();
        }
        else if(object==quit){
            System.exit(0);
        }
    }
    
}

/**
 * TicTacToe
 * Robert Liberski
 * 05.2016
 */

package ox;

//imports
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
//end of imports

/**
 * Just modified JButton
 * @author R.Liberski
 */
public class CustomButton extends JButton{
        
    /**
     * Creates new button
     * @param title 
     *        Text on button
     */
    public CustomButton(String title){
        super(title);
        
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(200,50));
        setMinimumSize(new Dimension(200,50));
        setMaximumSize(new Dimension(200,50));
        
        setBackground(new Color(100,100,255));
        setForeground(Color.WHITE);
    }
    
}

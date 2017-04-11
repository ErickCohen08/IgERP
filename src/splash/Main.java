/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package splash;

//import de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel;
//import javax.swing.JOptionPane;
//import javax.swing.UIManager;

/**
 *
 * @author ErCo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable()
      {
          @Override          
          public void run() 
          {
//              try {
//                    UIManager.setLookAndFeel(new SyntheticaBlueMoonLookAndFeel());
//                } catch (Exception e) {
//                    JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
//                }
              
              new InicioAplicacion().setVisible(true);
          }
      });
    }
}

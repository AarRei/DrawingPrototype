package xv.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import xv.GUI.DrawWindow;

/**
 * 
 * @author Aaron Reiher, Sascha Rothkopf
 * @version 0.0.2
 *
 */
public class Main {
	
	/**
	 * Declares and initializes the DrawWindow.
	 * 
	 * @param args starting options
	 */
	public static void main(String[]args){
		try {
            // Verwenden des System-LookAndFeels
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		DrawWindow d = new DrawWindow();
	}
}

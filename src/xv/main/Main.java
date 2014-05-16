package xv.main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import xv.Canvas.Canvas;
import xv.GUI.DrawWindow;
import xv.Network.Client.ClientMain;

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

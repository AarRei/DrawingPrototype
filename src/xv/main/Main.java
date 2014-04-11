package xv.main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import xv.Canvas.Canvas;
import xv.GUI.DrawWindow;
import xv.Network.Client.ClientMain;

public class Main {
	public static void main(String[]args){
		try {
            //
            // Use the system look and feel for the swing application
            //
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
		/*
		try {
			if(args.length > 0){
				ClientMain net = new ClientMain(InetAddress.getByName(args[0]),Integer.parseInt(args[1]));
			}
			else{
				ClientMain net = new ClientMain(InetAddress.getByName("2a02:908:f640:5a00:224:1dff:fec0:b83f"),7777);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}

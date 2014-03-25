/*
 * open license stuff.
 * This is the main file which will start the two threads for server and client.
 * Right now, it only starts the server thread
 * */

package serverclient;
import globalfunctions.Contact;
import globalfunctions.IpAddress;

import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import GUIObjects.AppWindow;

public class Mainstart 
{
	public static HashMap <String,BlockingQueue<Character>> threadsync = new HashMap <String, BlockingQueue<Character>> ();
	public static HashMap <String,Contact> people = new HashMap <String,Contact> ();
	public static String myid;
	public static String myusername;
	public static DatagramSocket socket;
	public static BlockingQueue<DatagramPacket> Q;
	
	
	public static void main(String[] args)
    {
		Q = new ArrayBlockingQueue<DatagramPacket>(15);
		try {
			socket = new DatagramSocket(3333);
		} catch (SocketException e) {
			System.err.print("Unable to initiate connection: Port maybe in use already");
			System.exit(0);
		}
		myid=IpAddress.IdentityMac();
		myusername = IpAddress.getUserName();
		if (myid==null)
			{
				System.err.print("Network Problems detected!");
				System.exit(0);
			}
			
			PacketSorterThread PS = new PacketSorterThread(Q);
			ShoutThread S = new ShoutThread();//"172.22.30.19", "172.22.30.21");
			ListenThread L =  new ListenThread(Q);
			
			new Thread (PS).start();
			new Thread(L).start();
			new Thread(S).start();
			try
	        {
	        	Thread.sleep(3000);
	        }
	        catch(Exception E)
	        {
	        	System.out.print("Wokenup");
	        }
			
		   /*for (String key : people.keySet()) {
	            Contact value = (Contact) people.get(key);
	            value.printall();
	        }*/
		   //System.out.print("Supposedly printed everything in the hashmap");
			//new Thread(new Server()).start();
			//final Contact person = (Contact) people.get("F07BCB8001D7");
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						//UsernameWindow login = new UsernameWindow();
						@SuppressWarnings("unused")
						AppWindow mainWindow = new AppWindow();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});/*
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						person.StartChat();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});*/
	   //     SendMessage SM = new SendMessage(person, "This is the message I'm sending to you!!!");
	     //   new Thread(SM).start();
    }
}
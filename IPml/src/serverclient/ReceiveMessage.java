package serverclient;

import globalfunctions.Contact;
import globalfunctions.Misc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.net.InetAddress;

import GuiElements.BroadCastReceiver;
import GuiElements.ChatWindowPanelReceiver;
import GuiElements.FileTransferPanel;

public class ReceiveMessage implements Runnable 
{
	String packdetails[] = null;
	//InetAddress ip = null;
	//HashMap <String, Contact> people = null;
	Timestamp t = null;
	
	public ReceiveMessage(String [] packdetails, InetAddress ip, Timestamp t)
	{
		this.packdetails= packdetails;
		this.t=t;
	}
	@Override
	public void run()
	{//packdetails[1]=mac
       
		Thread.currentThread().setName("ReceiveMessageThread");
		Contact person = (Contact) MainStart.people.get(packdetails[1]);
		if(packdetails[0].equals("M")) {
			//packdetails[2]=threadnumber of sending thread
			//packdetails[3]=message
			if(packdetails[3].equals("boo wendy"))
				Misc.playSound("Boo_Wendy.wav");
			if(packdetails[3].equals("respect my authority"))
				Misc.playSound("authoritah.wav");
			ChatWindowPanelReceiver MessagePane = new ChatWindowPanelReceiver(packdetails[3], new SimpleDateFormat("HH:mm:ss").format(t));
			if (person.getWindow().getFocusOwner()==null)
			{
				Misc.playSound("blop.wav");
			}
			person.getWindow().chatconsole(MessagePane);
			try {
				person.getBlockingQ().put(packdetails[1]+"|"+person.getUserName()+"|"+ new SimpleDateFormat("HH:mm:ss").format(t)+"|"+packdetails[3]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
					
		}
		else if(packdetails[0].equals("S")) {
			//packdetails[2]=threadnumber of sending thread
			//packdetails[3]=sendingPanelId
			//packdetails[4]=filesize
			//packdetails[5]=filename
			int sendPanelId = Integer.parseInt(packdetails[3]);
			if(person.getWindow().getFocusOwner()==null)
				Misc.playSound("pindrop.wav");
			FileTransferPanel ftPane = new FileTransferPanel(person,packdetails[5],packdetails[4],sendPanelId, new SimpleDateFormat("HH:mm:ss").format(t),false);
			person.getWindow().chatconsole(ftPane);
		}
		else if (packdetails[0].equals("BM"))
		{//packdetails[2] = message
			BroadCastReceiver bcr = new BroadCastReceiver(person.getUserName(),packdetails[2], new SimpleDateFormat("HH:mm:ss").format(t), person.getColour());
			MainStart.mainWindow.broadcastConsole(bcr);
		}
		else if (packdetails[0].equals("BS"))
		{//packdetails[2]=sendingPanelId
		 //packdetails[3]=fileSize
 		 //packdetails[4]=fileName
			int sendPanelId = Integer.parseInt(packdetails[2]);
			FileTransferPanel ftPane = new FileTransferPanel(person,packdetails[4],packdetails[3],sendPanelId, new SimpleDateFormat("HH:mm:ss").format(t), true);
			if(MainStart.mainWindow.getFocusOwner()==null)
				Misc.playSound("longpop.wav");
			MainStart.mainWindow.broadcastConsole(ftPane);
		}
		
	}
	/*public static void requestFile(char fileType,String filePath, String ipAddress) {
		
		 
   	 byte[] buf = new byte[256];
   	 System.out.println("file path: " + filePath);
   	 
		 buf= new String(fileType+":R-"+filePath).getBytes();
	     InetAddress address = null;
	     
		try {
			address = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     for(int i=0;i<buf.length;i++) {
	    	 System.out.print((char)buf[i]);
	     }
	     DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3333);
	     try {
	    	 DatagramSocket multicastSocket = new DatagramSocket();
			 multicastSocket.send(packet);
			 System.out.println("Packet sent ");
			 multicastSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

}
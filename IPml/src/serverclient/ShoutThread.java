/*
 * This is the client file
 */
package serverclient;
import globalfunctions.IpAddress;

import java.io.*;
import java.net.*;

public class ShoutThread extends Thread 
{
	protected DatagramSocket socket = new DatagramSocket();
	protected String macadd;
	protected String ipadd1=null;
	protected String ipadd2=null;
	protected int portnumber;
	
	public ShoutThread(String macadd, String ipadd1, String ipadd2) throws SocketException
	{
		super("ShoutThread");
		// get a datagram socket
		this.macadd=macadd;
		this.ipadd1=ipadd1;
		this.ipadd2=ipadd2;
		this.portnumber=3333;
	}
	public ShoutThread(String macadd, int portnumber, String ipadd1, String ipadd2) throws SocketException
	{
		super("ShoutThread");
		// get a datagram socket
		this.macadd=macadd;
		this.ipadd1=ipadd1;
		this.ipadd2=ipadd2;
		this.portnumber=portnumber;
	}
	public ShoutThread(String macadd) throws SocketException
	{
		super("ShoutThread");
		// get a datagram socket
		this.macadd=macadd;
		this.portnumber=3333;
	}
	public ShoutThread(String macadd, int portnumber) throws SocketException
	{
		super("ShoutThread");
		// get a datagram socket
		this.macadd=macadd;
		this.portnumber=portnumber;
	}
	
	public void run()
	{  
		
		try
		{
			if(ipadd1!=null && ipadd2!=null)
			{
				for (long ip1=IpAddress.ipToLong(InetAddress.getByName(ipadd1));ip1<=IpAddress.ipToLong(InetAddress.getByName(ipadd2));ip1++)
				{// send request
			        byte[] buf = new byte[256];
			        buf= new String("D:C:"+macadd+":"+System.getProperty("os.name")+":"+InetAddress.getLocalHost().getHostName()+":username").getBytes();
			        InetAddress address = IpAddress.LongToip(ip1);
			        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, portnumber);
			        socket.send(packet);
				}
			}
			else
			{
				   byte[] buf = new byte[256];
				   buf= new String("D:C:"+macadd+":"+System.getProperty("os.name")+":"+InetAddress.getLocalHost().getHostName()+":username").getBytes();
			       InetAddress address = InetAddress.getByName("255.255.255.255");
			       DatagramPacket packet = new DatagramPacket(buf, buf.length, address, portnumber);
			       socket.send(packet);
			}
	        
	        socket.close();
		} 
		catch (UnknownHostException e) 
		{
			System.err.print("Unable to find IP of current machine");
		}
		catch (IOException except)
        {
        	System.err.print("Network Problem : Unable to send packets!");
        }
     
    }
}
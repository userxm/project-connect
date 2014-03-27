package globalfunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import fileSending.Sender;

public class FileTransfer {
	public static String getHeader(Path filePath) throws IOException  {
    	String fileName = filePath.getFileName().toString();
		long fSize =  Files.size(filePath);
		long fileSize= fSize;

		System.out.println("\nFile Size is " + fileSize + " file path " + fileName);
		
		boolean flag=false;
		char pathType= ' ';
		
		flag = isPathValid(filePath.toString());
		if(flag==false) {
			Sender.displayError("Path Not Valid");
		}
		flag = isFile(filePath.toString());
		if(flag==true) {
			pathType = 'f';
		}
		if(flag==false) {
			flag = isDirectory(filePath.toString());
			if(flag==true) {
				pathType = 'd';
			}
		}
		System.out.println("Path type " + pathType);
		
		/* File Header is of the format 
		 * [f/d]*[file/directory Size]-[file/directory path][newline character]
		 * f represents a file
		 * d represents a directory
		 * */
				
		// header contains the content of file header which will be sent to receiver
		String header = pathType + "*" + Long.toString(fileSize) + "-" + filePath +  "\n" ;
		
		// Creation of file header
		return header;
		
    }
	
	public static boolean isPathValid(String filePath) {
		
		if(new File(filePath).exists())
			return true;
		else
			return false;
	}

	public static boolean isDirectory(String filePath) {
		if(new File(filePath).isDirectory())
			return true;
		else
			return false;
	}
	public static  boolean isFile(String filePath) {
		if(new File(filePath).isFile())
			return true;
		else
			return false;
	}



}

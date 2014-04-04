package GuiElements;

/*
 * this panel is for accepting or rejecting the incoming file
 * */
import fileSending.Server;
import fileSending.TestPane;
import globalfunctions.Contact;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;

import java.awt.Color;

import javax.swing.border.TitledBorder;

import serverclient.MainStart;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.border.MatteBorder;
import javax.swing.JToolBar;

public class FileTransferPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JButton btnAccept;
	private JButton btnReject;
	private JButton btnCancel;
	private Thread serverThread;
	private TestPane progBar;
	
	public FileTransferPanel(Contact person, String filepath, int sendPanelId, String timeStamp) {
		
		String filename = fileName(filepath);
		setBackground(Color.WHITE);
		setMaximumSize(new Dimension(3000,1000));
		setPreferredSize(new Dimension(500,120));
		setBorder(new MatteBorder(0, 3, 0, 0, (Color) new Color(0, 0, 102)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{360,70,70};
		gridBagLayout.rowHeights = new int[]{55,25};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		setLayout(gridBagLayout);
		createInsidePanel(person,filepath,sendPanelId,timeStamp, filename);
		
	}

	
	private void createInsidePanel(final Contact person, final String filepath, final int sendPanelId, final String timeStamp, final String filename){
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		panel.setBorder(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{360};
		gbl_panel.rowHeights = new int[]{35,20};
		gbl_panel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		//the filename label is here
		JLabel lbl_fileName = new JLabel("<html>"+filename+"</html>");
		//lbl_fileName.setMinimumSize(new Dimension(300, 50));
		GridBagConstraints gbc_lbl_fileName = new GridBagConstraints();
		gbc_lbl_fileName.anchor = GridBagConstraints.WEST;
		gbc_lbl_fileName.insets = new Insets(0, 0, 5, 0);
		gbc_lbl_fileName.gridx = 0;
		gbc_lbl_fileName.gridy = 0;
		lbl_fileName.setBorder(new TitledBorder(null, "<html>Awaiting response</html>",TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, new Color(128, 128, 128)));
		lbl_fileName.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		panel.add(lbl_fileName, gbc_lbl_fileName);
		
		progBar = new TestPane();
		progBar.setVisible(false);
		GridBagConstraints gbc_progBar = new GridBagConstraints();
		gbc_progBar.anchor= GridBagConstraints.SOUTHWEST;
		gbc_progBar.gridx = 0;
		gbc_progBar.gridy = 1;
		panel.add(progBar, gbc_progBar);
		
		final FileTransferPanel ftp = this;
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setVisible(true);
				fileChooser.setDialogTitle("Select a location to save the file");
				fileChooser.setSelectedFile(new File(filename));
				int returnVal = fileChooser.showSaveDialog(getParent());
				
				
				//int returnVal = fileChooser.showOpenDialog(getParent());
				String SaveAsPath="";
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fileChooser.getSelectedFile();
			            SaveAsPath = file.getAbsolutePath();	
			            System.out.println("SaveAsPath inside Filetransferpanel..." + SaveAsPath);
			        }
		        
		        
				serverThread = new Thread(new Server(6666, ftp,SaveAsPath));// Starts server
				serverThread.start();
				try {
					person.sendAcceptFile(filepath.toString().trim(),MainStart.myID,sendPanelId);
					onAcceptUI();
				} catch (SocketException exc) {
					// Do stuff
					
				} catch (IOException exc) {
					// Do stuff
				}

			}
		});
		GridBagConstraints gbc_btnAccept = new GridBagConstraints();
		gbc_btnAccept.insets = new Insets(0, 0, 5, 5);
		gbc_btnAccept.gridx = 1;
		gbc_btnAccept.gridy = 0;
		add(btnAccept, gbc_btnAccept);
		
		
		
		btnReject = new JButton("Reject");
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					person.sendRejectFile(filepath.toString().trim(), MainStart.myID, sendPanelId);
					onRejectUI();
				} catch (SocketException exc) {
					// Do stuff
					
				} catch (IOException exc) {
					// Do stuff
				}
				
			}
		});
		GridBagConstraints gbc_btnReject = new GridBagConstraints();
		gbc_btnReject.insets = new Insets(0, 5, 5, 0);
		gbc_btnReject.gridx = 2;
		gbc_btnReject.gridy = 0;
		add(btnReject, gbc_btnReject);
	
		btnCancel = new JButton("Cancel");
		btnCancel.setVisible(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 5, 5, 0);
		gbc_btnCancel.gridx = 2;
		gbc_btnCancel.gridy = 0;
		add(btnCancel, gbc_btnCancel);
		
		JLabel lblTimestamp = new JLabel(timeStamp);
		lblTimestamp.setFont(new Font("Ubuntu Light", Font.PLAIN, 10));
		lblTimestamp.setBackground(Color.WHITE);
		
		GridBagConstraints gbc_lblTimestamp = new GridBagConstraints();
		gbc_lblTimestamp.insets = new Insets(0, 0, 0, 0);
		gbc_lblTimestamp.gridx = 0;
		gbc_lblTimestamp.gridy = 1;
		gbc_lblTimestamp.gridwidth=3;
		gbc_lblTimestamp.anchor = GridBagConstraints.NORTHEAST;
		add(lblTimestamp, gbc_lblTimestamp);
	
	
	}//constructor ends here
	
	private void onAcceptUI(){
		java.awt.EventQueue.invokeLater(new Runnable() {
		    public void run() {
		    	btnAccept.setVisible(false);
		    	btnReject.setVisible(false);
				btnCancel.setVisible(true);
				revalidate();
				repaint();
		    }
		} );
	}
	private void onRejectUI(){
		java.awt.EventQueue.invokeLater(new Runnable() {
		    public void run() {
				btnAccept.setVisible(false);
				btnReject.setVisible(false);
				revalidate();
				repaint();
		    }
		} );
	}
	private static String fileName(String filePath) {
		int pos=0;
		for(int i=filePath.length()-1;i>=0;i--) {
			if(filePath.charAt(i)=='\\' || filePath.charAt(i)=='/') {
				pos = i;
				break;
			}
		}
		return filePath.substring(pos+1,filePath.length());
	}
	
	public TestPane getprogbar()
	{
		return progBar;
	}
}

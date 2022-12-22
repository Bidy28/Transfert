package serveur;
import client.Client;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.*;
import java.net.*;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.io.FileOutputStream; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.*;
import javax.swing.JButton;
import java.io.IOException;

public class Serveur{
    static  ArrayList<Mon_fichier> myFiles= new ArrayList<>();
    public static void main(String[] args) throws IOException {

        int fileId= 0;

        JFrame frame = new JFrame();
		frame.setBounds(100, 100, 381, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setVisible(true);
		
		JLabel lblLesFichiersRecus = new JLabel("Les fichiers recus");
		lblLesFichiersRecus.setFont(new Font("Arial", Font.BOLD, 25));
        lblLesFichiersRecus.setAlignmentX(Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(lblLesFichiersRecus);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane);
		
		ServerSocket serversocket= new ServerSocket(2811);
		
		
		while(true){
			try{
				Socket socket= serversocket.accept();

				DataInputStream dataInputStream= new DataInputStream(socket.getInputStream());
				int fileNameLength= dataInputStream.readInt();

				if(fileNameLength>0){
					byte[] fileNameBytes= new byte[fileNameLength];
					dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
					String fileName= new String(fileNameBytes);
					int fileContentLength= dataInputStream.readInt();

					if(fileContentLength>0){
						byte[] fileContentBytes= new byte[fileContentLength];
						dataInputStream.readFully(fileContentBytes, 0 , fileContentLength);

						JPanel pan= new JPanel();
						pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

						JLabel lab= new JLabel(fileName);
						lab.setFont(new Font("Arial", Font.BOLD,20));
						lab.setBorder(new EmptyBorder(10,0,10,0));

							if(getFileExtension(fileName).equalsIgnoreCase("txt")){
								pan.setName(String.valueOf(fileId));
								pan.addMouseListener(getMyMouseListener());

								pan.add(lab);
								panel.add(pan);
								frame.validate();
							}else{
								pan.setName(String.valueOf(fileId));
								pan.addMouseListener(getMyMouseListener());

								pan.add(lab);
								panel.add(pan);
								frame.validate();
							}
						myFiles.add(new Mon_fichier(fileId, fileName, fileContentBytes,getFileExtension(fileName)));
					}
					
				}
			}catch(IOException error){
				error.printStackTrace();
			}
		}
        
    }
	public static MouseListener getMyMouseListener(){
		return new MouseListener(){
			public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
				JPanel panel= (JPanel)e.getSource();
				int fileId= Integer.parseInt(panel.getName());

				for(Mon_fichier myFile: myFiles){
					if(myFile.getId()== fileId){
						JFrame framesuivant= createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
						framesuivant.setVisible(true);
					}
				}
			}

			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension){

		JFrame jFrame= new JFrame();
		jFrame.setSize(400,400);

		JPanel jPanel= new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		JLabel lab= new JLabel("file downloader");
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		lab.setFont(new Font("Arial", Font.BOLD, 25));
		lab.setBorder(new EmptyBorder(20,0,10,0));

		JLabel question= new JLabel("voulez vous vraiment recevoir:"+ fileName);
		question.setAlignmentX(Component.CENTER_ALIGNMENT);
		question.setFont(new Font("Arial", Font.BOLD, 25));
		question.setBorder(new EmptyBorder(20,0,10,0));

		JButton btOui= new JButton("Oui");
		btOui.setPreferredSize(new Dimension(150,75));
		btOui.setFont(new Font("Arial", Font.BOLD, 20));

		JButton btNon= new JButton("Non");
		btNon.setPreferredSize(new Dimension(150,75));
		btNon.setFont(new Font("Arial", Font.BOLD, 20));

		JLabel jlContenu= new JLabel();
		jlContenu.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel panBouton= new JPanel();
		panBouton.setBorder(new EmptyBorder(20,0,10,0));

		panBouton.add(btOui);
		panBouton.add(btNon);

		if(fileExtension.equalsIgnoreCase("txt")){
			jlContenu.setText("<html>"+ new String(fileData) + "</html>");
		}
		// }else{
		// 	jlContenu.setIcon(new ImageIcon(fileData));
		// }

		btOui.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					File fileToDownload= new File(fileName);
						try{
							FileOutputStream fileOutputStream= new FileOutputStream(fileToDownload);

							fileOutputStream.write(fileData);
							fileOutputStream.close();

							jFrame.dispose();
						}catch(IOException error){
							error.printStackTrace();
						}

			}
		});

		btNon.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					jFrame.dispose();
			}
		});

		jPanel.add(lab);
		jPanel.add(question);
		jPanel.add(jlContenu);
		jPanel.add(panBouton);

		jFrame.add(jPanel);

		return jFrame;


	}

	public static String getFileExtension(String fileName){
		int i = fileName.lastIndexOf('.');

		if(i>0){
			return fileName.substring(i + 1);
		}
		else{
			return "Tsy hita ny ext";
		}
	}

    
       
    
 
   
}

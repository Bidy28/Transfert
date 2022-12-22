package client;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.io.*;
import javax.swing.JFileChooser;
import java.net.*;
import serveur.*;
public class Client{

    public static void main(String[] args) {
        final File[] filetosend= new File[1];
        
        JFrame frame = new JFrame();
		frame.setBounds(100, 100, 403, 423);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.setVisible(true);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 255)));
		panel.setBounds(31, 21, 329, 140);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("Choisissez un fichier");
		label.setBounds(65, 33, 223, 67);
		panel.add(label);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 255)));
		panel_1.setBounds(31, 199, 329, 140);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JButton btEnvoye = new JButton("Envoyer");
		btEnvoye.setBounds(44, 36, 89, 65);
		panel_1.add(btEnvoye);
		
		JButton btChoisir = new JButton("choisir");
		btChoisir.setBounds(182, 36, 89, 65);
		panel_1.add(btChoisir);
        
        btChoisir.addActionListener(new ActionListener(){
        
            public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
                JFileChooser jFileChooser= new JFileChooser();
                jFileChooser.setDialogTitle("Choisisser un fichier");
                if(jFileChooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION){
                    filetosend[0]= jFileChooser.getSelectedFile();
                    label.setText("le fichier que vous avez choisis est:"+ filetosend[0].getName());
                }
            }
        });

        btEnvoye.addActionListener(new ActionListener(){
    
            public void actionPerformed(ActionEvent e) {
		    // TODO Auto-generated method stub
                if(filetosend[0]==null){
                    label.setText("Misafidiana zavatra alefa azafady");
                }
                else{
                    try{
                        FileInputStream fileInput= new FileInputStream(filetosend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 2811);
                        DataOutputStream dataOutput= new DataOutputStream(socket.getOutputStream());

                        String fileName= filetosend[0].getName();
                        byte[] fileNameBytes= fileName.getBytes();

                        byte[] fileContentBytes= new byte[(int) filetosend[0].length()];
                        fileInput.read(fileContentBytes);
                        dataOutput.writeInt(fileNameBytes.length);
                        dataOutput.write(fileNameBytes);

                        dataOutput.writeInt(fileContentBytes.length);
                        dataOutput.write(fileContentBytes);
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                                        }
                }
		
	        }

        });


        
    }

}
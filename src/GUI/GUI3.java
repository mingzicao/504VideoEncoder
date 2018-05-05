package GUI;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jpeg.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.SystemColor;
import java.awt.Color;



public class GUI3 extends JFrame{
	
    private JFrame frame = new JFrame(); // Main Frame
    JButton button ;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JLabel label;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JLabel label5;
    JTextField textField;
    JTextField textField2;
    JList list;
    JComboBox combobox;
    JComboBox combobox2;
    JScrollPane scrollPane;
    JProgressBar progressBar;
    JProgressBar progressBar2;
    Timer t1;
    Timer t2;
    int counter1;
    int counter2;
    List<File> fileList = new ArrayList<>();
    String TXTFile;

    
    public GUI3(){
    	
    button = new JButton("Browse");
    button.setForeground(new Color(119, 136, 153));
    button.setBackground(new Color(0, 0, 0));
    button.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    button.setBounds(6,283,155,40);
    button2 = new JButton("Let's encode!");
    button2.setForeground(new Color(119, 136, 153));
    button2.setBackground(Color.WHITE);
    button2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
    button2.setBounds(181, 422, 245, 149);
    button3 = new JButton("Then decode!");
    button3.setForeground(new Color(119, 136, 153));
    button3.setBackground(Color.WHITE);
    button3.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
    button3.setBounds(531, 34, 362, 250);
    button4 = new JButton("Play Video!");
    button4.setForeground(new Color(119, 136, 153));
    button4.setBackground(Color.WHITE);
    button4.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
    button4.setBounds(531, 361, 362, 267);
    button5 = new JButton("Browse");
    button5.setForeground(new Color(119, 136, 153));
    button5.setBackground(Color.WHITE);
    button5.setFont(new Font("Lucida Grande", Font.BOLD , 10));
    button5.setBounds(531, 5, 155, 30);
    label = new JLabel();
    label.setBounds(6,34,420,250);
    label2 = new JLabel("  Please select a image folder.");
    label2.setForeground(new Color(119, 136, 153));
    label2.setBackground(Color.WHITE);
    label2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    label2.setBounds(1,1,453,25);
    label3 = new JLabel("Sample Ratio:");
    label3.setForeground(new Color(119, 136, 153));
    label3.setBackground(Color.WHITE);
    label3.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    label3.setBounds(181, 366, 120, 16);
    label4 = new JLabel("EncodedStream.txt");
    label4.setForeground(new Color(119, 136, 153));
    label4.setBackground(Color.WHITE);
    label4.setBounds(246, 593, 124, 45);
    label5 = new JLabel("Quality Factor:");
    label5.setForeground(new Color(119, 136, 153));
    label5.setBackground(Color.WHITE);
    label5.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    label5.setBounds(181, 397, 120, 16);
    textField = new JTextField();
    textField.setBounds(173, 282, 253, 40);
    textField.setColumns(10);
    textField2 = new JTextField();
    textField2.setBounds(700, 5, 193, 30);
    textField2.setColumns(10);
    list = new JList();
    list.setBackground(Color.WHITE);
    scrollPane = new JScrollPane();
    scrollPane.setBounds(16, 364, 145, 264);
    combobox=new JComboBox();  
    combobox.setBounds(302, 355, 88, 40);
    combobox.addItem("4:2:0");
    combobox.addItem("4:2:2");
    combobox.addItem("4:2:4");
    combobox2=new JComboBox();  
    combobox2.setBounds(302, 390, 88, 40);
    combobox2.addItem("10");
    combobox2.addItem("20");
    combobox2.addItem("30");
    combobox2.addItem("40");
    combobox2.addItem("50");
    combobox2.addItem("60");
    combobox2.addItem("70");
    combobox2.addItem("80");
    combobox2.addItem("90");
    progressBar = new JProgressBar();
    progressBar.setBounds(541, 296, 341, 33);
    progressBar2 = new JProgressBar();
    progressBar2.setBounds(191, 571, 220, 33);

    
    


   
   
    frame.setContentPane(new JLabel(new ImageIcon("11.jpg")));
    frame.getContentPane().add(button);
    frame.getContentPane().add(button2);
    frame.getContentPane().add(button3);
    frame.getContentPane().add(button4);
    frame.getContentPane().add(button5);
    frame.getContentPane().add(label);
    frame.getContentPane().add(label2);
    frame.getContentPane().add(label3);
    frame.getContentPane().add(label4);
    frame.getContentPane().add(label5);
    frame.getContentPane().add(textField);
    frame.getContentPane().add(textField2);
    frame.getContentPane().add(scrollPane);
    scrollPane.setViewportView(list);
    frame.getContentPane().add(combobox);
    frame.getContentPane().add(combobox2);
    frame.getContentPane().add(progressBar);
    frame.getContentPane().add(progressBar2);
    
    
    
    
    frame.getContentPane().setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(930,700);  // set screen window size
    frame.setVisible(true);
    
    combobox.addActionListener(new ActionListener() {
   	public void actionPerformed(ActionEvent e) {
   	    String s= combobox.getSelectedItem().toString();  
   	    System.out.println(s);
	}
    });
    
    combobox2.addActionListener(new ActionListener() {
   	public void actionPerformed(ActionEvent e) {
   	    String s2= combobox2.getSelectedItem().toString();  
   	    System.out.println(s2);
	}
    });
    

        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            
              JFileChooser file = new JFileChooser();
              file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
              file.setCurrentDirectory(new File(System.getProperty("user.home")));
              int result = file.showSaveDialog(null);
               //if the user click on save in Jfilechooser
              if(result == JFileChooser.APPROVE_OPTION){
                  File selectedFile = file.getSelectedFile();
                  String path = selectedFile.getAbsolutePath();
                  textField.setText(path);
                  File getFile = new File(path);
                  File[] files = getFile.listFiles();
                  if(files == null) {
                	  	JOptionPane.showMessageDialog(null, "No File Exists!");
                  }
          		int bound = files.length;
          		String readPath = "";
          		for(int i = 0; i < bound - 1; i++) {
          			readPath = path + "/test" + i + ".jpg";
          			System.out.println(readPath);
          			File f = new File(readPath);
          			fileList.add(f);
          		}
                    
                  DefaultListModel DLM = new DefaultListModel();  // add image names
    				for(File f : fileList) {
    					DLM.addElement(f.getName());
    				}
    				label.setIcon(ResizeImage(fileList.get(0).getAbsolutePath()));
                    frame.getContentPane().setLayout(null);
    				list.setModel(DLM);
                  

              }

              else if(result == JFileChooser.CANCEL_OPTION){
            	  
            	  JOptionPane.showMessageDialog(null, "No File Select!");
              }
              
            }
        });
    

      //set the timer t1
        t1 = new Timer(200, new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			

    			if(counter1 == 101) {
    				t1.stop();
    				//button2.setEnabled(true);
    			}
    			else {
    				counter1++;
    				progressBar2.setValue(counter1);
    				progressBar2.setStringPainted(true);
    				//prg2.setValue(i);
    			}
    		}
    	});
        
        button2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    frame.add(label4);
        		counter1 = 0;
        		System.out.println("start");
        		t1.start();
        		System.out.println("end");
        		button2.setEnabled(false);
        		File[] files = new File[fileList.size()];
        		for(int i = 0; i < fileList.size(); i++) {
        			files[i] = fileList.get(i);
        		}
        		System.out.println(files.length);
        		try {
					readImages_1.encoderun(files, 1, 80);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        }); 
        
        
        //set the timer
        t2 = new Timer(100, new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			

    			if(counter2 == 101) {
    				t2.stop();
    				//button2.setEnabled(true);
    			}
    			else {
    				counter2++;
    				progressBar.setValue(counter2);
    				progressBar.setStringPainted(true);
    				//prg2.setValue(i);
    			}
    		}
    	});
       
        
        button3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    //frame.add(label4);
        		counter2 = 0;
        		t2.start();
        		button3.setEnabled(false);
        		File f = new File(TXTFile);
        		Decode de;
				try {
					de = new Decode(f);
					de.write();
					//System.out.println(fileList.size());
					File getFile = new File("output/");
					File[] files = getFile.listFiles();
					FilesToMov.genMovie("output",de.imageHeight, de.imageWidth, files.length - 1);
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        }); 
          
    
    button4.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	GUI2 test = new GUI2();
    	String[] args = new String[] {};
    	test.playOut(args);
    	}
    });
    
    
    button5.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
        
          JFileChooser file = new JFileChooser();
          file.setCurrentDirectory(new File(System.getProperty("user.home")));
          //filter the files with .txt
          FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt");
          file.addChoosableFileFilter(filter);
          int result = file.showSaveDialog(null);
          
           //if the user click on save in Jfilechooser
          if(result == JFileChooser.APPROVE_OPTION){
              File selectedFile = file.getSelectedFile();
              TXTFile = selectedFile.getAbsolutePath();
              textField2.setText(TXTFile);                    
          }
          else if(result == JFileChooser.CANCEL_OPTION){
        	  
        	  JOptionPane.showMessageDialog(null, "No File Select!");
          }
          
        }
    });

    }
     
     // Method to resize imageIcon with the same size of a Jlabel
    public ImageIcon ResizeImage(String ImagePath)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    
    public static void main(String[] args){
        new GUI3();
    }
    
   }
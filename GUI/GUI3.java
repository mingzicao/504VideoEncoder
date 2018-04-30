import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;



public class GUI3 extends JFrame{
	
    JButton button ;
    JButton button2;
    JButton button3;
    JLabel label;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JTextField textField;
    JList list;
    JComboBox combobox;
    JScrollPane scrollPane;
    JProgressBar progressBar;
    JProgressBar progressBar2;
    Timer t;
    int i;
	final static int interval = 1000;



    private JFrame frame = new JFrame(); // Main Frame
    //private JProgressBar progressBar;
;

    
    public GUI3(){
    button = new JButton("Browse");
    button.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    button.setBounds(6,283,155,40);
    button2 = new JButton("Let's encode!");
    button2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
    button2.setBounds(181, 422, 245, 149);
    button3 = new JButton("Then decode!");
    button3.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 25));
    button3.setBounds(531, 34, 362, 250);
    label = new JLabel();
    label.setBounds(6,34,420,250);
    label2 = new JLabel("  Please select one image, I will upload all the images in the folder!");
    label2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    label2.setBounds(1,1,453,25);
    label3 = new JLabel("Quality:");
    label3.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    label3.setBounds(181, 366, 61, 16);
    label4 = new JLabel("EncodedStream.txt");
    label4.setBounds(246, 593, 124, 45);
    textField = new JTextField();
    textField.setBounds(173, 282, 253, 40);
    textField.setColumns(10);
    list = new JList();
    scrollPane = new JScrollPane();
    scrollPane.setBounds(16, 364, 145, 264);
    combobox = new JComboBox();
    combobox.setBounds(234, 355, 88, 40);
    combobox.addItem('1');
    combobox.addItem('2');
    combobox.addItem('3');
    progressBar = new JProgressBar();
    progressBar.setBounds(541, 296, 341, 33);
    progressBar2 = new JProgressBar();
    progressBar2.setBounds(191, 571, 220, 33);
//    progressBar.setValue(0);
//    progressBar.setStringPainted(true);
//    Border border = BorderFactory.createTitledBorder("Reading...");
//    progressBar.setBorder(border);
//    frame.getContentPane().add(progressBar, BorderLayout.NORTH);
//    progressBar.setValue((0));
    //progressBar_1 = new JProgressBar();
    
    


   
    


    
    
    //frame.setContentPane(new JLabel(new ImageIcon("3.jpg")));
    frame.getContentPane().add(button);
    frame.getContentPane().add(button2);
    frame.getContentPane().add(button3);
    frame.getContentPane().add(label);
    frame.getContentPane().add(label2);
    frame.getContentPane().add(label3);
    frame.getContentPane().add(label4);
    frame.getContentPane().add(textField);
    frame.getContentPane().add(scrollPane);
    scrollPane.setViewportView(list);
    frame.getContentPane().add(combobox);
    frame.getContentPane().add(progressBar);
    frame.getContentPane().add(progressBar2);

    
    

    

    
    
    button.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
        
          JFileChooser file = new JFileChooser();
          file.setCurrentDirectory(new File(System.getProperty("user.home")));
          //filter the files
          FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
          file.addChoosableFileFilter(filter);
          int result = file.showSaveDialog(null);
           //if the user click on save in Jfilechooser
          if(result == JFileChooser.APPROVE_OPTION){
              File selectedFile = file.getSelectedFile();
              String path = selectedFile.getAbsolutePath();
              label.setIcon(ResizeImage(path));
              frame.getContentPane().setLayout(null);
              textField.setText(path);
              
              DefaultListModel DLM = new DefaultListModel();  // add image names
				DLM.addElement('a');
				DLM.addElement('b');
				DLM.addElement('c');
				DLM.addElement('d');
				DLM.addElement('e');
				DLM.addElement('f');
				DLM.addElement('g');
				DLM.addElement('h');
				DLM.addElement('i');
				DLM.addElement('g');
				DLM.addElement('k');
				DLM.addElement('l');
				DLM.addElement('m');
				DLM.addElement('m');
				DLM.addElement('m');
				DLM.addElement('m');
				DLM.addElement('m');
				list.setModel(DLM);
              

          }
           //if the user click on save in Jfilechooser


          else if(result == JFileChooser.CANCEL_OPTION){
        	  
        	  JOptionPane.showMessageDialog(null, "No File Select!");
        	  //System.out.println("No File Select");
          }
          //frame.dispose();
          //frame.setVisible(false);
		  //secondpage process = new secondpage();
		  //process.setVisible(true);
        }
    });
    
    //set the timer
//    t = new Timer(interval, new ActionListener() {
//		public void actionPerformed(ActionEvent ae) {
//			
//			if(i == 50) {
//				t.stop();
//				//button2.setEnabled(true);
//			}
//			else {
//				i++;
//				progressBar.setValue(i);
//				//prg2.setValue(i);
//			}
//		}
//	});
    
    button2.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
            progressBar2.setValue(0);
            progressBar2.setStringPainted(true);
            Border border = BorderFactory.createTitledBorder("Reading");
            progressBar2.setBorder(border);
            frame.getContentPane().add(progressBar2, BorderLayout.NORTH);
            int num = 0;
            while(num < 101) {
            	progressBar2.setValue(num);
            	//try {Thread.sleep(100000);}catch (InterruptedException e1) { }
            	num += 1;
            }

    	}
    }); 
    
    
    button3.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		
    		//i = 0;
    		//t.start();
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            Border border = BorderFactory.createTitledBorder("Reading");
            progressBar.setBorder(border);
            frame.getContentPane().add(progressBar, BorderLayout.NORTH);
            int num = 0;
            while(num < 101) {
            	progressBar.setValue(num);
            	//try {Thread.sleep(100000);}catch (InterruptedException e1) { }
            	num += 1;
            }
////			GUI33 process = new GUI33();
////			process.setVisible(true);
//    		int num = 0;
////    		
////    	    Container content = frame.getContentPane();
////    	    JProgressBar progressBar = new JProgressBar();
//    	    progressBar.setStringPainted(true);
//    	    //Border border = BorderFactory.createTitledBorder("Reading...");
//    	    //progressBar.setBorder(border);
//    	    while(num < 101) {
//    	    	progressBar.setValue(num);
//    	    	try {
//    	    		Thread.sleep(50);
//    	    		} catch (InterruptedException e1) { 
//    	  	    }
//    	    	num += 1;
//    	    }
    	}
    });
    
    frame.getContentPane().setLayout(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(2560,1600);  // set screen window size
    frame.setVisible(true);
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
package mpeg;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Player {

	float timePerFrame;
	Movie m;
	BufferedImage images[];
	ImageIcon icons[];
	Image set[];
	public static int frameHeight=1366;
	public static int frameWidth=768;
	public void openMovie(String fileName){
		this.m = null;

	      try
	      {
	         FileInputStream fileIn = new FileInputStream(fileName);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         m = (Movie) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Movie class not found");
	         c.printStackTrace();
	         return;
	      }
	}
	
	public void paint(Graphics g){
		g.drawImage(images[0], 0, 0, 0, 0, 0, 0, 0, 0, null, null);
	}
	
	
	public void buildBufferedImages(Frame[] f) throws IOException{
		int height = m.frames[0].height;
		int width = m.frames[0].width;
		System.out.println("h:" + height + " w:" + width);
		while(height > frameHeight || width > frameWidth){
			height = (int)(height * .99);
			width = (int)(width * .99);
		}
		System.out.println("h:" + height + " w:" + width);
		images = new BufferedImage[f.length];
		icons = new ImageIcon[f.length];
		for(int i = 0; i < f.length;i++){
			images[i] = ImageIO.read(new ByteArrayInputStream(f[i].bytearray));
			icons[i] = new ImageIcon(images[i]);
			icons[i] = new ImageIcon(icons[i].getImage().getScaledInstance(width,height,java.awt.Image.SCALE_SMOOTH));
		}
		//icons[i] = new ImageIcon(images[i]);
		//set[i] = icons[i].getImage().getScaledInstance(500, 500,java.awt.Image.SCALE_SMOOTH);
		//icons[i] = new ImageIcon(set[i]);
		
	}
	
	public void playMovie() throws InterruptedException{

		
		ImageIcon icon;
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(frameHeight,frameWidth);
		JLabel lbl = new JLabel();
		lbl.setIcon(this.icons[0]);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int i = 0;
		//ImageIcon next = this.icons[1];
		while(i < 10000000){
			icon = (this.icons[i%m.frames.length]); 
			Thread.sleep(40);
			lbl.setIcon(icon);
			frame.add(lbl);
			i++;
		}
		
	}
	
	public void playMovie2(){
		JFrame frame = new JFrame("Player 2");
		frame.setSize(600,600);
		frame.setLayout(new FlowLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image img1 = images[0];
		panel.getGraphics().drawImage(img1, 0, 0, panel);
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException{
		Player p = new Player();
		
		p.openMovie(System.getProperty("user.dir")+"/data/movies/movie5.ser");
		System.out.println("This movie has " + p.m.frames.length + " frames");
		p.buildBufferedImages(p.m.frames);
		p.playMovie();
		//p.playMovie2();
	}
	
}

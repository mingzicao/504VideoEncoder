package jpeg;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jpeg.YuvImage.Component;



public class YuvImage {
	
	   static final int Y_COMP = 0; 
	   static final int Cb_COMP = 1; 
	   static final int Cr_COMP = 2; 
		
	   public Component Y; 
	   public Component Cb; 
	   public Component Cr; 
	   public int samplingRatio; 

	   public YuvImage(Component Y, Component Cb, Component Cr, int sr) { 
	       if (Sampler.checkValidSamplingRatio(sr)) { 
	           this.Y = Y; 
	           this.Cb = Cb; 
	           this.Cr = Cr; 
	           this.samplingRatio = sr; 
	       } 
	       else 
	           throw new IllegalArgumentException("Invalid sampling ratio."); 
	   } 
	   public static void showComponent(Component Y) {
		   System.out.println(Y.getType());
		 
		   int[][] data = Y.getData();
		   System.out.println("Dimension is " + "  " + data.length +"   " +data[0].length);
		   for (int[] d : data) {
			   	for (int i : d) {
			   	  System.out.print(i+" ");
			   	}
			   	System.out.println();
		   }
	   }
	 
	   public static YuvImage rgbToYuv(Image rgbImg, int sr) { 
		   try { 
			   int w = rgbImg.getWidth(null); 
			   int h = rgbImg.getHeight(null); 
			   int N = w*h;
			   int[] info = new int[N]; 
	           PixelGrabber grabber = new PixelGrabber(rgbImg,0,0,w,h,info,0,w); 
	           //init three component then init a new YUV image;
	           Component Y = new Component(new int[h][w], 0);
        	   Component Cb = new Component(new int[h][w], 1);
        	   Component Cr = new Component(new int[h][w], 2);
        	   
	           if (grabber.grabPixels())   {
	        	   // fill in the Component data from Rgb INFO
	        	   for (int i = 0; i < info.length; i++) {
	        		   int r = (info[i] >> 16) & 0xff;  // extract three compoent from Object
	   	               int g = (info[i] >> 8) & 0xff; 
	   	               int b = info[i] & 0xff;
	   	               int m  = i/w;
	   	               int n = i%w;
		   	            Y.data[m][n] = (int) (0.299f*r + 0.587f*g + 0.114f*b); 
			            Cb.data[m][n] = 128 + (int) (-0.1687f*r - 0.3313f*g + 0.5f*b); 
			            Cr.data[m][n] = 128 + (int) (0.5f*r - 0.4187f*g - 0.0813f*b); 	   	               
	        	   }   
	           }
	           System.out.println("Before Sampling ");
	           showComponent(Y);
	           showComponent(Cb);
	           showComponent(Cr);
	           
	           return new YuvImage(Y,Cb,Cr, sr); 
	        } 
	        catch (Exception e) {} 
	        return null; 
	    } 
	 /*
	  * Component is a nested class in YUV Image;
	  * YuvImage has three Components: Y Cr cb 
	  * So 
	  * */   
	public static class Component { 
		   int[][] data;  // each Component's has its own 2D array;
		   int type; 
		    Component(int[][] data, int type) { 
		       if (data != null) { 
		           if (YuvImage.checkComponentType(type)) { 
		               this.data = data; 
		               this.type = type; 
		           } 
		           else 
		        	   throw new IllegalArgumentException("Invalid Component type. Should be Y / Cr / Cb"); 
		       } 
		       else 
		           throw new IllegalArgumentException("Component data must not be null."); 
		   } 

		   public Dimension getSize() { 
		       return new Dimension(data[0].length,  data.length); 
		   } 

		   public int[][] getData() { 
		       return data; 
		   } 

		   public int getType() { 
		       return YuvImage.getType(type); 
		   } 
		
	}
	 public static boolean checkComponentType(int type) {
		   if (type == Y_COMP ||type == Cb_COMP || type == Cr_COMP ) {
			   return true;
		   } 
		   return false;		  
	}
	
	public static int getType(int type) {
		 switch (type) { 
	         case 0:  System.out.println("Y Type"); return type; 
	         case 1:  System.out.println("Cb Type"); return type;
	         case 2:  System.out.println("Cr Type"); return type;
	         default:      throw new IllegalArgumentException("Invalid Sampling Ratio "); 
		}
	}
	
	public static void main(String[] args) {
		
		File f = null;
	    //read image
	    try{
	      f = new File("/Users/apple/Desktop/hihi.png"); //image file path
	      Image image = ImageIO.read(f);
	      BufferedImage buffered = (BufferedImage) image;
	      System.out.println("Reading complete.");
	      SizeTrimer st = new SizeTrimer();
	      st.resizeImage(image, 1);
	      YuvImage yuv = YuvImage.rgbToYuv(image, 1);
	      
	    }
	    catch(IOException e){
	      System.out.println("Error: "+e);
	    }
	}
	public Component getComponent(int t) {
		Component c = null;
		if (t == YuvImage.Y_COMP) {
			c = Y;
		} else if (t == YuvImage.Cb_COMP) {
			c = Cb;
		} else if (t == YuvImage.Cr_COMP) {
			c = Cr;
		} else {
			 throw new IllegalArgumentException("Invalid Component type"); 
		}
		return c;
	}
	
}

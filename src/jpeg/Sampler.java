package jpeg;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jpeg.YuvImage.Component;

public class Sampler {
	
		static final int YUV_444 = 0; 
		static final int YUV_422 = 1; 
		static final int YUV_420 = 2;
		   
		public static boolean checkValidSamplingRatio(int sr) {
			if (sr == YUV_444 || sr ==  YUV_422 || sr == YUV_420) {
				return true;
			}
			return false;
		}

		public YuvImage sampling(YuvImage yuv, int sr) {
			if (sr == YUV_444) {
				return yuv;
			}
			Component Y = new Component(yuv.Y.data, YuvImage.Y_COMP);
			Component Cb = samplingComponent(yuv.Cb, sr, YuvImage.Cb_COMP);
			Component Cr = samplingComponent(yuv.Cr, sr, YuvImage.Cr_COMP);
			return new YuvImage(Y, Cb, Cr, sr); 	
		}
	
		private Component samplingComponent(Component component, int sr,  int compType) { 
			  Dimension old_dim = component.getSize(); 
			//  System.out.println(" old dimention: w h" +" "+ old_dim.width +" " +old_dim.height +" ");
			  Dimension new_dim = null;
//resize(old_dim, sr);			
			   if (sr == YUV_422) {
				  System.out.println(" sampling ratio : 4:2:2");
				  new_dim =  new Dimension(old_dim.width / 2, old_dim.height); 
			  } else if (sr == YUV_420) {
				  System.out.println(" sampling ratio : 4:2:0");
				  new_dim =  new Dimension(old_dim.width / 2, old_dim.height/2); 
			  } else {
				  new_dim = old_dim;
			  }
			  
			//  System.out.println(" new dimention: w h" +" "+ new_dim.width +" " +new_dim.height +" ");
			  int[][] old_data = component.getData();
			  int[][] new_data = new int [new_dim.height][new_dim.width];
			  Dimension sampleDistance = getSampleDistances(sr);
			  
			 // System.out.println("sample distance " +" "+ sampleDistance.width +" " +sampleDistance.width +" ");
			  int S = sampleDistance.height * sampleDistance.width; 
			 
			  for (int i = 0; i < old_dim.height; i++) { 
				   for (int j = 0; j < old_dim.width; j++) { 
					   new_data[i / sampleDistance.height][j / sampleDistance.width] += old_data[i][j]; 
				   } 
			  } 
			  for (int i = 0; i < new_dim.height; i++) { 
				   for (int j = 0; j < new_dim.width; j++) { 
					   new_data[i][j]  /= S; 
				   } 
			  } 
			  
			  return new Component(new_data, compType); 			  
		} 

	 private Dimension getSampleDistances(int samplingRatio) { 
		  Dimension ret; 	   
		  switch (samplingRatio) { 
		  case YUV_444: 
		   ret = new Dimension(1, 1); 
		   break; 
		  case YUV_420: 
		   ret = new Dimension(2, 2); 
		   break; 
		  case YUV_422: 
		   ret = new Dimension(2, 1); 
		   break; 
		  default: 
		   ret = new Dimension(1, 1); 
		   break; 
		  } 	   
		  return ret; 
	} 
	 
	 public static void main(String[] args) {
			
			File f = null;
		    //read image
		    try{
		      f = new File("/Users/apple/Desktop/hihi.png"); //image file path
		      Image image = ImageIO.read(f);
		      BufferedImage buffered = (BufferedImage) image;
		      System.out.println("Reading complete.");
		      System.out.println("old IMAGE H" + image.getHeight(null));
		      System.out.println("old IMAGE W" + image.getWidth(null));
		      SizeTrimer st = new SizeTrimer();
		      image = st.resizeImage(image, 1);
		      System.out.println("new IMAGE H" + image.getHeight(null));
		      System.out.println("new IMAGE W" + image.getWidth(null));
		      YuvImage yuv = YuvImage.rgbToYuv(image);
		      Sampler sp = new Sampler();
		      yuv = sp.sampling(yuv, 2);
		      YuvImage.showComponent(yuv.Y);
		      YuvImage.showComponent(yuv.Cb);
		      YuvImage.showComponent(yuv.Cr);
		    }
		    catch(IOException e){
		      System.out.println("Error: "+e);
		    }
		}
	 

}

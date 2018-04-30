package jpeg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import jpeg.*;
import jpeg.YuvImage.Component;



public class ImageGrid {
	
	 private int h;
	 private int w;
	 private int SamplingRatio;
	 public MCU[] imageGridder(YuvImage yuvImg) { 	 
		  
		   Component Y = yuvImg.getComponent(YuvImage.Y_COMP);		 			
	        this.h = Y.getSize().height;
	        this.w = Y.getSize().width;
	        this.SamplingRatio = yuvImg.samplingRatio;
	       List<MCU> list;
	       if (yuvImg.samplingRatio ==  Sampler.YUV_444 && w % 8 == 0 && h % 8 == 0) {
		    	list = sample444(yuvImg);
			    MCU[] mcuArray = new MCU[list.size()]; 
			    mcuArray = list.toArray(mcuArray); 
			    return mcuArray;
	       } else if (yuvImg.samplingRatio ==  Sampler.YUV_422 && Y.getSize().width % 16 == 0 && Y.getSize().height % 16 == 0) {
	    	   list = sample422(yuvImg);       
		       MCU[] mcuArray = new MCU[list.size()]; 
		       mcuArray = list.toArray(mcuArray); 
		       return mcuArray;	    	   
	       } else  if (yuvImg.samplingRatio ==  Sampler.YUV_420 && w % 16 == 0 && h % 16 == 0) {
	    	   list = sample422(yuvImg);
		       MCU[] mcuArray = new MCU[list.size()]; 
		       mcuArray = list.toArray(mcuArray); 
		       return mcuArray;	
	       } else {	    	   
	    	   throw new IllegalArgumentException("Invalid Sampling Ratio Or Invalid Dimension");
	       }      
	   } 
	 public YuvImage unGrid422 (MCU[] mcu) {
		 YuvImage yuv = null;
		 Component Y;
		 Component Cr;
		 Component Cb;
		 int N = Block.SIZE;
		 
		 int[][] inverseYuvY = new int[h][w];
		 int[][] inverseYuvCb = new int[h][w];
		 int[][] inverseYuvCr = new int[h][w];
		 int row = 0;
		 int col = 0;
		// System.out.println("W  " + w + "H : " + h);
		 for (int i = 0; i < mcu.length; i++) {
			 	
			 	// 左上角定点； 
			 	
			 	row = (i*16/w)*16;
			 	col = (i*16)%w;
		//	 	System.out.println("row " + row + "col : " + col);
			
			 	unMCU_Y(mcu[i], inverseYuvY, row, col);
			 	
			 	
			 	unMCU_Cb(mcu[i], inverseYuvCb, row, col);
			 	unMCU_Cr(mcu[i], inverseYuvCr, row, col);
//			 	System.out.println("_______________Inverse   y _____________________");
//			 	 print(inverseYuvY);
//			 	
//			 	 System.out.println("_______________Inverse   Cb _____________________");
//			 	print(inverseYuvCb);
//			 	 
//			 	System.out.println("_______________Inverse   Cr _____________________");
//			 	print(inverseYuvCr);
			// 	int[][] Cr_back = unMCU_Cr(mcu[i]);
			// 	int[][] Cb_back = unMCU_Cb(mcu[i]);
			 	
		 }
		 Y = new Component(inverseYuvY,0);
		 Cb = new Component(inverseYuvCb, 1);
		 Cr = new Component(inverseYuvCr, 2);
		 return new YuvImage(Y, Cb, Cr, SamplingRatio);
		 
	 }
	 public void unMCU_Cb(MCU m, int[][] Cb_back, int row, int col) {
		 Block[] Cb = m.getCbBlockArray();
		 int N = 8;
		 
		 int[][] b1 = Cb[0].getData();
		 for (int i = 0; i < N; i++) {
			 for (int j = 0; j <N; j++) {
				 Cb_back[row + i][col +j] = b1[i][j];
				 Cb_back[row + i][col + j+8] = b1[i][j];
			 }
		 }
		 int[][] b2 = Cb[1].getData();
		 for (int i = 0; i < N; i++) {
			 for (int j = 0; j <N; j++) {
				 Cb_back[row + i + 8][col+j] = b2[i][j];
				 Cb_back[row + i + 8][col+j+8] = b2[i][j];
			 }
		 }
	 }
	 public void unMCU_Cr(MCU m, int[][] Cr_back, int row, int col) {
		 Block[] Cr = m.getCrBlockArray();
		 int N = 8;
		 
		 int[][] b1 = Cr[0].getData();
		 for (int i = 0; i < N; i++) {
			 for (int j = 0; j <N; j++) {
				 Cr_back[row + i][col +j] = b1[i][j];
				 Cr_back[row + i][col + j+8] = b1[i][j];
			 }
		 }
		 int[][] b2 = Cr[1].getData();
		 for (int i = 0; i < N; i++) {
			 for (int j = 0; j <N; j++) {
				 Cr_back[row + i + 8][col+j] = b2[i][j];
				 Cr_back[row + i + 8][col+j+8] = b2[i][j];
			 }
		 }
	 }
	 public void unMCU_Y(MCU m, int[][] Y_back, int row, int col ) {
		 Block[] YBlock = m.getYBlockArray();
		 int N = 8;
		
		 
		 for (int i = 0; i < 2; i++) {
			int r = row + i*8;
			 for (int j = 0; j < 2;  j++) {			 	 
					int index = i*2 + j; 
					int  c = col + j*8;
					 int [][] data = YBlock[index].getData(); // 8 * 8
					 for (int u = 0; u < N; u++) {
						 for (int v = 0; v < N ; v++) {
						   int x =  r + u;
						   int y =  c + v;
						 //  System.out.println("r " + x + "c : " + y);
						   Y_back[x][y] = data[u][v];  // i = 0 + v j = 0 + u
						 }
					 }	 
				 }
			}
		
		 
	 } 
	 public int[][] unMCU_Cb(MCU m) {
		 int[][] Cb_back = new int[16][16];
		 Block[] Cb = m.getYBlockArray();
		 int[][] data = Cb[0].getData();
		 for (int u = 0; u < 8; u++) {
			 for (int v = 0; v < 8; v++) {
				 Cb_back[u][v] =  data [u][v];
				 Cb_back[u][v+8] =  data [u][v];
			 }
		 }
		 data = Cb[1].getData();
		 for (int u = 8; u < 16; u++) {
			 for (int v = 0; v < 8; v++) {
				 Cb_back[u][v] =  data [u-8][v];
				 Cb_back[u][v+8] =  data [u-8][v];
			 }
		 }
		 System.out.println(" -------------cb--------------------------");
		 print(Cb_back);
		 return Cb_back;
	 } 
	 

	   private List<MCU> sample422(YuvImage yuvImg) {
		   Component Y = yuvImg.Y;
    	   Component Cb = yuvImg.getComponent(YuvImage.Cb_COMP); 
	       Component Cr = yuvImg.getComponent(YuvImage.Cr_COMP); 
	       int[][] YData = Y.getData(); 
	       int[][] CbData = Cb.getData(); 
	       int[][] CrData = Cr.getData(); 	
	       List<MCU> list = new ArrayList<>();
	       for (int i = 0; i < Y.getSize().height; i += Block.SIZE*2) { 
	           for (int j = 0;j < Y.getSize().width;  j += Block.SIZE*2) { 		             
	        	   Block[] YBlocks = new Block[4]; 
	               Block[] CbBlocks = new Block[2]; 
	               Block[] CrBlocks = new Block[2]; 
	               int index = 0; 
	        	   for (int x = 0; x < 2;  x++) {       // 2 blocks vertical for each component 
	                   for (int y = 0; y < 2;  y++) {
	                	   int M = i + x*Block.SIZE;
	                	   int N = j+  y*Block.SIZE;
	           //     	   System.out.println("INDEX OF TOP LEFT OF Y block : " + M + "   "+ N);
	                	   // 2 blocks horizontal for Y, 1 block for Cb and Cr 
	                	   int[][] blockData =  getBlockData(YData, M, N);	   
	                	   YBlocks[index] = new Block(blockData, YuvImage.Y_COMP); 
	                       if (y == 0) {  
		                	    M = i+ x*Block.SIZE;
		                	    N = j/2;
		           //     	   System.out.println("INDEX OF TOP LEFT OF Cr Cb block : " + M + "   "+ N);
	                    	   blockData =  getBlockData(CbData, M, N);                    	   
	                    	   CbBlocks[index/2] = new Block(blockData, YuvImage.Cb_COMP);           
	                    	   blockData =  getBlockData(CrData, M, N);
	                    	   CrBlocks[index/2] = new Block(blockData, YuvImage.Cr_COMP); 
	                       } 
	                       index++; 
	                       
	                   } 
	               } 
	        	   MCU mcu = new MCU(YBlocks, CbBlocks, CrBlocks);
	               list.add(mcu); 
	              }
	         }
	        	  
	        return list;
	}
	   private List<MCU> sample444(YuvImage yuvImg) {
		   Component Y = yuvImg.Y;
		   int h = Y.getSize().height;
	       int w = Y.getSize().width;  
    	   Component Cb = yuvImg.getComponent(YuvImage.Cb_COMP); 
	       Component Cr = yuvImg.getComponent(YuvImage.Cr_COMP); 
	       int[][] YData = Y.getData(); 
	       int[][] CbData = Cb.getData(); 
	       int[][] CrData = Cr.getData();  
	       List<MCU> list = new ArrayList<>();
	       for (int i = 0; i < h; i += Block.SIZE) { 
	           for (int j = 0;j < w;  j += Block.SIZE) { 
	               Block[] YBlocks = new Block[1]; 
	               Block[] CbBlocks = new Block[1]; 
	               Block[] CrBlocks = new Block[1]; 	               
	               int[][] blockData =  getBlockData(YData,i,j);
	               Block block = new Block(blockData, YuvImage.Y_COMP);
	               YBlocks[0] = block; 	               
	               blockData = getBlockData(CbData,i,j);
	               block = new Block(blockData, YuvImage.Cb_COMP);
	               CbBlocks[0] = block;                
	               blockData = getBlockData(CrData,i,j);
	               block = new Block(blockData, YuvImage.Cr_COMP);
	               CrBlocks[0] = block;            
	               MCU mcu = new MCU (YBlocks, CbBlocks, CrBlocks);
	               list.add(mcu); 
	    //           System.out.println("---------------add one MCU to MCT ARRAY ------------------------");
	           } 
	       } 
	       return list;
		}
	
	   private List<MCU> sample420(YuvImage yuvImg) {
		   Component Y = yuvImg.Y;
		   Component Cb = yuvImg.getComponent(YuvImage.Cb_COMP); 
	       Component Cr = yuvImg.getComponent(YuvImage.Cr_COMP); 
	       int[][] YData = Y.getData(); 
	       int[][] CbData = Cb.getData(); 
	       int[][] CrData = Cr.getData(); 		       
	   //    print(YData);
	   //    print(CbData);
	   //    print(CrData);
	       List<MCU> list = new ArrayList<>();
	       
	       for (int i = 0; i < Y.getSize().height; i += Block.SIZE*2) { 
	           for (int j = 0;j < Y.getSize().width;  j += Block.SIZE*2) { 		             
	        	   Block[] YBlocks = new Block[4]; 
	               Block[] CbBlocks = new Block[1]; 
	               Block[] CrBlocks = new Block[1]; 
	               int index = 0; 
	        	   for (int x = 0; x < 2;  x++) {       // 2 blocks vertical for each component 
	                   for (int y = 0; y < 2;  y++) {
	                	   // 2 blocks horizontal for Y, 1 block for Cb and Cr 
	                	   int M = i + x*Block.SIZE;
	                	   int N = j+  y*Block.SIZE;
	                	   int[][] blockData =  getBlockData(YData,M, N);	   
	                	//   System.out.println("INDEX OF TOP LEFT OF y block : " + M + "   "+ N);
	                	   YBlocks[index] = new Block(blockData, YuvImage.Y_COMP); 
	                       if (y == 0 && x == 0) {  
	                    	   // get also Cb and Cr blocks 
	                    	   M = i/2;
	                    	   N = j/2;
	                    	  // System.out.println("INDEX OF TOP LEFT of Cr cb: " +  M + "   "+ N);
	                    	   blockData =  getBlockData(CbData, M, N);                    	   
	                    	   CbBlocks[index] = new Block(blockData, YuvImage.Cb_COMP);           
	                    	   blockData =  getBlockData(CrData, M, N);
	                    	   CrBlocks[index] = new Block(blockData, YuvImage.Cr_COMP); 
	                       } 
	                       index++; 
	                   } 
	               }        	   
	               MCU mcu = new MCU(YBlocks, CbBlocks, CrBlocks);
	               list.add(mcu); 
	           } 
	       } 
	       return list;
		}
	
	private int[][] getBlockData(int[][] compData, int X, int Y) { 
	       int[][] b = new int[Block.SIZE][Block.SIZE]; 
	       for (int i = 0;i < Block.SIZE; i++) { 
	           for (int j = 0; j < Block.SIZE; j++) { 
	               b[i][j] = compData[X+i][Y+j]; 
	           } 
	       } 
	       return b; 
	   } 
	   
	   public static void checkMcu(MCU[] mcu) {
		   for (MCU m: mcu) {
		    	  System.out.println("Y block array");
		    	  Block[] y =  m.getYBlockArray();
		    	  int i = 0;
		    	  for (Block b: y) {
		    	 		System.out.println("block  : " + i++);
		    	 		int[][] block = b.getData();
		    	 		print(block);
		    	  }
		    	  Block[] cb = m.getCbBlockArray();
		    	  System.out.println("Cb block array");
		    	  
		    	  i = 0;
		    	  for (Block b: cb) {
		    	 		System.out.println("block  : " + i++);
		    	 		int[][] block = b.getData();
		    	 		print(block);
		    	  }
		    	  
		    	 Block[] cr = m.getCrBlockArray();
		    	  System.out.println("Cr block array");
		    	  
		    	  i = 0;
		    	  for (Block b: cr) {
		    	 		System.out.println("block  : " + i++);
		    	 		int[][] block = b.getData();
		    	 		print(block);
		    	  }
		    	  
		    	  
		      }
	   }
	   public static void print(int[][] b) {
		   for(int[] i : b) {
			   for (int j : i) {
				   System.out.print(j + " ");
			   }
			   System.out.println();
		   }
	   }
	   
	   public static void main(String[] args) {
			
			File f = null;
		    //read image
		    try{
		      f = new File("/Users/apple/Desktop/hihi.png"); //image file path
		      Image image = ImageIO.read(f);
		      BufferedImage buffered = (BufferedImage) image;
		    //  System.out.println("Reading complete.");
		      System.out.println("old IMAGE H" + image.getHeight(null));
		      System.out.println("old IMAGE W" + image.getWidth(null));
		      SizeTrimer st = new SizeTrimer();
		      
		      int SamplingRatio = 1;
		      
		      image = st.resizeImage(image, SamplingRatio);
		      System.out.println("new IMAGE H" + image.getHeight(null));
		      System.out.println("new IMAGE W" + image.getWidth(null));
		      YuvImage yuv = YuvImage.rgbToYuv(image , SamplingRatio);
		      Sampler sp = new Sampler();
		      yuv = sp.sampling(yuv, SamplingRatio);	
		      ImageGrid ig = new ImageGrid();
		      MCU [] mcu =  ig.imageGridder(yuv);
		     
		      System.out.println(" ----------------------    check MCU ARRAY - --------------------------");
		  //    checkMcu(mcu);
		      System.out.println("mcu length" + mcu.length);
		    }
		    catch(IOException e){
		      System.out.println("Error: "+e);
		    }
		}
}

package jpeg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DCT {
	double[][] dctData; 
	
	
	public static DCT FDCT (Block b) {
		 int[][] data = b.getData(); 
		 double DIV = 1d / Math.sqrt(2); 
		 double[][] dctBlock = new double[Block.SIZE][Block.SIZE]; 
		 for (int u = 0; u < Block.SIZE; u++) { 
			 for (int v = 0; v < Block.SIZE; v++) { 
				 double ud = u == 0 ? DIV : 1;
				 double vd = v == 0 ? DIV : 1;
				 double pre = ud * vd / 4; 
				 double sum = 0; 
				 for (int i = 0; i < Block.SIZE; i++) { 
					 for (int j = 0; j < Block.SIZE; j++) { 
						 sum += (data[i][j] - 128) * Math.cos((2 * i + 1) * u * Math.PI / 16) * Math.cos((2 * j + 1) * v * Math.PI / 16); 
					 } 
				 } 
				 dctBlock[u][v] = pre * sum; 
		  } 
		 } 

		 return new DCT(dctBlock); 
	}
	
    public DCT(double[][] dctData) { 
        if (dctData != null) { 
            
        	if (dctData.length == Block.SIZE && dctData[0].length ==  Block.SIZE) 
                this.dctData = dctData; 
            else 
                throw new IllegalArgumentException("Block should be 8 * 8"); 
        } 
        else 
            throw new IllegalArgumentException("The DCT block data must not be null."); 
    } 
 
    public double[][] getData() { 
        return dctData; 
    } 
    
    
    public static void print(int[][] b) {
		   for(int[] i : b) {
			   for (int j : i) {
				   System.out.print(j + " ");
			   }
			   System.out.println();
		   }
	   }
    public static void print(double[][] b) {
		   for(double[] i : b) {
			   for (double j : i) {
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
		      
		      int SamplingRatio = 0;
		      
		      image = st.resizeImage(image, SamplingRatio);
		      System.out.println("new IMAGE H" + image.getHeight(null));
		      System.out.println("new IMAGE W" + image.getWidth(null));
		      YuvImage yuv = YuvImage.rgbToYuv(image);
		      Sampler sp = new Sampler();
		      yuv = sp.sampling(yuv, SamplingRatio);	
		      ImageGrid imageGrid = new ImageGrid();
		      MCU [] mcu =  imageGrid.imageGridder(yuv);  
		      System.out.println(" ----------------------    check MCU ARRAY - --------------------------");
		  //    checkMcu(mcu);
		      System.out.println("mcu length" + mcu.length);
		      
		      int i = 0;
		      for (MCU m : mcu) {
		    	 System.out.println("MCU #_____________________________" + i++);
		    	 Block[] Y  = m.getYBlockArray();
		    	 Block[] Cr = m.getCrBlockArray();
		    	 Block[] Cb = m.getCbBlockArray();
		    	 System.out.println("yyyy dct  ____________________________________________" );
		    	 for (Block b: Y) {
		    		 DCT dctBlock = DCT.FDCT(b);
		    		 print(dctBlock.getData());
		    	 }
		      	 System.out.println("cr dct  ____________________________________________" );
		    	 for (Block b: Cr) {
		    		 DCT dctBlock = DCT.FDCT(b);
		    		 print(dctBlock.getData());
		    	 }
		      	 System.out.println("cb dct  ____________________________________________" );
		    	 for (Block b: Cb) {
		    		 DCT dctBlock = DCT.FDCT(b);
		    		 print(dctBlock.getData());
		    	 }
		      }
		    }
		    catch(IOException e){
		      System.out.println("Error: "+e);
		    }
		}
}

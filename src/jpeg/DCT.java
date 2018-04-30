package jpeg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jpeg.YuvImage.Component;

public class DCT {
	double[][] dctData; 
	int Component;
	public static DCT FDCT(Block b){   //偶余弦变换EDCT 
    	int[][] input = b.getData();
    	int N = 8;
    	double[][] output = new double[N][N];
        short i,j,p,q;
        double coefficient1,coefficient2;
        double PI = Math.PI;
        for(p=0;p<N;p++)
        {
            for(q=0;q<N;q++)
            {
                if(p==0) coefficient1=Math.sqrt(1.0/N);
                else coefficient1=Math.sqrt(2.0/N);
                if(q==0) coefficient2=Math.sqrt(1.0/N);
                else coefficient2=Math.sqrt(2.0/N);
                double tmp=0.0;
                for(i=0;i<N;i++)
                    for(j=0;j<N;j++)
                        tmp+=input[i][j]*Math.cos((2*i+1)*PI*p/(2*N))*Math.cos((2*j+1)*PI*q/(2*N));//形成新的矩阵 
                output[p][q]=Math.round(coefficient1*coefficient2*tmp);
            }
        }
       // print(output);
        return new DCT(output, b.type);
    }
    
	
	
	
	  public static Block IDCT(DCT d) {
		  double[][] input = d.getData();
	  		int N = 8;
	  		int[][] output = new int[8][8];
		    short i,j,p,q;
		    double coefficient1,coefficient2;
		    double PI = Math.PI;
		    for(i=0;i<N;i++)
		    {
		        for(j=0;j<N;j++)
		        {
		            double tmp=0.0;
		            for(p=0;p<N;p++)
		                for(q=0;q<N;q++)
		                {
		                    if(p==0) coefficient1=Math.sqrt(1.0/N);
		                    else coefficient1=Math.sqrt(2.0/N);
		                    if(q==0) coefficient2=Math.sqrt(1.0/N);
		                    else coefficient2=Math.sqrt(2.0/N);
		                    tmp+= coefficient1 *coefficient2*input[p][q]*Math.cos((2*i+1)*PI*p/(2*N))*Math.cos((2*j+1)*PI*q/(2*N));
		                }
		                output[i][j]=(int)(Math.round(tmp));
		        } 
		    }
	//	   print(output);
		   return new Block(output, d.Component);
		}    
	 
	
	
    public DCT(double[][] dctData, int type) { 
        if (dctData != null) { 
        	this.Component = type;
            
        	if (dctData.length == Block.SIZE && dctData[0].length ==  Block.SIZE) 
                this.dctData = dctData; 
            else 
                throw new IllegalArgumentException("Block should be 8 * 8"); 
        } 
        else 
            throw new IllegalArgumentException("The DCT block data must not be null."); 
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
		      
		      YuvImage yuv = YuvImage.rgbToYuv(image, SamplingRatio);
		      Sampler sp = new Sampler();
		      yuv = sp.sampling(yuv, SamplingRatio);	
		      ImageGrid imageGrid = new ImageGrid();
		      MCU [] mcu =  imageGrid.imageGridder(yuv);  
		      System.out.println(" ----------------------    check MCU ARRAY - --------------------------");
		  //    checkMcu(mcu);		   
		      for (MCU m : mcu) {		    
		    	 Block[] Y  = m.getYBlockArray();
		    	 Block[] Cr = m.getCrBlockArray();
		    	 Block[] Cb = m.getCbBlockArray();
		    	
		    	 for (Block b: Y) {
		    		 print(b.getData());
		    		 DCT d = DCT.FDCT(b);
		    		 Block newb =  DCT.IDCT(d);
		    		 
		    		 
		    	 }		      	
		    	 for (Block b: Cr) {
		    		 print(b.getData());
		    	
		    		 DCT d = DCT.FDCT(b);
		    		 Block newb =  DCT.IDCT(d);
		    	 }
		    	 for (Block b: Cb) {
		    		 print(b.getData());	
		    		
		    		 DCT d = DCT.FDCT(b);
		    		 Block newb =  DCT.IDCT(d);
		    		 
		    	 }
		    }
		      
		    }
		    catch(IOException e){
		      System.out.println("Error: "+e);
		    }
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
		      
	    
}

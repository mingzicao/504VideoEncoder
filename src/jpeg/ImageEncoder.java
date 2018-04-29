package jpeg;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.IOException;

import jpeg.RunLengthEncoder.Cell;

/*	
 * 	input: Image Object
 *  Image - [sizeTrimer] - rgbImage
 *  rgbImage - [ YuvConverter] - YUVimage
 *  YuvImage + SampleRatio - [Sampler] - YuvImage'
 *  YUVImage'. three Y Cr Cb - [ MCUConverter] - MCU
 *  MCU - [ BlockConverter] - 3 block Array;
 *  each Block - [DCT Transfer] - [Quantizer] -  dctBlock;
 *  dctBlock - [ AC DC encoder] - [VLIencoder + Huffman encoder] -jpg.file;
 *  output: an jpgFile; put in the outputStream;
 
 * */
public class ImageEncoder {
		
		Image rgbImage; 
	    YuvImage yuvImage;
	    Sampler sampler;
	    Quantizer quantizer;
	    SizeTrimer trimmer; 
	    MCU[] allMCU;
	    HuffmanEncoder hf;
	    //  EntropyCoderI entropyCoder; 	    
	    int samplingRatio; 
	    int qualityFactor;
	    
	    private static final int DefaultSampleRatio = Sampler.YUV_444; 
	    private static final int DefaultQualityFactor = 100;
	  
	    
	    
	    public ImageEncoder(Image rgbImg) { 
	        this(rgbImg, DefaultSampleRatio, DefaultQualityFactor); 
	    } 
	    public ImageEncoder(Image rgb, int sr, int qf){
	    	if ( Sampler.checkValidSamplingRatio(sr) ) {
		    	this.rgbImage = rgb;
		    	this.samplingRatio = sr;
		    	this.qualityFactor = qf;
		    	trimmer =  new SizeTrimer();
		    	sampler = new Sampler();
		    	quantizer = new Quantizer(qf); 
		    	//hf = new HuffmanEncoder();
		         
	    	} else {
	    		 throw new IllegalArgumentException("Invalid subsampling Ratio, please input 0/ 1/ 2"); 
	    	}
	    }
	    
	    
	    
	    
	    protected void imageCompress(BufferedOutputStream bos) throws IOException { 
	        HuffmanEncoder hf = new HuffmanEncoder(bos); 
	    	Image resizedImage = trimmer.resizeImage(rgbImage, samplingRatio);
	        // showImage(scaledImg); 
	        YuvImage yuv = YuvImage.rgbToYuv(resizedImage); 	        
	        yuv = sampler.sampling(yuv, samplingRatio);
	        ImageGrid imageGrid = new ImageGrid();
	      //  System.out.println(" ----------------------    check MCU ARRAY - --------------------------");	 
	        MCU [] mcu =  imageGrid.imageGridder(yuv);  
		   
		   // System.out.println(" ----------------------    check MCU ARRAY - --------------------------");	  
	        System.out.println(" ----------------------   hhhhhhh- --------------------------");	 
		    System.out.println("mcu length" + mcu.length);
		   
		    int QualityFactor = 80;
		    Quantizer q = new Quantizer(QualityFactor);  
		      Block prev = null;
		      for (MCU m : mcu) {		    	  	 
			    	 Block[] Y  = m.getYBlockArray();
			    	 Block[] Cr = m.getCrBlockArray();
			    	 Block[] Cb = m.getCbBlockArray();
			    	 
			    	 for (int i = 0; i < Y.length; i++) {
			    		 DCT dctYBlock = DCT.FDCT(Y[i]);
			    		 Block cur = q.quantizeDCTBlock(dctYBlock, YuvImage.Y_COMP);
			    		 hf.setCurBlock(cur);
			    		 hf.huffmanEncoding(YuvImage.Y_COMP);
			    		 prev = cur;
			    		 hf.setPrevBlock(prev, YuvImage.Y_COMP);
			    	 }
			    	 
			    	 for (int i = 0; i < Cb.length; i++) {
			    		 DCT dctCbBlock = DCT.FDCT(Cb[i]);
			    		 Block cur = q.quantizeDCTBlock(dctCbBlock, YuvImage.Cb_COMP);
			    		 hf.setCurBlock(cur);
			    		 hf.huffmanEncoding(YuvImage.Cb_COMP);
			    		 prev = cur;
			    		 hf.setPrevBlock(prev, YuvImage.Cb_COMP);
			    	 }
			    			    	 
			    	 for (int i = 0; i < Cr.length; i++) {
			    		 DCT dctCrBlock = DCT.FDCT(Cr[i]);
			    		 Block cur = q.quantizeDCTBlock(dctCrBlock, YuvImage.Cr_COMP);
			    		 hf.setCurBlock(cur);
			    		 hf.huffmanEncoding(YuvImage.Cr_COMP);
			    		 prev = cur;
			    		 hf.setPrevBlock(prev,YuvImage.Cr_COMP);
			    	 }		    	
			    	     	 			    	 
		      }
	        
	    } 
	   
	    
	    
	    public void compressToOutStream(BufferedOutputStream bos) throws IOException { 
	      //  bos.write(SOI); 
	      //  writeHeaderSegments(bos); 
	    	imageCompress(bos); 
	      //  bos.write(EOI); 
	      //  bos.flush(); 
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




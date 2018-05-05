package jpeg;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.IOException;

import jpeg.RunLengthEncoder.Cell;

/*	
 *  In Image Encoder: 开始压缩就是调用image compress 函数：
 *   When init a new Image Encoder, also initialize a new HuffEncoder instance;
 * 	a HuffEncoder contains RLE encoder and a DCEncoder;
 * 	the table uses for HuffmanCoding is created with HUffEncoder initialization;
 *  流程 你可以根据HuffmanEncoder  里面的main 函数的调用顺序 来梳理 
 *  
 * 	input: Image Object > ImageEncoder > 调用imageCompress() 函数
 * 
 *  Image rgbImage - [SizeTrimer] - resized rgbImage
 *  rgbImage - [ YuvConverter] - YUVimage
 *  YuvImage + SampleRatio - [Sampler] - YuvImage'
 *  
 *  YUVImage'.  Y Cr Cb - [ imageGrid ] - MCU[] array ( each MCU  contains 3  block[] : Y[] , Cr[ ], Cb[]) 
 *  
 *  
 *  each Block  - [DCT.forward] - [Quantizer.quatize] -  Quantized dctBlock;

 * 
 * 	QuantizedBlock QB  -  [Block. ZigZag]  - 1D - block
 * 
 * 					-	 		[  DC Encoder  	] - 
 * 	1D  block	HUffEncoder.					 		>  HuffmanEncoding   /   VLI Encoding  > jpeg write to OUTStream;
 * 	 				- 	 		[  AC RLE Encoder  ] -     (查表)
 * 
 *  现在想跑的话，在HuffmanEncoder 下有一个main 函数， 跑完之后结果都算出来了 我把 bufferIt 注释掉了 BufferIt这一步 就是往BOS 写东西
 *  再输出， jpeg的 header 文件我还没往里面加， 要是需要jpeg 的话看 mit 小哥的 Encoder.java 类里面 有写jpeg 头文件的部分  
 
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
	        YuvImage yuv = YuvImage.rgbToYuv(resizedImage,1); 	        
	        yuv = sampler.sampling(yuv, samplingRatio);
	        ImageGrid imageGrid = new ImageGrid(resizedImage.getHeight(null),resizedImage.getWidth(null));
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




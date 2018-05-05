package jpeg;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jpeg.RunLengthEncoder.Cell;


/* When init a new Image Encoder, initialize a new HuffEncoder instance;
 * a HuffEncoder contains RLE encoder and a DC encoder;
 * the table uses for HuffmanCoding is created with HUffEncoder initialization; 
 * 
 * 
 * QuantizedBlock QB  -  [ZigZag]  - 1D Q block
 * 
 * Block 	-	 [  DC Encoder  	] - 
 * 										  - >  HuffmanEncoding   /   VLI Encoding  > jpeg write to OUTStream;
 * Block 	- 	 [  AC RLE Encoder  ] -  
 * 
 * */

public class HuffmanEncoder {			
		RunLengthEncoder rle;
		DCEncoder de;				
		Block cur;
		Block[] prev; // need to remember the prev block of 
		
		BufferedOutputStream bos;	
		
		int[][] DC_Y_TABLE;
		int[][] DC_CbCr_TABLE;
		int[][] AC_Y_TABLE;
		int[][] AC_CbCr_TABLE;
		int codeBuffer = 0;
		int bitBuffer = 0;
		
		public HuffmanEncoder(BufferedOutputStream bos) {
			this.bos = bos;
			rle = new RunLengthEncoder();
			de = new DCEncoder();
			prev = new Block[3];
			
			DC_Y_TABLE = initializeTable(true, BITS_DC_LUMINANCE, VALS_DC_LUMINANCE);
			DC_CbCr_TABLE =initializeTable(true,BITS_DC_CHROMINANCE,VALS_DC_CHROMINANCE);
			AC_Y_TABLE = initializeTable(false,BITS_AC_LUMINANCE,VALS_AC_LUMINANCE);;
			AC_CbCr_TABLE = initializeTable(false,BITS_AC_CHROMINANCE,VALS_AC_CHROMINANCE); ;
		}
		public void setPrevBlock (Block b, int component) {
			this.prev[component] = b ;
		}
		public void setCurBlock (Block b) {
			this.cur = b;
		}
		public Block getCurBlock() {
			return cur;
		}
		public Block getPrevBlock(int component) {
			return prev[component];
		}		
		/*bits array len = 16, which represents # of huff for Y , Cb, Cr*/
		static final int[] BITS_DC_LUMINANCE = {0x00, 0, 1, 5, 1, 1,1,1,1,1,0,0,0,0,0,0,0}; 
	    /** The number of Huffman codes for chrominance DC coefficients. **/ 
	    
		static final int[] BITS_DC_CHROMINANCE = {0x01,0,3,1,1,1,1,1,1,1,1,1,0,0,0,0,0}; 
	    /** The number of Huffman codes for luminance RUN-LEVEL codes. **/ 
	    
		static final int[] BITS_AC_LUMINANCE = {0x10,0,2,1,3,3,2,4,3,5,5,4,4,0,0,1,0x7d}; 
	    /** The number of Huffman codes for chrominance RUN-LEVEL codes. **/ 
	 
		static final int[] BITS_AC_CHROMINANCE = {0x11,0,2,1,2,4,4,3,4,7,5,4,4,0,1,2,0x77}; 	 
	 
	    /**
	     * Used for Value length encoding.
	
	     **/ 
	    static final int[] VALS_DC_LUMINANCE = {0,1,2,3,4,5,6,7,8,9,10,11}; 
	    /**
	     * Index values for calculating the position of a chrominance DC-code 
	     * within the Huffman table (or VLC table) for DC chrominance. 
	     **/ 
	    static final int[] VALS_DC_CHROMINANCE = {0,1,2,3,4,5,6,7,8,9,10,11}; 
	    /**
	     * Index values for calculating the position of a Huffman code for RUN-LEVELS 
	     * of luminance AC coefficients. 
	     **/ 
	    static final int[] VALS_AC_LUMINANCE = { 
	          0x01, 0x02, 0x03, 0x00, 0x04, 0x11, 0x05, 0x12, 
	          0x21, 0x31, 0x41, 0x06, 0x13, 0x51, 0x61, 0x07, 
	          0x22, 0x71, 0x14, 0x32, 0x81, 0x91, 0xa1, 0x08, 
	          0x23, 0x42, 0xb1, 0xc1, 0x15, 0x52, 0xd1, 0xf0, 
	          0x24, 0x33, 0x62, 0x72, 0x82, 0x09, 0x0a, 0x16, 
	          0x17, 0x18, 0x19, 0x1a, 0x25, 0x26, 0x27, 0x28, 
	          0x29, 0x2a, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 
	          0x3a, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 
	          0x4a, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 
	          0x5a, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 
	          0x6a, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 
	          0x7a, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 
	          0x8a, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 
	          0x99, 0x9a, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 
	          0xa8, 0xa9, 0xaa, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 
	          0xb7, 0xb8, 0xb9, 0xba, 0xc2, 0xc3, 0xc4, 0xc5, 
	          0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xd2, 0xd3, 0xd4, 
	          0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xe1, 0xe2, 
	          0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 
	          0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 
	          0xf9, 0xfa}; 
	 
	    /**
	     * Index values for calculating the position of a Huffman code for RUN-LEVELS 
	     * of chrominance AC coefficients. 
	     **/ 
	    static final int[] VALS_AC_CHROMINANCE = { 
	          0x00, 0x01, 0x02, 0x03, 0x11, 0x04, 0x05, 0x21, 
	          0x31, 0x06, 0x12, 0x41, 0x51, 0x07, 0x61, 0x71, 
	          0x13, 0x22, 0x32, 0x81, 0x08, 0x14, 0x42, 0x91, 
	          0xa1, 0xb1, 0xc1, 0x09, 0x23, 0x33, 0x52, 0xf0, 
	          0x15, 0x62, 0x72, 0xd1, 0x0a, 0x16, 0x24, 0x34, 
	          0xe1, 0x25, 0xf1, 0x17, 0x18, 0x19, 0x1a, 0x26, 
	          0x27, 0x28, 0x29, 0x2a, 0x35, 0x36, 0x37, 0x38, 
	          0x39, 0x3a, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 
	          0x49, 0x4a, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 
	          0x59, 0x5a, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 
	          0x69, 0x6a, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 
	          0x79, 0x7a, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 
	          0x88, 0x89, 0x8a, 0x92, 0x93, 0x94, 0x95, 0x96, 
	          0x97, 0x98, 0x99, 0x9a, 0xa2, 0xa3, 0xa4, 0xa5, 
	          0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xb2, 0xb3, 0xb4, 
	          0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xc2, 0xc3, 
	          0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xd2, 
	          0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 
	          0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 
	          0xea, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 
	          0xf9, 0xfa}; 
	 
	   
	    protected int[][] initializeTable(boolean dc, int[] bits, int[] vals) { 
	        int[][] huffTable; 
	        int[] huffsize = new int[257];  
	        int[] huffcode= new int[257]; 
	        int p = 0, lastp, code, si; 
	        for (int i = 1; i < bits.length; i++) { 
	            for (int j = 1; j <= bits[i]; j++) { 
	                huffsize[p++] = i; 
	            } 
	        } 
	        huffsize[p] = 0; 
	        lastp = p;  
	        code = 0; 
	        si = huffsize[0]; 
	        p = 0; 
	        while(huffsize[p] != 0) { 
	            while(huffsize[p] == si) { 
	                huffcode[p++] = code; 
	                code++; 
	            } 
	            code <<= 1; 
	            si++; 
	        }  
	        if (dc) 
	            // create Huffman table for DC coefficients 
	            huffTable = new int[12][2]; 
	        else 
	            // create Huffman table for run-levels of AC coefficients 
	            huffTable = new int[255][2]; 
	 
	        for (p = 0; p < lastp; p++) { 
	            huffTable[vals[p]][0] = huffcode[p]; 
	            huffTable[vals[p]][1] = huffsize[p]; 
	        } 
	 
	        return huffTable; 
	 } 
	   
	
	public void huffmanEncoding(int component) {
		int[] linearBlock = cur.getZigZag(cur);	
		de.encodeDC(component, this);
		rle.encodeAC(linearBlock, component, this);	
		/** linear block -  RLE encoding  - Cell(run, length) [], 
		    encode this cell array in huffman Encoder; 
			rle.encoding(linearBlock, component, bos);
		*/	
		
	}
	 /*
	public void encodeAC( int[] array, int component, HuffmanEncoder hf) {
		Cell[] cells = RLEncoding(array);
		 int[][] huffmanTable = hf.getHuffmanTable(component, false); // false for AC,  True for DC;
         for (int i=0;i < cells.length;i++) { 
             int Zero = cells[i].getZero(); 
             int Value = cells[i].getValue(); 
             if (i < cells.length-1 || Value != 0) { 
                 System.out.println(" Coding run-level: (" + Zero + " , " + Value + ")"); 
                 while (Zero > 15) {
                 	 System.out.println("(15  , 0 ) " + huffmanTable[0xF0][0] + "," + huffmanTable[0xF0][1] + "  ");
         //            bufferIt(huffmanTable[0xF0][0], huffmanTable[0xF0][1], bos); // 超过15 个0 ， 直接buffer 固定的数；（15, 0）
                     Zero -= 16; 
                 } 
                 int complement = Value; 
                 if (Value < 0) { 
                     Value = -Value; 
                     complement--; 
                 } 
                 int bitLength = 0; 
                 while (Value != 0) { 
                 	bitLength++; 
                     Value >>= 1; 
                 } 
                 int tableIndex = (Zero << 4) + bitLength;  // 合并成一个byte  (4'BIT consecutive + 4'BIT valueLength)
                 // 查 huffman 表 编码；
                 
                 System.out.println("After huffman encoding: " + huffmanTable[tableIndex][0] + " , " + huffmanTable[tableIndex][1] + "  "); 
   
                //      bufferIt(huffmanTable[tableIndex][0], huffmanTable[tableIndex][1], bos);
                 
                 System.out.println("bitlen   temp " + bitLength + " , " + complement + "  "); 
     //            bufferIt(complement, bitLength, bos); 
             } 
             else {
                 // end-of-block sentinel value with zero-level 
                System.out.println("end-of-block sentinel value" + huffmanTable[0][0] + " , " + huffmanTable[0][1] + "  "); 
      //           bufferIt(huffmanTable[0][0], huffmanTable[0][1], bos); 
             }
         } 
		
	}*/
		 protected void bufferIt(int code, int size, BufferedOutputStream bos) throws IOException { 
		        System.out.print("\nBuffering Huffman code: " + code + " (size: " + size + ")"); 
		        int codeBuffer = code; 
		        int bitBuffer = this.bitBuffer; 
		        codeBuffer &= (1 << size) - 1; 
		        bitBuffer += size; 
		        codeBuffer <<= 24 - bitBuffer; 
		        codeBuffer |= this.codeBuffer; 
		 
		        while(bitBuffer >= 8) { 
		            int c = ((codeBuffer >> 16) & 0xFF); 
		            bos.write(c); 
		            if (c == 0xFF) 
		                bos.write(0); 
		            codeBuffer <<= 8; 
		            bitBuffer -= 8; 
		        } 
		        this.codeBuffer = codeBuffer; 
		        this.bitBuffer = bitBuffer; 
		    } 
		 
		    /**
		     * Flushes the content of the buffer to the given stream. 
		     * @param bos the output stream to flush the content of the buffer 
		     * @exception java.io.IOException if an error occurred while writing to the stream 
		     */ 
		    public void flushBuffer(BufferedOutputStream bos) throws IOException { 
		        while (bitBuffer >= 8) { 
		            int c = ((codeBuffer >> 16) & 0xFF); 
		            bos.write(c); 
		            if (c == 0xFF) 
		                bos.write(0); 
		            codeBuffer <<= 8; 
		            bitBuffer -= 8; 
		        } 
		        if (bitBuffer > 0) { 
		            int c = ((codeBuffer >> 16) & 0xFF); 
		            bos.write(c); 
		        } 
		    } 
		
	
		 public int[][] getHuffmanTable(int component, boolean isDC) {
				if (component == YuvImage.Y_COMP) {
					if (isDC) {
						return DC_Y_TABLE;
					}else {
						return AC_Y_TABLE;
					}
				} else if (component == YuvImage.Cr_COMP || component == YuvImage.Cb_COMP) {
					if (isDC) {	
						return 	DC_CbCr_TABLE;
					}else {
						return  AC_CbCr_TABLE;	
					}	
				}
				return null;
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
			      
			      YuvImage yuv = YuvImage.rgbToYuv(image,1);
			      Sampler sp = new Sampler();
			      yuv = sp.sampling(yuv, SamplingRatio);	
			      ImageGrid imageGrid = new ImageGrid(image.getHeight(null),image.getHeight(null));
			      MCU [] mcu =  imageGrid.imageGridder(yuv);  
			      System.out.println(" ----------------------    check MCU ARRAY - --------------------------");
			  //    checkMcu(mcu);	
			      Quantizer q = new Quantizer(80);
			      Block prev = null;
			      HuffmanEncoder hf = new HuffmanEncoder(null);
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
			    catch(IOException e){
			      System.out.println("Error: "+e);
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
				
}
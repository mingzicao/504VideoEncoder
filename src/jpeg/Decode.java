package jpeg;

import java.awt.Image;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import jpeg.RunLengthEncoder.Cell;

public class Decode {
	int mostRecentIFrame = 0;
    int offset = 0;
    int count  = 0;
	Quantizer q = new Quantizer(80);
	// ImageGrid ig = new ImageGrid();
	ArrayList<Integer> in = new ArrayList<>();
	ArrayList<Block> AllBlocks = new ArrayList<>();
	int blockNum            =  0;
	int GOP                 = 4;
    public int imageHeight         = 0;
    public  int imageWidth          = 0;
    int frameRate           = 0;
    int numberOfFrames      = 0;
    byte[] bArray;
    Image[] test;
    private ImageGrid ig ;
    
    @SuppressWarnings("resource")
	public Decode(File file) throws IOException{
    	
    		bArray = new byte[(int) file.length()];
        FileInputStream inStream;
        inStream = new FileInputStream(file);
        inStream.read(bArray);
        
		blockNum = ((bArray[0] & 0xff) | ((bArray[1] & 0xff) << 8));
        GOP = bArray[2];
        imageHeight = ((bArray[3] & 0xff) | ((bArray[4] & 0xff) << 8));
        imageWidth = ((bArray[5] & 0xff) | ((bArray[6] & 0xff) << 8));
        frameRate = bArray[7];
        numberOfFrames = ((bArray[8] & 0xff) | ((bArray[9] & 0xff) << 8));
        test = new Image[numberOfFrames];
        ig= new ImageGrid(imageHeight, imageWidth);
        System.out.println("MCU num:"+ blockNum+" GOP: "+GOP+" imageHeight: "+imageHeight+" imageWidth: "+imageWidth+" frameRate: "+frameRate+" Frame number: "+numberOfFrames);
        test();
       // test(MCU[][])
//		System.out.println("MCU num:"+ blockNum+" GOP: "+GOP+" imageHeight: "+imageHeight+" imageWidth: "+imageWidth+" frameRate: "+frameRate+" Frame number: "+numberOfFrames);	
		
    }
    
    public void decoderun(){
	    	for (int i = 10; i < bArray.length; i++) {
	    		in.add(((int) bArray[i]));
	    }
	    int flag = 1;
	    while(count != numberOfFrames) {
	//    	System.out.println(count);
	        // Protocol: 0 - Block, 1 - Reference,
	        // Take the input stream, and use for loop for each Byte.
	        if (flag == 1) {
	        		reConstructI(in);
	            mostRecentIFrame = count;
	            flag = 0;
	            count += 1;
	            
	        }
	        else if (flag == 0) {            
	        		reConstructP(in);
	        		count += 1;
	            if (count % GOP == 0) {
	                flag = 1;
	            }
	            
	        }
	    }
    }
    
    
    public static void main(String[] args) throws IOException {
    		Decode de = new Decode(new File("encoding.txt"));
//    		de.decoderun();
//    		System.out.println(de.AllBlocks.size());
//    		de.deQandDCT();
//    		Image[] result = de.RGBdecoder();
//    		de.writetest(result);
    		de.write();
//    		de.test();
    }
    
    public void reConstructI(ArrayList<Integer> in) {
    		// reconstruct I frame
    		int mcuCount = 0;
    		while(mcuCount < blockNum) {
    			seperateRLE();
    			mcuCount += 1;
//    			System.out.println(mcuCount);
    		}
    }
    
    public void reConstructP(ArrayList<Integer> in) {
    		// reconstruct P frame
    		int mcuCount = 0;
    		while(mcuCount < blockNum) {
			if(in.get(offset).equals(1)) {
				// if this mcu block is similiar with the block in I frame
				// reference the mcu in I frame
				Integer i = (Integer) in.get(offset+1);
	            byte j = i.byteValue();
	            Integer k = (Integer) in.get((offset+2));
	            byte l = k.byteValue();
	            int pointer = ((j & 0xff)|((l & 0xff) << 8));
	            int index = pointer*8 + (mostRecentIFrame)*blockNum*8;
	            for(int m = 0; m < 8;m++) {
	            		AllBlocks.add(AllBlocks.get(index++));
	            }
	            offset += 3;
	            
			}
			else if(in.get(offset).equals(0)) {
				// this block is not similar with I frame
				offset++;
				seperateRLE();			
			}
			mcuCount += 1;
    		}
}
    
    private void seperateRLE() {
    		// reconstruct block of Y(4), Cr(2), Cb(2)
    		int blockcount = 0;
    		int type ;
    		while(blockcount < 8) {
	    		if(blockcount <4) {
	    			type = 0;
	    			DCACWrapper dcacresult = seperateBlockRLE(type);
	    			Block unDA = dcacresult.deWrap(dcacresult);
	    			AllBlocks.add(unDA);
	    		}else if(blockcount <6 && blockcount >= 4) {
	    			type = 2;
	    			DCACWrapper dcacresult = seperateBlockRLE(type);
	    			Block unDA = dcacresult.deWrap(dcacresult);
	    			AllBlocks.add(unDA);
	    		}else if(blockcount < 8 && blockcount >= 6) {
	    			type = 1;
	    			DCACWrapper dcacresult = seperateBlockRLE(type);
	    			Block unDA = dcacresult.deWrap(dcacresult);
	    			AllBlocks.add(unDA);
	    		}
	        blockcount += 1;
    		}
    		
    }
    
    private void test() throws IOException {
    	Image[] allFrame = new Image[numberOfFrames];
    		String readPath;
    		
    		System.out.println("# of frames" + numberOfFrames);
    		for(int i = 10; i <= 10 + numberOfFrames -1; i++) {
				if(i < 100) {
					readPath = "input/test00" + i + ".JPG";
					
				}else {
					readPath = "input/test0" + i + ".JPG";
				}
						
				System.out.println("read path" + readPath);
				File f = new File(readPath);
				Image image = ImageIO.read(f);
				allFrame[i-10] = image;
				
    		}
    	
    		//writetest(allFrame);
    		
	 		MCU[][] bufferedMCU = new MCU[numberOfFrames][blockNum]; 
	    	SizeTrimer st = new SizeTrimer();
	    	Sampler sp = new Sampler();
			Image im = st.resizeImage(allFrame[1], 1);
			YuvImage imyuv = YuvImage.rgbToYuv(im,1);	
			imyuv = sp.sampling(imyuv, 1);
		    ImageGrid imageGrid = new ImageGrid(imageHeight,imageWidth);
	//	System.out.println("image grid h w  1111  " + imageGrid.h + " "+ imageGrid.w);	
    		for(int i = 0; i< allFrame.length;i++) {
    			Image ii = allFrame[i];			
    			ii  = st.resizeImage(ii, 1);
    			YuvImage newYuv = YuvImage.rgbToYuv(ii,1);
    			newYuv  = sp.sampling(newYuv, 1);
    			MCU [] immcu =  imageGrid.imageGridder(newYuv);
    			
    			bufferedMCU[i] = immcu;
    		}
    			
    	//	System.out.println("image grid h w  22   " + imageGrid.h + "  "+ imageGrid.w);		
    		for(int j = 0; j <bufferedMCU.length;j++) {
    			MCU[] mm = bufferedMCU[j];
    			
    			YuvImage nn = ig.unGrid422(mm);
    			Image immmm = YuvImage.yuvToRgb(nn);
    			test[j] =immmm; 
    		}
    }
    
    private void writetest(Image[] result) throws IOException {
    	int i= 0;
		String format = ".jpg";
		for(Image ii: result) {
    		String outputpre = "output00";
    		String outputName = outputpre + Integer.toString(i++);
    	    String outputPath = "output/";
    	    String output = outputPath + outputName + format;
    	    File outputfile = new File(output);
    	    ImageIO.write((RenderedImage) ii, "jpg", outputfile);
		}
    }
    
    public void write() throws IOException {
    		int i= 0;
    		String format = ".jpg";
	    		for(Image ii: test) {
	    		String outputpre = "output00";
	    		String outputName = outputpre + Integer.toString(i++);
	    		String outputPath = "output/";
	    	    String output = outputPath + outputName + format;
	    	//    System.out.println("open output file " + output);
	    	    File outputfile = new File(output);
	    	    
	    	    ImageIO.write((RenderedImage) ii, "jpg", outputfile);
    	  
    		}
    }
    
    private DCACWrapper seperateBlockRLE(int type) {
    		List<Cell> list = new ArrayList<>();
    		// get the DC value of RLE
    		Integer intlowDC = (Integer) in.get(offset++);
        byte lowDC = intlowDC.byteValue();
        Integer inthighDC = (Integer) in.get((offset));
        byte highDC = inthighDC.byteValue();
        int DC = ((lowDC & 0xff)|((highDC & 0xff) << 8));
        offset += 2;
        
        // get the AC value of RLE
        while(in.get(offset) != 0) {
        		int zeroNum = in.get(offset -1);
        		int value = in.get(offset);
        		list.add(new Cell(zeroNum,value));
        		offset += 2;
        }
        //add the last one cell(all of zero)
        int zeroNum = in.get(offset -1);
		int value = in.get(offset);
		list.add(new Cell(zeroNum,value));
		offset ++;
		
		//  reconstruct DCACWrapper for each 8*8 block
        Cell[] result = new Cell[list.size()];
        for (int i = 0; i < list.size(); i++) {
        		result[i] = list.get(i);
        }
        DCACWrapper dcac = new DCACWrapper(DC,result,type);
        return dcac;
    }
    
    public void deQandDCT() {
    		// for each block do the de quantilize and IDCT
    		for(int i = 0;i < AllBlocks.size();i++) { 
    			Block b = AllBlocks.get(i);    			
    			DCT D = q.DeQuantize(b, b.type);
    			Block newb =  DCT.IDCT(D);
    			AllBlocks.set(i, newb);
    		}
    }
    public MCU[] mcudecoder(int start) {
    		
    	    	MCU[] result = new MCU[blockNum];
    	    	// each mcu[] have blockNum mcu, each mcu have 8 blocks(4Y,2Cr,2Cb)
	    	for(int i = 0;i<blockNum; i++) {
	    		Block[] Y = new Block[4];
	    		Block[] Cr =new Block[2];
	    		Block[] Cb =new Block[2];
	    		for(int j= 0; j < 8;j++) {
	    			if(j<4) {
	    				Y[j] = AllBlocks.get(start + i);
	    			}else if(j>=4 && j<6) {
	    				Cr[j-4] = AllBlocks.get(start +i);
	    			}else if(j>=6 && j<8){
	    				Cb[j-6] = AllBlocks.get(start +i);
	    			}
	    		}
	    		MCU m = new MCU(Y,Cb,Cr);
	    		result[i] = m;
	    		}
	    	return result;	
    		
    }
    
    public Image[] RGBdecoder() {
    		int count = 0;
    		Image[] result = new Image[numberOfFrames];
    		for(int i = 0; i < AllBlocks.size();i += blockNum*8) {
    			System.out.println("Now reconstruct mcu "+ i + " of image "+count);
    			MCU[] mcu = mcudecoder(i);
//    			ImageGrid ig = new ImageGrid();
    			YuvImage yuv = ig.unGrid422(mcu);
    			Image image = YuvImage.yuvToRgb(yuv);
//    			System.out.print("count is " + count);
    			result[count++] = image;
    		}
    		return result;
    }
    
    public static void print(double[][] b) {
    	//print the 2-D array
        for(double[] i : b) {
         for (double j : i) {
          System.out.print(j + " ");
         }
         System.out.println();
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

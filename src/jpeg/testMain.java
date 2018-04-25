package jpeg;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;


public class testMain {
	 public static void main(String[] args) {
	    	System.out.println("1111111111");
	        try { 
	            if (args.length == 1 || args.length == 3 ||  args.length == 5 || args.length == 7) { 
	                String imgFile = "", compressedImgFile = "", samplingString = YUV_444; 
	                int samplingRatio = SubSamplerI.YUV_444; 
	                int qualityFactor = QuantizationI.DEFAULT_QUALITY_FACTOR; 
	                
	                for (int i=0;i < args.length;i+=2) { 
	                    if (args[i].equalsIgnoreCase(SAMPLING_OPTION)) { 
	                        samplingString = args[i+1]; 
	                        samplingRatio = getSamplingRatio(args[i+1]); 
	                    } 
	                    else if (args[i].equalsIgnoreCase(QUALITY_OPTION)) 
	                        qualityFactor = Integer.parseInt(args[i+1]); 
	                    else if (args[i].equalsIgnoreCase(OUTPUT_FILE_OPTION)) 
	                        compressedImgFile = args[i+1]; 
	                    else 
	                        imgFile = args[i]; 
	                } 
	                if (compressedImgFile.equals("")) 
	                    compressedImgFile = imgFile.substring(0,imgFile.indexOf(".")) + ".jpg"; 
	                System.out.print("\nimage file to compress: " + imgFile); 
	                System.out.print("\ncompressed image file: " + compressedImgFile); 
	                Encoder encoder = new Encoder(getImage(imgFile),samplingRatio,qualityFactor); 
	                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(compressedImgFile)); 
	                
	                
	                long startTime = System.currentTimeMillis(); 
	                
	                encoder.compress(bos); 
	                long endTime = System.currentTimeMillis(); 
	                bos.flush(); 
	                bos.close(); 
	                showStats(samplingString,qualityFactor,imgFile, 
	                          compressedImgFile,endTime - startTime); 
	            } 
	            else 
	                System.out.print("\nUsage: java dmms.jpeg.test.JPEGEncoder [-s <samplingRatio>] [-q <qualityFactor>] [-o <outputFile>] <imgFile>"); 
	        } 
	        catch (Exception e) { 
	            System.out.print("\nException occurred: " + e.getMessage() + "\n"); 
	            e.printStackTrace(); 
	        } 
	    } 
}

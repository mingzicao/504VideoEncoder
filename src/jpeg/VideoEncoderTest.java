package jpeg;

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jpeg.Quantizer;
import jpeg.Sampler;
import jpeg.readImages_1;
//import movieplus.*;
/*this is the lastest version
 * 
 * 
 * 
 * 1.  Under src/jpeg : run this CML:
javac -cp ".:./src/jpeg/libs/*" *.java

2 back to /src directory, run this CML

java  -cp “.:./src/jpeg/libs/*"  jpeg/VideoEncoderTest -s 4:2:2 -q 5 -p /Users/apple/Documents/workspace/ImageCompression/input  -n 3

-s : you can input a different sampling ratio
-q: you can input a qualify factor btw 5 - 100
-p: the input directory
-n: how many pictures do you want to encode


 * 
 * */
public class VideoEncoderTest {

    private static final String SAMPLING_OPTION = "-s"; 
    private static final String QUALITY_OPTION = "-q"; 
 //   private static final String OUTPUT_FILE_OPTION = "-o";
    private static final String INPUT_PATH = "-p";
    private static final String IMAGE_NUMBERS = "-n";
 
    private static final String YUV_444 = "4:4:4"; 
    private static final String YUV_422 = "4:2:2"; 
    private static final String YUV_420 = "4:2:0"; 
    
    // 命令行 ： -s [sampling ration]  -q [quality factor] -o [output file] -p [inputfile path] -n [how many pictures]
    //  s = 1    -q = 3    -o = 5  -  p = 7    -n =  9
    public static void main(String[] args) {
    	
        try { 
          	int j = 0;     	
          	if (args.length == 2 || args.length == 4 ||  args.length == 6 || args.length == 8 ) { 
           
            	  String readPath  ="";
              //  String compressedImgFile = "";
                String samplingString = YUV_422; 
                int samplingRatio = Sampler.YUV_422; 
                int qualityFactor = Quantizer.DefalutQualityFactor; 
                int ImageNumber = 0; 
               
                for (int i = 0; i < args.length;i+=2) { 
                 	//
                    if (args[i].equalsIgnoreCase(SAMPLING_OPTION)) {   //  -s
                        samplingString = args[i+1]; 
                        samplingRatio = getSamplingRatio(args[i+1]); 
                    } 
                    else if (args[i].equalsIgnoreCase(QUALITY_OPTION)) { // -q
                        qualityFactor = Integer.parseInt(args[i+1]); 
                    }
                    else if (args[i].equalsIgnoreCase(INPUT_PATH)) { // -p
                    	 readPath = args[i+1];
                    } else { // -n
                    	ImageNumber = Integer.parseInt(args[i+1]);  // -n
                    }
                   
                } 
              
                System.out.println(" sampling ratio  : " + samplingRatio);
                System.out.println(" quality factor : " + qualityFactor);
                System.out.println("image file to compress: " + readPath); 
        //        System.out.println("compressed image file: " + compressedImgFile);
                System.out.println(" # of images to encode: " + ImageNumber);
                File[] files = new File[ImageNumber];
        		  String s = null;
                for(int i = 10; i < (ImageNumber+10); i++) {
	        			if(i < 100) {
	        				 s = readPath  + "/test00" + i + ".JPG";
	        				
	        			} else {
	        			     s = readPath  + "/test00" + i + ".JPG";
	        				
	        			}   			
        				File f = new File(s);
        				System.out.println(" reading image  ... " + readPath);
        				int index = i-10;
        			//	System.out.println(" i- 10 " + index);
        				files[i-10] = f;
         	
        			//files[i-10] = f;
        			}
                System.out.println(" ----------------------start encode  ----------------------  ");
                readImages_1.encoderun(files, samplingRatio, qualityFactor);
                System.out.println("  ---------------------- start decode  ---------------------- ");
                Decode de = new Decode(new File("encoding.txt"));
                System.out.println("  ---------------------- write to Disk  ---------------------- ");
                de.write();
                System.out.println("  ----------------------make movie now  ---------------------- ");
                FilesToMov.genMovie("/output", 770, 1026, ImageNumber);
               
            } 
            else 
                System.out.print("\nUsage: java jpeg.test.JPEGEncoder [-s <samplingRatio>] [-q <qualityFactor>] [-p <Input File directory>] [-n <Total number of Images >]"); 
        } 
        catch (Exception e) { 
            System.out.print("\nException occurred: " + e.getMessage() + "\n"); 
            e.printStackTrace(); 
        }     
        
    } 
    
    private static int getSamplingRatio(String ratio) { 
        if (ratio.equals(YUV_444)) 
            return Sampler.YUV_444; 
        else if (ratio.equals(YUV_422)) 
            return Sampler.YUV_422; 
        else if (ratio.equals(YUV_420)) 
            return Sampler.YUV_420; 
        throw new IllegalArgumentException("Invalid subsampling ratio " + ratio + "."); 
    } 
}

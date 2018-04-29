package mpeg;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

//import javax.media.jai.PlanarImage;


public class Frame implements java.io.Serializable{

	// ByteArrayOutputStream baos;
	byte[] bytearray;
	int height,width;
	
	
	public Frame(String fileName,float quality) throws IOException{
		//String dirName = System.getProperty("user.dir");
		//String fName = dirName + "/data/" + fileName;
		System.out.println(fileName);
		
		
		File imageFile = new File(fileName);
		
		InputStream is = new FileInputStream(imageFile);
		
		// create a BufferedImage as the result of decoding the supplied InputStream
		BufferedImage image = ImageIO.read(is);
		
		// get all image writers for JPG format
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

		if (!writers.hasNext())
			throw new IllegalStateException("No writers found");

		
		ImageWriter writer = (ImageWriter) writers.next();

		ImageWriteParam param = writer.getDefaultWriteParam();

		// compress to a given quality
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		
		IIOImage tempImg = new IIOImage(image, null, null); 
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		writer.setOutput(ImageIO.createImageOutputStream(byteArrayOut));
		// appends a complete image stream containing a single image and
		//associated stream and image metadata and thumbnails to the output
		writer.write(null, tempImg, param);
		
//		System.out.print(byteArrayOut.toString());
		
		// get byte[] from byteArrayOut
		// this.baos = byteArrayOut; 
		this.bytearray = byteArrayOut.toByteArray();
		
		// convert IIOImage tempImg into RenderedImage
		RenderedImage rImg = tempImg.getRenderedImage(); 

		// get height and width from RenderedImage
		this.height = rImg.getHeight();
		this.width = rImg.getWidth();
//		System.out.print(this.bytearray);
//		
//		System.out.print(this.height);
//		System.out.print( this.width);
//		System.out.println("\n");
//		byte[] bytes = {-1, 0, 1, 2, 3 };
	    StringBuilder sb = new StringBuilder();
	    int i=0;
	    for (byte b : this.bytearray) {
	    	if(i==this.width){
//	    		sb.append("\n");
//	    		System.out.println("");
	    		i=0;

	    	}
	        sb.append(String.format("%02X ", b));
	        System.out.print(String.format("%02X ", b));
	        i=i+1;
	    }
	    
//	    System.out.println("\n");
//	    for(i=0;i<this.height;i++){
//	    	System.out.println(sb.substring(255*i, 255*(i+1)).toString());
//	    }
	    
	    
//	    System.out.println("\n");
//	    System.out.println("\n");
//	    System.out.println("\n");
//	    System.out.println("\n");
	    
		// close all streams
		is.close();
		writer.dispose();
		
	}

	
	
//	public Frame(String fileName,float quality) throws IOException{
//		System.out.println(fileName);
//		ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
//		BufferedImage img=ImageIO.read(new File(fileName));		
//		this.height = img.getHeight();
//		this.width = img.getWidth();
//		ImageIO.write(img, "jpg", baos);
//		this.bytearray = baos.toByteArray();
//	}
	
	public static void printHexFile(String filename) throws IOException{
	    FileInputStream in = new FileInputStream(filename);
	    int read;
	    while((read = in.read()) != -1){
	        System.out.print(Integer.toHexString(read) + " ");
//	        System.out.print(read + "\t");
	        System.out.print((char)read);
	    }
	    
//	    FileInputStream in2 = new FileInputStream(filename);
//	    int read2;
//	    System.out.println();
//	    while((read2 = in2.read()) != -1){
//	    System.out.print(read2 + " ");
//	    }
	}
	public void printBinaryFile(String filename) throws IOException{
	    FileInputStream in = new FileInputStream(filename);
	    int read;
	    while((read = in.read()) != -1){
	        System.out.print(Integer.toBinaryString(read) + "\t");
	    }
	}

	    
	    
	public static void main(String[] args) throws IOException{

		
//		String fileName = System.getProperty("user.dir") + "/data/large/YOURIMAGE" + i +".jpg";
//		System.out.println("Getting raw RGBA components for " + fileName);

		
//		String files[] = new String[6];
//		files[0] = "one";
//		files[1] = "two";
//		files[2] = "three";
//		files[3] = "four";
//		files[4] = "five";
//		files[5] = "six";
		
		
		printHexFile(System.getProperty("user.dir") + "/data/large/puredark.jpg");
		
		
		Frame movie[] = new Frame[1];
		
//		for(int i = 0; i < 1; i++){
//			movie[i] = new Frame(System.getProperty("user.dir") + "/data/large/YOURIMAGE" + (i+1) +".jpg",1.0f);
			movie[0] = new Frame(System.getProperty("user.dir") + "/data/large/puredark.jpg",1.0f);
			movie[1] = new Frame(System.getProperty("user.dir") + "/data/large/purewhite.jpg",1.0f);
//		}
//		System.out.print(movie.toString());
		
		

	}
	
	
}

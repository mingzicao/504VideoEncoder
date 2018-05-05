package jpeg;

import java.lang.*;
import java.nio.*;
import java.nio.file.Files;
import java.util.*;

import javax.imageio.*;

import jpeg.RunLengthEncoder.Cell;

import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class readImages_1 {
	
	FileOutputStream filesys;
	int quantityNum;
	MCU[][] bufferedMCU;
	static ImageGrid imageGrid;
	
	
	
	public static void encoderun(File[] files,int SamplingRatio,int quantityNum) throws IOException {
		Image[] allFrame = new Image[files.length];	
		int count = 0;
		SamplingRatio = 1;
		for(File ele:files) {
			System.out.println("Reading file # : " + count);
			Image image = ImageIO.read(ele);
			allFrame[count++] = image;
		}
		System.out.println("Reading complete.");
		int fps = 10;
		int GOP = 4;
		String saveName = "encoding.txt";
		// initial the constructor of encoder(readImages_1)
		int frameNum = allFrame.length;
		Image image = allFrame[0];
		SizeTrimer st = new SizeTrimer();
		image = st.resizeImage(image, SamplingRatio);
		int picHeight = image.getHeight(null);
		int picWidth = image.getWidth(null);
		YuvImage yuv = YuvImage.rgbToYuv(image,1);
		Sampler sp = new Sampler();
		yuv = sp.sampling(yuv, SamplingRatio);	
		imageGrid = new ImageGrid(image.getHeight(null),image.getWidth(null));
		MCU [] mcu =  imageGrid.imageGridder(yuv);		
		
		long start = System.currentTimeMillis();
		
		readImages_1 test = new readImages_1(mcu.length, frameNum, allFrame,80);
		
		File saveFile = new File(saveName);
		Files.deleteIfExists(saveFile.toPath());
		try {
			test.filesys = new FileOutputStream(saveName, true);
		}
		catch(FileNotFoundException e) {}
		test.genBitstream(mcu.length, GOP, picHeight, picWidth, fps, frameNum, saveName);
		test.encodingMap(saveName, GOP, allFrame);
		test.filesys.close();
		long end = System.currentTimeMillis();
		long costtime = end - start;
		float cossec = (float)costtime/1000;
		
	}
	long headerLen;
	
	public readImages_1(int mcuNum, int frameNum,Image[] allFrame, int quantityNum) throws IOException {
		this.quantityNum = quantityNum;
		bufferedMCU = new MCU[frameNum][mcuNum];
		for(int i = 0; i< allFrame.length;i++) {
			Image im = allFrame[i];
			SizeTrimer st = new SizeTrimer();
			im = st.resizeImage(im, 1);
			YuvImage imyuv = YuvImage.rgbToYuv(im,1);
			Sampler sp = new Sampler();
			imyuv = sp.sampling(imyuv, 1);
			MCU [] immcu =  imageGrid.imageGridder(imyuv);
			bufferedMCU[i] = immcu;
		}
		
	}
	
	//genBitstream creates a bitstream that contains header of images
	public void genBitstream(int mcuNum, int GOP, int picHeight,
			int picWidth, int fps, int frameNum, String saveName) throws IOException {
		byte[] headerInfo = new byte[10];
		headerInfo[0] = (byte) (mcuNum & 0xFF); 
		headerInfo[1] = (byte) ((mcuNum >> 8) & 0xFF);
		headerInfo[2] = (byte) GOP;
		headerInfo[3] = (byte) (picHeight & 0xFF); 
		headerInfo[4] = (byte) ((picHeight >> 8) & 0xFF); 
		headerInfo[5] = (byte) (picWidth & 0xFF); 
		headerInfo[6] = (byte) ((picWidth >> 8) & 0xFF); 
		headerInfo[7] = (byte) fps; 
		headerInfo[8] = (byte) (frameNum & 0xFF); 
		headerInfo[9] = (byte) ((frameNum >> 8) & 0xFF);
		
		headerLen += 10;
		filesys.write(headerInfo);
		
	}
	
	public void encodingMap(String saveName, int GOP, Image[] allFrame) throws IOException {
		int count = 0;
		MCU[] IMCU = null;
		
		for(int j = 0; j < allFrame.length/2;j++) {
			Image frame = allFrame[j];
			SizeTrimer st = new SizeTrimer();
			int SamplingRatio = 1;
			frame = st.resizeImage(frame, SamplingRatio);
			YuvImage yuv = YuvImage.rgbToYuv(frame,1);
		    Sampler sp = new Sampler();
		    yuv = sp.sampling(yuv, SamplingRatio);	
//		    ImageGrid imageGrid = new ImageGrid();
//		    ImageGrid imageGrid = new ImageGrid(im.getHeight(null),im.getWidth(null));
			
			//to calculate whether it's I frame or not
			if(count % GOP == 0)	{
//				System.out.println("Iam an I frame!!!!");
				
				MCU[] mcu =  imageGrid.imageGridder(yuv);
				IMCU = mcu;
				
				ItoStream(mcu);
				
			} else {
				MCU[] mcu =  imageGrid.imageGridder(yuv);
//				System.out.println("Iam a P frame~~~~");
                for(int i = 0; i<mcu.length;i++){
                		int[][] Pcheck = mcu[i].getYBlockArray()[0].getData();
                		int[][] Icheck = IMCU[i].getYBlockArray()[0].getData();
                		boolean s = checkSimilar(Pcheck,Icheck);
                		if(s) {
                			int pointer = i;
                			byte[] headerInfo = new byte[3];
                			headerInfo[0] = (byte)1;
                			headerInfo[1] = (byte)(pointer & 0xFF);
                			headerInfo[2] = (byte)((pointer >> 8) & 0xFF);
                			filesys.write(headerInfo);
                		}else {
                			filesys.write((byte)0);
                			PtoStream(mcu[i]);
                		}
                		
                }
			}
			count++;
		}
		System.out.println("the video encoding part finished");
	}
	
	public void ItoStream(MCU[] mcu) throws IOException {
		for (MCU m : mcu) {
		    	Block[] Y  = m.getYBlockArray();
		    	Block[] Cr = m.getCrBlockArray();
		    	Block[] Cb = m.getCbBlockArray();
		    	for(Block b: Y) {
		    		BlockToStream(b);
		    	}
		    	for(Block b: Cr) {
		    		BlockToStream(b);
		    	}
		    	for(Block b: Cb) {
		    		BlockToStream(b);
		    	}
	    	 
	    	 }
	}
	
	public void PtoStream(MCU m) throws IOException {
		Block[] Y  = m.getYBlockArray();
    		Block[] Cr = m.getCrBlockArray();
    		Block[] Cb = m.getCbBlockArray();
    		for(Block b: Y) {
    			BlockToStream(b);
    		}
    		for(Block b: Cr) {
    			BlockToStream(b);
    		}
    		for(Block b: Cb) {
    			BlockToStream(b);
    		}
	}
	
	
	private void BlockToStream(Block b) throws IOException {
		int type = 0;
		Quantizer q = new Quantizer(this.quantityNum);
		RunLengthEncoder rle  = new RunLengthEncoder();
		
		DCT d = DCT.FDCT(b);
		Block block = q.quantizeDCTBlock(d, type);
		Cell[] cell = rle.RLEncoding(block.getZigZag(block));
		int DC = block.getData()[0][0];
		byte[] bArray = new byte[cell.length*2 +2];
		int index = 0;
		bArray[index++] = (byte)(DC & 0xFF);
		bArray[index++] = (byte)((DC >> 8) & 0xFF);
		for(int i = 0; i< cell.length;i++) {
			bArray[index++] = (byte)(cell[i].getZero());
			bArray[index++] = (byte)(cell[i].getValue());
		}
		filesys.write(bArray);
//		System.out.println(index);
//		DCACWrapper daw  = new DCACWrapper(DC, cell, type);
//		WrapperToFile(daw);
		
	}

	
	private boolean checkSimilar(int[][] a, int[][] b) {
		int suma = 0;
		int sumb = 0;
		boolean result;
		for(int i = 0; i<a.length;i++) {
			for(int j = 0;j < a[0].length;j++) {
				suma += a[i][j];
			}
		}
		for(int i = 0; i<b.length;i++) {
			for(int j = 0;j < b[0].length;j++) {
				sumb += b[i][j];
			}
		}
		if(Math.abs(suma - sumb) < 100) {
			result = true;
		}else {
			result = false;
		}
		return result;
	}
	
}

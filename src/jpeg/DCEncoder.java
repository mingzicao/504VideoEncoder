package jpeg;

import java.io.BufferedOutputStream;
import java.io.IOException;


public class DCEncoder {
	 protected void encodeDC(int component, HuffmanEncoder hf)  { 
		  System.out.println("-------------------------   encoding  DC  ---------------------"); 
		  int curDC =  hf.cur.getData()[0][0]; 
		  int prevDC = 0 ;
		  if (hf.prev[component] != null) {
			  prevDC = hf.prev[component].getData()[0][0];
		  }
		  int diff =  curDC - prevDC; 
	        int temp = diff; 
	        if(diff < 0) { 
	        	diff = -diff; 
	            temp--; 
	        } 
	        int bitLength = 0; 
	        while (diff != 0) { 
	        	bitLength++; 
	        	diff >>= 1; 
	        } 
	        System.out.println(" cur DC  : "+ curDC);
	        System.out.println(" bit length : "+ bitLength); 	        
	        int[][] huffTable =  hf.getHuffmanTable(component, true);
	 
	        System.out.println(" huff table [bitLength][0]: "+ huffTable[bitLength][0] +" "+ huffTable[bitLength][1]);
	 //       bufferIt(huffTable[bitLength][0], huffTable[bitLength][1],bos); 
	        if (bitLength != 0) {
	            // store DC value difference 
	        	  System.out.println(" temp bits "+ temp + "    "+ bitLength);
	///            bufferIt(temp, bitLength, bos); 
	        }
	    } 
}

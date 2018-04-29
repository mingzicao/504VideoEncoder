package jpeg;

import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jpeg.RunLengthEncoder.Cell;

public class RunLengthEncoder {
	
	public static class Cell { 
		    int nonZeroValue;
		    int sequantialZero;
		    Cell(int z, int NonZeroValue) { 
		      	this.nonZeroValue = NonZeroValue;
		      	this.sequantialZero = z;
		    } 
		    public int getValue(){
		    	return nonZeroValue;
		    }
		    public int getZero(){
		    	return sequantialZero;
		    }
		
	}		
	
	public Cell[] RLEncoding (int[] array) {
		  	List<Cell> list = new ArrayList<>();
	        int run = 0; 
	        for (int i = 1 ; i < array.length; i++) { 
	            if (array[i] == 0) {
	                run++;
	               
	            }
	            else { 
	                list.add(new Cell(run, array[i])); 
	                run = 0; 
	            } 
	        } 
	        if (run > 0)  {
	        	list.add(new Cell(run, 0));
	        }
	        
	        Cell[] result = new Cell[list.size()];
	        for (int i = 0; i < list.size(); i++) {
	        	result[i] = list.get(i);
	        }
	        return result;
	        	
	}

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
                 System.out.println("Encode AC " + huffmanTable[tableIndex][0] + " , " + huffmanTable[tableIndex][1] + "  "); 
      //           bufferIt(huffmanTable[tableIndex][0], huffmanTable[tableIndex][1], bos); 
                 System.out.println("bitlen   temp " + bitLength + " , " + complement + "  "); 
     //            bufferIt(complement, bitLength, bos); 
             } 
             else {
                 // end-of-block sentinel value with zero-level 
                System.out.println("end-of-block sentinel value" + huffmanTable[0][0] + " , " + huffmanTable[0][1] + "  "); 
      //           bufferIt(huffmanTable[0][0], huffmanTable[0][1], bos); 
             }
         } 
		
	}

}

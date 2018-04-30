package jpeg;

import java.awt.Component;

/*Each Block is 8px * 8px unit;
 *Each Min Computation Unit (MCU) is 2 * 2 blocks = Block[2][2];
 *According to different Sampling Ratio
 *Get new block;
 *static final int Y_COMP = 0; 
	static final int Cb_COMP = 1; 
	static final int Cr_COMP = 2; 
 *
 * */
public class Block {
	static final int SIZE = 8;
    static final int[] ZIGZAG = { 0,  1,  8,  16,  9,  2,  3, 10, 
          17, 24, 32, 25, 18, 11,  4,  5, 
          12, 19, 26, 33, 40, 48, 41, 34, 
          27, 20, 13,  6,  7, 14, 21, 28, 
          35, 42, 49, 56, 57, 50, 43, 36, 
          29, 22, 15, 23, 30, 37, 44, 51, 
          58, 59, 52, 45, 38, 31, 39, 46, 
          53, 60, 61, 54, 47, 55, 62, 63};
    
    static final int[] deZIGZAG = {
    	0, 1, 5, 6, 14, 15, 27, 28, 
    	2, 4, 7, 13, 16, 26, 29, 42,
    	3, 8, 12, 17, 25, 30, 41, 43,
    	9, 11, 18, 24, 31, 40, 44, 53,
    	10, 19, 23, 32, 39, 45, 52, 54,
    	20, 22, 33, 38, 46, 51, 55, 60,
    	21, 34, 37, 47, 50, 56, 59, 61,
    	35, 36, 48, 49, 57, 58, 62, 63
    };
	
	int[][] data = new int[SIZE][SIZE];
	int type;
	
	public Block(int[][] blockData, int c) {
		this.data = blockData;
		this.type = c; // 0 / 1 / 2
	}
	public int[][] getData() {
		return data;
	}
	public static int[][] deZigZag (int[] array) {
		int[][] block = new int[SIZE][SIZE];
		int[] l = new int [SIZE*SIZE];
		int k = 0;
		for (int i : deZIGZAG) {
			l[k++] = array[i];
		}
		k = 0;
		for (int i = 0 ; i< SIZE; i++) {
			for (int j = 0 ; j< SIZE; j++) {
				block[i][j] = l[k++];
			}
		}
		return block;
	}
	public int[] getZigZag(Block block) {
		int[][] data = block.getData();
		int[] data1D = new int[Block.SIZE*Block.SIZE];
		int index = 0;
		for (int[] arr : data) {
			for (int i: arr) {
				data1D[index++] = i;
			}
		}

		int[] dataZigZag = new int[Block.SIZE*Block.SIZE];
		index = 0;
		
		for (int i : ZIGZAG) {
			dataZigZag[index++] = data1D[i]; 
		}
		return dataZigZag;
		
	} 
	
	
	public static void main (String[] args) 
    {	

			int [] za = new int[] {
					0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57,
					50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63 
			};
			int[][] result = Block.deZigZag(za);
			
			print(result);
			
			int [] array = new int[] {
			0, 1, 2, 3, 4, 5, 6, 7, 
			8, 9, 10, 11, 12, 13, 14, 15, 
			16, 17, 18, 19, 20, 21, 22, 23, 
			24, 25, 26, 27, 28, 29, 30, 31, 
			32, 33, 34, 35, 36, 37, 38, 39, 
			40, 41, 42, 43, 44, 45, 46, 47, 
			48, 49, 50, 51, 52, 53, 54, 55, 
			56, 57, 58, 59, 60, 61, 62, 63 };
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

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
	
	int[][] data = new int[SIZE][SIZE];
	int type;
	
	public Block(int[][] blockData, int c) {
		this.data = blockData;
		this.type = c; // 0 / 1 / 2
	}
	public int[][] getData() {
		return data;
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
}

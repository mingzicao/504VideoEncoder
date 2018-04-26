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
	
	
	
	int[][] data = new int[SIZE][SIZE];
	int type;
	
	
	public Block(int[][] blockData, int c) {
		this.data = blockData;
		this.type = c; // 0 / 1 / 2
	}
	public int[][] getData() {
		return data;
	}
}

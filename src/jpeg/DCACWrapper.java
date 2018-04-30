package jpeg;

import jpeg.RunLengthEncoder.Cell;

public class DCACWrapper {
	private Cell[] AC;
	private int DC; 
	private int Component;
	public DCACWrapper ( int dc , Cell[] ac, int C) {
		this.AC = ac;
		this.DC = dc;
		this.Component = C;
	}
	public Cell[] getAC() {
		return AC;
	}
	public int getDC() {
		return DC;
	}
	public void setAC(Cell[] c) {
		this.AC = c;
	}
	public void serDC(int d) {
		this.DC = d;
	}
	public static void print (DCACWrapper dca) {
		System.out.println("this block type :" + dca.Component);
		System.out.println("this block is wrapped as :" );
		System.out.println("dc :" + dca.getDC());
		Cell[] cells = dca.getAC();
		for (Cell c : cells) {
			c.print();
		}
		System.out.println();
	}
	
	
	public Block deWrap(DCACWrapper dca) {
		int size = Block.SIZE* Block.SIZE;
		int[] linear = new int[size];
		linear [0] =  dca.getDC();
		
		int index = 1;
		
		Cell[] cell = dca.getAC();
	
		for (int i = 0; i < cell.length; i++) {
			int zero = cell[i].getZero();
			int value = cell[i].getValue();
			
			
			if (zero == 0 ){
				linear[index++] = value;
			} else {
				
				while (zero > 1) {
					linear[index++] = 0;
					zero--;
				}
				linear[index++] = value;
			}
		//	System.out.println("index = " + index);
			
		}
		int[][] data = Block.deZigZag(linear);
	
		return new Block(data, dca.Component);
	 
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

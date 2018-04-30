package jpeg;

public class aa {
	static final int[] ZIGZAG = { 0,  1,  8,  16,  9,  2,  3, 10, 
        17, 24, 32, 25, 18, 11,  4,  5, 
        12, 19, 26, 33, 40, 48, 41, 34, 
        27, 20, 13,  6,  7, 14, 21, 28, 
        35, 42, 49, 56, 57, 50, 43, 36, 
        29, 22, 15, 23, 30, 37, 44, 51, 
        58, 59, 52, 45, 38, 31, 39, 46, 
        53, 60, 61, 54, 47, 55, 62, 63};
	
	 public static void main (String[] args) 
	    {	int [][] m = new int [8][8];
	    	int [] lm = new int[64];
	    	int index = 0;
	    	for (int i = 0; i < 8; i++) {
	    		for (int j = 0; j < 8; j ++) {
	    			m[i][j] = index;
	    			System.out.print(index+", ");
	    			lm[index] = index;
	    			index++;
	    		}
	    		System.out.println();
	    	}
	    	
//	    	for (int i : lm) {
//	        	System.out.print(i+ " ");
//	        }
//	    	System.out.println();
	    	
	    	int[] mz = new int[64];
	    	
	    	int j = 0;
	    	for (int i : ZIGZAG) {
	    		mz[j++] = lm[i]; 
	    	}
	    	for (int i : mz) {
	        	System.out.print(i +", ");
	        }
	    	System.out.println();
	    	
	    	
	    
	    	
	        int [] deZigZag = new int[64];
	        for (int i = 0; i < 64; i++) {
	        	deZigZag[ ZIGZAG[i] ] =  i;
	        	
	        }
	      
	        System.out.println();
	        int [] array = new int[64];
	        j = 0;
	        
	    	for (int i : deZigZag) {
	    		array[j++] = mz[i];
	    	}
	    	for (int i : deZigZag) {
	        	System.out.print(i+",");
	        }
	    }
}

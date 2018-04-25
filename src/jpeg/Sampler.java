package jpeg;

public class Sampler {
	
		static final int YUV_444 = 0; 
		static final int YUV_422 = 1; 
		static final int YUV_420 = 2;
		   
		

		public static boolean checkValidSamplingRatio(int sr) {
			if (sr == YUV_444 || sr ==  YUV_422 || sr == YUV_420) {
				return true;
			}
			return false;
		}

}

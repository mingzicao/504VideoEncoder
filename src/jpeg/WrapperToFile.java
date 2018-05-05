package jpeg;

import java.io.FileOutputStream;
import java.io.IOException;

public class WrapperToFile {
	
	public WrapperToFile(DCACWrapper daw,FileOutputStream filesys) throws IOException{
		int len = daw.getAC().length*2 + 2;
		byte[] bArray = new byte[len];
		bArray[0] = (byte)(daw.getDC()& 0xFF);
		bArray[1] = (byte)((daw.getDC() >> 8) & 0xFF);
		int index = 2;
		for(int i = 0;i<daw.getAC().length;i++) {
			bArray[index++] = (byte)daw.getAC()[i].getZero();
			bArray[index++] = (byte)daw.getAC()[i].getValue();
		}
		filesys.write(bArray);
	}
}

package jpeg;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;

import org.jim2mov.core.DefaultMovieInfoProvider;
import org.jim2mov.core.FrameSavedListener;
import org.jim2mov.core.ImageProvider;
import org.jim2mov.core.Jim2Mov;
import org.jim2mov.core.MovieInfoProvider;
import org.jim2mov.core.MovieSaveException;
import org.jim2mov.utils.MovieUtils;

public class FilesToMov implements ImageProvider, FrameSavedListener{
    private ArrayList<String> fileArray = null;
    // 文件类型
    private int type = MovieInfoProvider.TYPE_QUICKTIME_JPEG;
    // 主函数
	public static void main(String[] args) throws MovieSaveException {
		System.out.println("begin ");
//		ArrayList<String> fileArray = new ArrayList<>();
//		String readPath = "";
//		for(int i = 0; i <= 99; i++) {
//			readPath = "/Users/limuzi/Downloads/test images/image" + i + ".jpg";
//			System.out.println(readPath);
//			fileArray.add(readPath);
//		}
//		
//		new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "Test.mov", 770, 1026);
		genMovie("/Users/apple/Documents/workspace/ImageCompression/output", 770, 1026, 10);
	}
	
	public FilesToMov(ArrayList<String> fileArray, int type, String path, int Height, int Width) throws MovieSaveException {
		this.fileArray = fileArray;
		this.type = type;
		DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(path);
		// set frame per second
		dmip.setFPS(20);
		// ser frames
		dmip.setNumberOfFrames(fileArray.size());
		// set height
		dmip.setMWidth(Width);
		// 设置视频宽度
		dmip.setMHeight(Height);
		new Jim2Mov(this, dmip, this).saveMovie(this.type);;
	}

	@Override
	public void frameSaved(int frameNumber) {
        System.out.println("Saved frame: " + frameNumber);
	}

	@Override
	public byte[] getImage(int frame) {
		try {
			return MovieUtils.convertImageToJPEG(new File(fileArray.get(frame)), 1.0f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void genMovie(String FilePath, int Height, int Width, int bound) throws MovieSaveException {
		ArrayList<String> fileArray = new ArrayList<>();
		String readPath = "";
		File[] listFiles = new File(FilePath).listFiles();
	//	int bound = listFiles.length;
		for(int i = 0; i < bound - 1; i++) {
			readPath = FilePath + "/output00" + i + ".jpg";
		//	System.out.println(readPath);
			fileArray.add(readPath);
			System.out.println("done fame" + i);
		}
		
		new FilesToMov(fileArray, MovieInfoProvider.TYPE_QUICKTIME_JPEG, "Result111.mp4", Height, Width);
		System.out.println("movie done");
	}
}

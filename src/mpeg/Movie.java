package mpeg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// methods for adding frames to a movie and saving 
public class Movie  implements java.io.Serializable{

	Frame frames[];
	float quality;
	
	public Movie(String[] files, float quality) throws IOException{
		this.quality = quality;
		frames = new Frame[files.length];
		for(int i = 0 ; i < files.length;i++){
			frames[i] = new Frame(files[i],quality);
		}
	}
	
	
	public void saveMovie(String fileName,String dirName){
		//http://www.tutorialspoint.com/java/java_serialization.htm
		try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(dirName + "/movies/" + fileName+ ".ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in " + dirName + "movies/");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public static void main(String[] args) throws IOException{
		int num_of_frames = 10;
		String files[] = new String[num_of_frames];
		for(int i = 0; i < num_of_frames; i++){
			files[i] = System.getProperty("user.dir") + "/data/large/YOURIMAGE" + (i+1) +".jpg";
		}

		Movie m = new Movie(files,1.0f);
		m.saveMovie("movie5", System.getProperty("user.dir")+"/data");
		
	}
	
}

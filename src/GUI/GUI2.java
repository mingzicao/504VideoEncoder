package GUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI2 extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		GUI5 pane=new GUI5(stage);
		Scene scene=new Scene(pane);
		stage.setScene(scene);
		stage.setTitle("VideoPlayer");
		stage.show();
	}
	
	public void playOut(String[] args){
		Application.launch(args);
	}
	
//	public static void main(String[] args) {
//		GUI2 test = new GUI2();
//		test.playOut(args);
//	}

}


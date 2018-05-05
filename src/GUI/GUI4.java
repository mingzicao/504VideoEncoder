package GUI;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class GUI4 {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI4 window = new GUI4();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI4() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(new Rectangle(5, 5, 50, 50));
		comboBox.addItem("1");
		comboBox.addItem("2");
		comboBox.addItem("3");
		frame.getContentPane().add(comboBox);
		comboBox.setBounds(39, 36, 52, 60);
		frame.getContentPane().add(comboBox);
	}

}

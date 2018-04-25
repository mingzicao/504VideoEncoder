package jpeg;


public class Quantizer {
	static final int DefalutQualityFactor = 100;
	
	private int qualityFactor = Quantizer.DefalutQualityFactor;
	
	public Quantizer(int qualityFactor) { 
	 this.qualityFactor = qualityFactor; 
	} 
}

package annexes;

/**
 * 
 * @author Group LAMA
 *
 */
public class Chrono {

	private long tempsDepart=0;
	private long tempsFin=0;
	private long pauseDepart=0;
	private long pauseFin=0;
	private long duree=0;

	/**
	 * Start the Chrono
	 */
	public void start(){
		tempsDepart=System.currentTimeMillis();
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
	}

	/**
	 * Pause the Chrono
	 */
	public void pause(){
		if(tempsDepart == 0)
			return;
		pauseDepart = System.currentTimeMillis();
	}

	/**
	 * Resume the Chrono
	 */
	public void resume(){
		if(tempsDepart == 0) 
			return;
		if(pauseDepart == 0) 
			return;
	
		pauseFin=System.currentTimeMillis();
		tempsDepart=tempsDepart+pauseFin-pauseDepart;
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
		duree=0;
	}

	/**
	 * Stop the Chrono
	 */
	public void stop() {
		if(tempsDepart == 0)
			return;
		
		tempsFin=System.currentTimeMillis();
		duree=(tempsFin-tempsDepart) - (pauseFin-pauseDepart);
		tempsDepart=0;
		tempsFin=0;
		pauseDepart=0;
		pauseFin=0;
	}        

	/**
	 * Get Duree
	 * @return duree in seconds
	 */
	public long getDureeSec(){
		return duree/1000;
	}

	/**
	 * Get Duree
	 * @return duree in milliseconds
	 */
	public long getDureeMs(){
		return duree;
	}        

} 

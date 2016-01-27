// This entire file is apart of my masterpiece
// Virginia Cheng
// The purpose of this class is to take care of the final level and the boss pizza.
// I think it is well designed because it is a simple class that only deals with one level.
// I originally had only one class for this project, so I decided to make this a seperate class in order to make it easier to read and understand.
// I also changed some variable names so they were more descriptive and changed the method names so they were consistently formatted with a verb first and then the noun.
// Like in my original code, I believe there is no duplicated code and no magic numbers.
// The design of this class is pretty simple. It sets up the boss level, tracks how many lives the boss currently has, and determines when and where the boss moves, along with allowing the programmer to input the number of lives and the speed of the boss which makes the class more usable and versatile. 

package game_vc54;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class bossLevel{
	private int maxLives;
	private int bossLives = 0;
	private int moveSpeed;
	private int timeCounter = 0;
	private ImageView bossPizza;
	double size;
	
	public bossLevel(int numLives, int numTime, ImageView bossImage){
		maxLives = numLives;
		moveSpeed = numTime;
		
    		bossPizza = bossImage;
	}
	
	public void moveBoss(){
	 	timeCounter++;
    		if(timeCounter > moveSpeed){
    			setCoordinates(bossPizza);
    			timeCounter = 0;
    		}
    	}
    
	private void setCoordinates(ImageView obj){
		obj.setX(Math.random()*size);
		obj.setY(Math.random()*size);
	}
	
	public Group setUpLevel(Group root, ImageView myPlayer, int screenSize){
    		size = screenSize;
    		bossPizza.setY(size/4);
    		bossPizza.setX(size/(4*2));
    	
    		myPlayer.setX(size);
    		myPlayer.setY(size);
    	
    		root.getChildren().add(bossPizza);
    		root.getChildren().add(myPlayer);
    	
    		return root;
	}
    
	public void hitBoss(){
    		bossLives++;
    	}
    
    	public boolean isDead(){
    		return bossLives > maxLives;
    	}
}

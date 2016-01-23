package game_vc54;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pizza {
	private ImageView myPizza;
	private double xCoordinate;
	private double yCoordinate;
	
	public Pizza(){
		Image pizzaPic = new Image(getClass().getClassLoader().getResourceAsStream("pizza.png"));
		myPizza = new ImageView(pizzaPic);
		xCoordinate = 1000;
		yCoordinate = 1000;
	}
	
	public ImageView getImage(){
		return myPizza;
	}
	
	public double xLocation(){
		return xCoordinate;
	}
	
	public double yLocation(){
		return yCoordinate;
	}
	
}

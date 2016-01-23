package game_vc54;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class WannaPizzaMe {
    public static final int KEY_INPUT_SPEED = 10;
    private static final int BOX_SPEED = 400;
	private static final int MOVEMENT_PER_KEY = 1;
	private static final int BOX_LIFESPAN = 30;
	private static final int NUM_PIZZAS = 15;
	private static final int MAX_LIVES = 5;
	private static final int IMMUNITY_TIME = 2500;
	private static final long MAX_TIME = 50000;
	private static final int ROTATE_AMT = 7;
	private static final int PIZZA_SPEED = 200;
	private static final int NUM_MOVES = 60;
	private static final int R_EDGE_BUFFER = 50;
	private static final int L_EDGE_BUFFER = 0;
	private static final int U_EDGE_BUFFER = 25;
	private static final int ONE_HEART = 50;
	private static final int BOSS_LIVES = 10;
	
	private static final String TITLE = "Wanna Pizza Me?";
	
	private Image playerPic;
	private long startTime;   //the time when immunity starts
	private long timeZero;    //the time at the very beginning of the game
	private boolean canShoot = true;
	private boolean canBeHit = true;
	private int myLives;
	private double size;
	private Scene myScene;
	private ImageView myPlayer;
	private ImageView myBox;
	private ImageView lives;
	private boolean isBox = false;
	private KeyCode lastMove = KeyCode.UP;
	private KeyCode boxDirection = KeyCode.UP;
	private ArrayList<ImageView> myPizzas = new ArrayList<ImageView>();
	private ArrayList<KeyCode> directions = new ArrayList<KeyCode>();
	private KeyCode[] oldDirections = new KeyCode[NUM_PIZZAS];
	
	private boolean isThereBossPizza = false;
	private boolean boxReturn = false;
	private int totalTime = 0;
	private int killed = 0;
	private boolean toBoss = false;
	private int moveCounter = 0;
	private double myLivePos = 0.0;
	private ImageView bossPizza;
	private ImageView startImage;
	private int numBossLives = 0;
	
	private Group root;
	
	public WannaPizzaMe(){
		directions.add(KeyCode.UP);
		directions.add(KeyCode.DOWN);
		directions.add(KeyCode.LEFT);
		directions.add(KeyCode.RIGHT);
	}
	
	public String getTitle(){
		return TITLE; 
	}
	
	public Scene makeScene1(int width, int length){ //Set up for the first scenes
		
	    root = new Group();
	    
		Image backPic = new Image(getClass().getClassLoader().getResourceAsStream("background.png"));
	    ImageView background = new ImageView(backPic);  
		root.getChildren().add(background);
	    Image heartPic = new Image(getClass().getClassLoader().getResourceAsStream("hearts.png"));
	    lives = new ImageView(heartPic);  
	    
		timeZero = System.currentTimeMillis();
		size = width;
	    myScene = new Scene(root, width, length, Color.GREY);
	    
	    playerPic = new Image(getClass().getClassLoader().getResourceAsStream("spongebob1.png"));
	    myPlayer = new ImageView(playerPic);  
	    myLives = MAX_LIVES;
	    myPlayer.setX(width / 2 - myPlayer.getBoundsInLocal().getWidth() / 2);
        myPlayer.setY(length / 2  - myPlayer.getBoundsInLocal().getHeight() / 2);
        
        createPizzas();
        
        root.getChildren().add(myPlayer);
	    root.getChildren().add(lives);
		
	    Image startPic = new Image(getClass().getClassLoader().getResourceAsStream("start.png"));
	    startImage = new ImageView(startPic);  
	    root.getChildren().add(startImage);
	    
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return myScene;
	}
	
	private void createPizzas(){
        for(int i = 0; i < NUM_PIZZAS;i++){
        	Image pizzaPic = new Image(getClass().getClassLoader().getResourceAsStream("pizza.png"));
    		ImageView myPizza = new ImageView(pizzaPic);
    		myPizza.setX(Math.random()*size);
    		myPizza.setY(Math.random()*size);
        	myPizzas.add(myPizza);
        	root.getChildren().add(myPizza);
        }
	}
	public void step(double elapsedTime){
		boxMove(elapsedTime);
		pizzaMove(elapsedTime);
		
		if(isThereBossPizza){
			bossGame();
		}
	}
	
	private void pizzaDelete(int i){
		myPizzas.get(i).setX(-1*size);
		root.getChildren().remove(myPizzas.get(i));
		killed++;
		deleteBox();
		myPizzas.remove(i);
		if(killed >= NUM_PIZZAS){
			killed = 0;
			nextLevel();
		}
	}
	
	public void boxMove(double elapsedTime){
        if(isBox){
        	for(int i = 0; i < myPizzas.size();i++){ 
        		if(isHit(myPizzas.get(i), myBox)){
        			pizzaDelete(i);
        		}
        	}
            if(!boxReturn){
                totalTime +=1;
            	if(totalTime >= BOX_LIFESPAN ){
            		boxReturn = true;
            	}
            	 move(myBox, BOX_SPEED, boxDirection, elapsedTime, true, false);
            }
            else{  
            	deleteBox();
            }
        }
	}
	
	private void pizzaMove(double elaspedTime){
	    KeyCode direction;
	    if(moveCounter == 0){
			for(int i = 0; i < myPizzas.size();i++){
				int whichWay = (int)(Math.random()*(directions.size()-1));
				direction = directions.get(whichWay);
				oldDirections[i] = direction;
			}
			moveCounter++;
	    }
	    else if(moveCounter == NUM_MOVES){
	    	moveCounter = 0;
	    }
	    else{
	    	moveCounter++;
	    }
	    for(int j = 0; j< myPizzas.size();j++){
	    	direction = oldDirections[j];
	    	if(canMove(myPizzas.get(j),direction)){
				move(myPizzas.get(j), PIZZA_SPEED, direction, elaspedTime, false, false);
				ifHit();
			}
	    	else if(direction.equals(KeyCode.LEFT)){
	    		myPizzas.get(j).setX(size);
	    	}
	    	else{
	    		move(myPizzas.get(j), PIZZA_SPEED, KeyCode.LEFT, elaspedTime, false, false);
	    	}
		}
	}
	
	private boolean canMove(ImageView obj, KeyCode dir){ 
		if(dir.equals(KeyCode.UP) && obj.getY()< U_EDGE_BUFFER){
			return false;
		}
		if(dir.equals(KeyCode.DOWN) && obj.getY()> size-R_EDGE_BUFFER){
			return false;
		}
		if(dir.equals(KeyCode.LEFT)&& obj.getX() < L_EDGE_BUFFER){
			return false;
		}
		if(dir.equals(KeyCode.RIGHT)&& obj.getX()> size-R_EDGE_BUFFER){
			return false;
		}
		return true;
	}
	
	private void move(ImageView obj, int speed, KeyCode direction,double elapsedTime, boolean rotate, boolean save){ 
		switch(direction){
			case UP:
				obj.setY(obj.getY() - speed * elapsedTime);
				saveLastMove(direction, save);
				break;
			case DOWN:
				obj.setY(obj.getY() + speed * elapsedTime);
				saveLastMove(direction, save);
				break;
			case LEFT:
				obj.setX(obj.getX() - speed * elapsedTime);
				saveLastMove(direction, save);
				break;
			case RIGHT:
				obj.setX(obj.getX() + speed * elapsedTime);
				saveLastMove(direction, save);
				break;
			default:
		}
		if(rotate){
			obj.setRotate(obj.getRotate() - ROTATE_AMT);
		}
	}
	
	private void saveLastMove(KeyCode direction, boolean save){
		if(save){
			lastMove = direction;
		}
		
	}

	private boolean isHit(ImageView a, ImageView b){
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
	}
	
	private void ifHit(){
    	long currentTime = System.currentTimeMillis();
		if(currentTime - timeZero > MAX_TIME){
			gameOver();
		}
		
		if(canBeHit){
			for(int i = 0; i < myPizzas.size();i++){    //this part is what happens when you get hit -> make new method?
				if(isHit(myPizzas.get(i), myPlayer)){
					minusLife();
				}
			}
			if(isThereBossPizza && isHit(myPlayer,bossPizza)){
				minusLife();
			}
		}
    	if(!canBeHit &&  currentTime - startTime > IMMUNITY_TIME){
    		myPlayer.setImage(playerPic);
    		canShoot = true;
    		canBeHit = true;
    	}
	}
	
	private void minusLife(){
		myLives--;
		lives.setX(lives.getX()-ONE_HEART);
		canShoot = false;
		immunity();
		if(myLives == 0){
			gameOver();
		}
	}
	
	//arrow keys
    private void handleKeyInput (KeyCode code) {
    	ifHit();
    	move(myPlayer, KEY_INPUT_SPEED, code, 1.0, false, true);
       switch (code){
            case X:
            	if(!canShoot){
            		break;
            	}
            	isBox = true;
            	Image pizzaBox = new Image(getClass().getClassLoader().getResourceAsStream("pizzabox.png"));
            	myBox = new ImageView(pizzaBox);
            	newBox(lastMove);
            	root.getChildren().add(myBox);
            	canShoot = false;
            	break;
            case P:
            	immunity();
            	break;
            case O:
            	restoreLives();
            	break;
            case N:
            	gameOver();
            	break;
            case L:
            	nextLevel();
            	break;
            case SPACE:
            	if(toBoss){
            		bossLevel();
            	}
            	break;
            case Y:
            	if(root.getChildren().remove(startImage)){
            		restoreLives();
            	}
            	break;
            default:
                // do nothing
        }
    }
    
    private void restoreLives(){
    	myLives = MAX_LIVES;
    	lives.setX(0);
    }
    
    private void newBox(KeyCode direction){
    	totalTime = 0;
    	boxReturn = false;
    	boxDirection = direction;
    	 switch (direction) {
         case RIGHT:
          	myBox.setX(myPlayer.getX());
          	myBox.setY(myPlayer.getY() + KEY_INPUT_SPEED*MOVEMENT_PER_KEY);
             break;
         case LEFT:
          	myBox.setX(myPlayer.getX());
          	myBox.setY(myPlayer.getY() + KEY_INPUT_SPEED*MOVEMENT_PER_KEY);
            break;
         case UP:
         	myBox.setX(myPlayer.getX() + KEY_INPUT_SPEED*MOVEMENT_PER_KEY);
         	myBox.setY(myPlayer.getY());
            break;
         case DOWN:
         	myBox.setX(myPlayer.getX() + KEY_INPUT_SPEED*MOVEMENT_PER_KEY);
         	myBox.setY(myPlayer.getY());
            break;
         default:
    	 }
    }
    
    private void deleteBox(){
    	isBox = false;
    	root.getChildren().remove(myBox);
    	canShoot = true;
    	totalTime = 0;
    	boxReturn = false;
    }
  
    private void immunity(){
    	Image immunityPic = new Image(getClass().getClassLoader().getResourceAsStream("angrySponge.png"));
    	myPlayer.setImage(immunityPic);
    	canBeHit = false;
    	startTime = System.currentTimeMillis();
    }
    
    private void gameOver(){
    	root.getChildren().clear();       //can change these 3 lines to a reset/clear method
    	Rectangle blackBox = new Rectangle(0,0, size,size);
    	blackBox.setFill(Color.BLACK);
    	Image endPic = new Image(getClass().getClassLoader().getResourceAsStream("gameover.png"));
    	ImageView theEnd = new ImageView(endPic);
		theEnd.setX(0);
		theEnd.setY(size/4);
    	root.getChildren().add(blackBox);
    	root.getChildren().add(theEnd);
    }
    
    private void nextLevel(){//fix this so i dont clear all the notes
    	toBoss = true;
    	myLivePos = lives.getX();
    	root.getChildren().clear();
    	Rectangle blackBox = new Rectangle(0,0, size,size);
    	blackBox.setFill(Color.BLACK);
    	root.getChildren().add(blackBox);
    	Image nextPic = new Image(getClass().getClassLoader().getResourceAsStream("nextLevel.png"));
    	ImageView nextLvl = new ImageView(nextPic);
		nextLvl.setX(0);
		nextLvl.setY(size/4);
    	root.getChildren().add(nextLvl);
    }
    
    private void bossLevel(){
    	for(int i = myPizzas.size()-1; i >= 0; i--){
    		pizzaDelete(i);
    	}
        root.getChildren().clear();
    	myPlayer.setX(size - R_EDGE_BUFFER - U_EDGE_BUFFER);
    	myPlayer.setY(size - R_EDGE_BUFFER - U_EDGE_BUFFER);
    	root.getChildren().add(myPlayer);
    	
    	
    	//set Up
        Image bossPic = new Image(getClass().getClassLoader().getResourceAsStream("boss.png"));
    	bossPizza = new ImageView(bossPic);
    	bossPizza.setY(-1*U_EDGE_BUFFER);
    	root.getChildren().add(bossPizza);
    	isThereBossPizza = true;
        lives.setX(myLivePos);
        root.getChildren().add(lives);
    	
    }
    
    private void bossGame(){
    	ifHit();
    	if(isBox && isHit(bossPizza,myBox)){
    		deleteBox();
    		numBossLives++;
    		if(numBossLives > BOSS_LIVES){
    			root.getChildren().remove(bossPizza);
            	Image winPic = new Image(getClass().getClassLoader().getResourceAsStream("win.png"));
            	ImageView youWin = new ImageView(winPic);
            	root.getChildren().clear();
            	root.getChildren().add(youWin);
    		}
    		if(numBossLives == BOSS_LIVES/2){
    			createPizzas();
    		}
    	}
    }
    
}

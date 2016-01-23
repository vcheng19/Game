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
    public static final int KEY_INPUT_SPEED = 20;
    private static final int BOX_SPEED = 400;
	private static final int MOVEMENT_PER_KEY = 2;
	private static final int BOX_LIFESPAN = 30;
	private static final int NUM_PIZZAS = 10;
	private static final int MAX_LIVES = 5;
	private static final int IMMUNITY_TIME = 2500;
	private static final long MAX_TIME = 50000;
	private static final int ROTATE_AMT = 7;
	private static final int PIZZA_SPEED = 200;
	private static final int NUM_MOVES = 30;
	private static final int R_EDGE_BUFFER = 50;
	private static final int L_EDGE_BUFFER = 0;
	private static final int U_EDGE_BUFFER = 25;
	private static final int ONE_HEART = 50;
	private static final int BOSS_LIVES = 10;
	private static final int BOSS_COUNT = 50;
	
	private static final String TITLE = "Wanna Pizza Me?";

	private Image playerPic;
	private long startTime;  
	private long timeZero;
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
	private boolean isOver = true;
	private boolean boxReturn = false;
	private int totalTime = 0;
	private int killed = 0;
	private boolean toBoss = false;
	private int moveCounter = 0;
	private double myLivePos = 0.0;
	private ImageView bossPizza;
	private ImageView startImage;
	private int numBossLives = 0;
	private int bossTime = 0;
	private boolean isGameStarted;
	
	private Group root;
	
	public String getTitle(){
		return TITLE; 
	}
	
	public Scene makeScene(int width){
	    root = new Group();
	    myScene = new Scene(root, width,width, Color.GREY);
		size = width;
		timeZero = System.currentTimeMillis();
		
		sceneSetUp();
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return myScene;
	}
	
	private void sceneSetUp(){
		isGameStarted = false;
		directions.add(KeyCode.UP);
		directions.add(KeyCode.DOWN);
		directions.add(KeyCode.LEFT);
		directions.add(KeyCode.RIGHT);
		
		ImageView background = createImage("background.png"); 
		root.getChildren().add(background);
		
		createPizzas();   
	    
	    playerPic = new Image(getClass().getClassLoader().getResourceAsStream("spongebob1.png"));
	    myPlayer = new ImageView(playerPic);  
	    myPlayer.setX(size / 2 - myPlayer.getBoundsInLocal().getWidth() / 2);
	    myPlayer.setY(size / 2  - myPlayer.getBoundsInLocal().getHeight() / 2);
	    root.getChildren().add(myPlayer);
		    
	    myLives = MAX_LIVES;
	    lives = createImage("hearts.png");
	    root.getChildren().add(lives);
		
	    startImage = createImage("start.png"); 
	    root.getChildren().add(startImage);
	}
	
	private void createPizzas(){  
        for(int i = 0; i < NUM_PIZZAS;i++){
    		ImageView myPizza = createImage("pizza.png");
    		setCoordinates(myPizza, size);
        	myPizzas.add(myPizza);
        	root.getChildren().add(myPizza);
        }
	}
	
	private void setCoordinates(ImageView obj, double size){
		obj.setX(Math.random()*size);
		obj.setY(Math.random()*size);
	}
	
	public void step(double elapsedTime){
		if(isGameStarted){
			boxMove(elapsedTime);
			pizzaMove(elapsedTime);
			
			if(isThereBossPizza){
				bossGame();
			}
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
	
	private void chooseDirection(){
		if(moveCounter == 0){
			for(int i = 0; i < myPizzas.size();i++){
				int whichWay = (int)(Math.random()*(directions.size()-1));
				KeyCode direction = directions.get(whichWay);
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
	}
	
	private void pizzaMove(double elaspedTime){
	    KeyCode direction;
	    chooseDirection();
	    for(int j = 0; j< myPizzas.size();j++){
	    	direction = oldDirections[j];
	    	if(canMove(myPizzas.get(j),direction)){
				move(myPizzas.get(j), PIZZA_SPEED, direction, elaspedTime, false, false);
				ifHit();
			}
	    	else{
	    		if(direction.equals(KeyCode.LEFT)){
	    			myPizzas.get(j).setX(size);
	    		}
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
				break;
			case DOWN:
				obj.setY(obj.getY() + speed * elapsedTime);
				break;
			case LEFT:
				obj.setX(obj.getX() - speed * elapsedTime);
				break;
			case RIGHT:
				obj.setX(obj.getX() + speed * elapsedTime);
				break;
			default:
		}
		if(save){
			lastMove = direction;
		}
		if(rotate){
			obj.setRotate(obj.getRotate() - ROTATE_AMT);
		}
	}

	private boolean isHit(ImageView a, ImageView b){
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
	}
	
	private void ifHit(){
		checkTime();
		if(canBeHit){
			for(int i = 0; i < myPizzas.size();i++){
				if(isHit(myPizzas.get(i), myPlayer)){
					minusLife();
				}
			}
			if(isThereBossPizza && isHit(myPlayer,bossPizza)){
				minusLife();
			}
		}
	}
	
	private void checkTime(){
    	long currentTime = System.currentTimeMillis();
    	if(!isThereBossPizza & currentTime - timeZero > MAX_TIME){
			gameOver();
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
		deleteBox();
		canShoot = false;
		immunity();
		if(myLives == 0){
			gameOver();
		}
	}
	
    private void handleKeyInput (KeyCode code) {
    	ifHit();
    	if(code.equals(KeyCode.UP) || code.equals(KeyCode.DOWN) || code.equals(KeyCode.LEFT) || code.equals(KeyCode.RIGHT)){
			move(myPlayer, KEY_INPUT_SPEED, code, 1.0, false, true);
			return;
    	}
       switch (code){
            case X:
            	if(!canShoot){
            		break;
            	}
            	newBox(lastMove);
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
            	isGameStarted = true;
            	if(root.getChildren().remove(startImage)){
            		restoreLives();
            		timeZero = System.currentTimeMillis();
            	}
            	break;
            default:
        }
    }
    
    private void restoreLives(){
    	myLives = MAX_LIVES;
    	lives.setX(0);
    }
    
    private void newBox(KeyCode direction){
    	canShoot = false;
    	isBox = true;
    	myBox = createImage("pizzabox.png");
    	
    	totalTime = 0;
    	boxReturn = false;
    	boxDirection = direction;
    	
        myBox.setX(myPlayer.getX());
       	myBox.setY(myPlayer.getY() + KEY_INPUT_SPEED*MOVEMENT_PER_KEY);
    	root.getChildren().add(myBox);
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
    	if(isOver){
    		reset("gameover.png");   
    	}
    }
    
    private void nextLevel(){
    	reset("nextlevel.png");
    	toBoss = true;
    	myLivePos = lives.getX();

    }
    
    private void reset(String filename){
    	root.getChildren().clear();
    	Rectangle blackBox = new Rectangle(0,0, size,size);
    	blackBox.setFill(Color.BLACK);
    	root.getChildren().add(blackBox);
    	
    	ImageView theImage = createImage(filename);
		
    	theImage.setX(0);
		theImage.setY(size/4);
    	root.getChildren().add(theImage);
    }
    
    private ImageView createImage(String filename){
    	Image thePic = new Image(getClass().getClassLoader().getResourceAsStream(filename));
    	ImageView theImage = new ImageView(thePic);
    	return theImage;
    }
    
    private void bossLevel(){
    	toBoss = false;
    	deleteAllPizza();
        root.getChildren().clear();
        myPlayer.setX(size - R_EDGE_BUFFER - U_EDGE_BUFFER);
    	myPlayer.setY(size - R_EDGE_BUFFER - U_EDGE_BUFFER);
    	root.getChildren().add(myPlayer);
    	
    	bossPizza = createImage("boss.png");
    	bossPizza.setY(size/4);
    	bossPizza.setX(size/(4*2));
    	root.getChildren().add(bossPizza);
    	
    	isThereBossPizza = true;
        lives.setX(myLivePos);
        root.getChildren().add(lives);
    }
    
    private void deleteAllPizza(){
    	for(int i = myPizzas.size()-1; i >= 0; i--){
    		pizzaDelete(i);
    	}
    }
    
    private void bossGame(){
    	ifHit();
    	bossMove();
    	if(isBox && isHit(bossPizza,myBox)){
    		deleteBox();
    		numBossLives++;
    		if(numBossLives > BOSS_LIVES){
    			root.getChildren().remove(bossPizza);
            	ImageView youWin = createImage("win.png");
            	root.getChildren().clear();
            	isOver = false;
            	root.getChildren().add(youWin);
    		}
    		if(numBossLives == BOSS_LIVES/2){
    			createPizzas();
    		}
    	}
    }
    
    private void bossMove(){
    	bossTime++;
    	if(bossTime > BOSS_COUNT){
    		setCoordinates(bossPizza, size/2);
    		bossTime = 0;
    	}
    }
    
}

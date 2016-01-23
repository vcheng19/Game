package game_vc54;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{

	private final static int SIZE = 750;
	public static final int FRAMES_PER_SECOND = 60;
	private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	
	private WannaPizzaMe myGame;
	
	public void start(Stage s){
		myGame = new WannaPizzaMe();
		s.setTitle(myGame.getTitle());
		
		Scene scene = myGame.makeScene(SIZE);
		s.setScene(scene);
        s.show();     
        
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> myGame.step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);;
        animation.play();
	}
	
	public static void main (String[] args) {
        launch(args);
    }
}

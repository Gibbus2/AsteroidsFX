package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class);
    }
    @Override
    public void start(Stage stage) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Game.class);

        Game game = context.getBean(Game.class);
        game.start(stage);
    }
}

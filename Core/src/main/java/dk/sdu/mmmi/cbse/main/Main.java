package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.map.IMapSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private long lastFrame;
    private final Text score = new Text(10, 20, "Score: 0");
    private AnimationTimer animationTimer;
    private boolean paused, gameOver;
    private final Text textPaused = new Text(0, 0, "Paused");
    private final Text textGameOver = new Text(0, 0, "GAME OVER");

    private List<IEntityProcessingService> iEntityProcessingServiceList;
    private List<IPostEntityProcessingService> iPostEntityProcessingServiceList;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        //set background
        ServiceLoader.load(IMapSPI.class).stream().findFirst().ifPresent( iMapSPIProvider -> {
            ImageView imageView = iMapSPIProvider.get().getMap();
            imageView.setFitHeight(gameWindow.getHeight());
            imageView.setFitWidth(gameWindow.getWidth());
            gameWindow.getChildren().add(imageView);
        });

        //score text
        score.setFont(new Font(15));
        score.setFill(Color.WHITE);
        gameWindow.getChildren().add(score);

        //paused text
        textPaused.setFont(new Font(30));
        textPaused.setFill(Color.WHITE);
        textPaused.setX(((double) gameData.getDisplayWidth()/2) - (textPaused.getLayoutBounds().getWidth()/2));
        textPaused.setY(((double) gameData.getDisplayHeight() /2) - (textPaused.getLayoutBounds().getHeight()/2));
        gameWindow.getChildren().add(textPaused);

        //game over text
        textGameOver.setFont(new Font(40));
        textGameOver.setFill(Color.WHITE);
        textGameOver.setX(((double) gameData.getDisplayWidth()/2) - (textGameOver.getLayoutBounds().getWidth()/2));
        textGameOver.setY(((double) gameData.getDisplayHeight() /2) - (textGameOver.getLayoutBounds().getHeight()/2));
        textGameOver.setVisible(false);
        gameWindow.getChildren().add(textGameOver);


        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
            if (event.getCode().equals(KeyCode.ESCAPE)){
                if(!gameOver) {
                    if (!paused) {
                        pause();
                    } else {
                        unpause();
                    }
                }
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }

        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, world);
        }

        // get all processing services and cache them
        iEntityProcessingServiceList = getEntityProcessingServices();
        iPostEntityProcessingServiceList = getPostEntityProcessingServices();

        lastFrame = System.nanoTime();
        setAnimationTimer();
        pause();

        gameOver = false;

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    private void setAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameData.setDelta(now - lastFrame);
                gameData.setFrame(now);

                update();
                draw();
                gameData.getKeys().update();

                lastFrame = now;

                if(!playerAlive()){
                    gameOver();
                }
            }

        };

    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : iEntityProcessingServiceList) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : iPostEntityProcessingServiceList) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {        
        for (Entity polygonEntity : polygons.keySet()) {
            if(!world.getEntities().contains(polygonEntity)){   
                Polygon removedPolygon = polygons.get(polygonEntity);               
                polygons.remove(polygonEntity);                      
                gameWindow.getChildren().remove(removedPolygon);
            }
        }
                
        for (Entity entity : world.getEntities()) {                      
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygon.setFill(Color.BLACK);
                polygon.setStroke(Color.WHITE);
                polygon.setStrokeWidth(0.6);
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }

    }

    private boolean playerAlive(){
        for (Entity entity : world.getEntities()){
            if(entity.getType() == EntityType.PLAYER){
                return true;
            }
        }
        return false;
    }

    private void pause(){
        animationTimer.stop();
        paused = true;

        textPaused.setVisible(true);
    }

    private void unpause(){
        textPaused.setVisible(false);

        lastFrame = System.nanoTime();
        animationTimer.start();
        paused = false;
    }

    private void gameOver(){
        gameOver = true;

        //stop timer
        animationTimer.stop();

        //stop all plugins
        for (IGamePluginService gamePluginService : getPluginServices()){
            gamePluginService.stop(gameData, world);
        }

        //Display text
        textGameOver.setVisible(true);

    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private List<IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}

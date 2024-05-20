package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.map.IMapSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.Collection;
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
    private long asteroidsSpawnDelta;
    private AnimationTimer animationTimer;
    private boolean paused;
    private final Text textPaused = new Text(0, 0, "Paused");

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
                if(!paused) {
                    pause();
                }else {
                    unpause();
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

        lastFrame = System.nanoTime();
        asteroidsSpawnDelta = 0;
        setAnimationTimer();
        pause();

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

                spawn();
                update();
                draw();
                gameData.getKeys().update();

                lastFrame = now;
            }

        };

    }

    private void spawn(){
        asteroidsSpawnDelta += gameData.getDelta();
        int spawnDeltaSec = (int) (asteroidsSpawnDelta/1_000_000_000);

        if(spawnDeltaSec >= 4){
            if(world.getAsteroids() < 3) {
                System.out.println("Spawning asteroids");
                for (IGamePluginService iGamePlugin : getPluginServices()) {
                    if (iGamePlugin.type() == EntityType.ASTEROID) {
                        iGamePlugin.start(gameData, world);
                    }
                }
            }
            asteroidsSpawnDelta = 0;
        }
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
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

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}

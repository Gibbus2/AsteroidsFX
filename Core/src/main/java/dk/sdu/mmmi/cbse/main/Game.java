package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.map.IMapSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
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

    private final List<IEntityProcessingService> iEntityProcessingServiceList;
    private final List<IPostEntityProcessingService> iPostEntityProcessingServiceList;
    private final List<IGamePluginService> iGamePluginServicesList;
    private final List<IMapSPI> iMapSPIList;

    Game(List<IEntityProcessingService> iEntityProcessingServiceList, List<IPostEntityProcessingService> iPostEntityProcessingServiceList, List<IGamePluginService> iGamePluginServicesList, List<IMapSPI> iMapSPIList) {
        this.iEntityProcessingServiceList = iEntityProcessingServiceList;
        this.iPostEntityProcessingServiceList = iPostEntityProcessingServiceList;
        this.iGamePluginServicesList = iGamePluginServicesList;
        this.iMapSPIList = iMapSPIList;
    }

    public void start(Stage window) {
        System.out.println("Start Game");

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());

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

        //set background
//        if(!iMapSPIList.isEmpty()){
//            ImageView imageView = iMapSPIList.getFirst().getMap();
//            imageView.setFitHeight(gameWindow.getHeight());
//            imageView.setFitWidth(gameWindow.getWidth());
//            gameWindow.getChildren().add(imageView);
//        }

        //score text
        score.setFont(new Font(15));
        score.setFill(Color.BLACK);
        gameWindow.getChildren().add(score);

        //paused text
        textPaused.setFont(new Font(30));
        textPaused.setFill(Color.BLACK);
        textPaused.setX(((double) gameData.getDisplayWidth()/2) - (textPaused.getLayoutBounds().getWidth()/2));
        textPaused.setY(((double) gameData.getDisplayHeight() /2) - (textPaused.getLayoutBounds().getHeight()/2));
        gameWindow.getChildren().add(textPaused);

        //game over text
        textGameOver.setFont(new Font(40));
        textGameOver.setFill(Color.BLACK);
        textGameOver.setX(((double) gameData.getDisplayWidth()/2) - (textGameOver.getLayoutBounds().getWidth()/2));
        textGameOver.setY(((double) gameData.getDisplayHeight() /2) - (textGameOver.getLayoutBounds().getHeight()/2));
        textGameOver.setVisible(false);
        gameWindow.getChildren().add(textGameOver);

        // load all game plugins
        for (IGamePluginService iGamePlugin : iGamePluginServicesList) {
            iGamePlugin.start(gameData, world);
        }


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
                updateUI();
                gameData.getKeys().update();

                if(!playerAlive()){
                    gameOver();
                }

                lastFrame = now;
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
                polygon.setStroke(Color.BLACK);
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

    private void updateUI(){
        this.score.setText("Score: " + gameData.getScore());
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
        for (IGamePluginService gamePluginService : iGamePluginServicesList){
            gamePluginService.stop(gameData, world);
        }

        //Display text
        textGameOver.setVisible(true);

    }



}



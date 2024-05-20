package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
public class PlayerPlugin implements IGamePluginService {


    @Override
    public void start(GameData gameData, World world) {
        Entity player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity playerShip = new Player(0.6);
        playerShip.setPolygonCoordinates(-5,-5,10,0,-5,5);
        playerShip.setX((double) gameData.getDisplayHeight() /2);
        playerShip.setY((double) gameData.getDisplayWidth() /2);
        playerShip.setRadius(8);
        playerShip.setRotationSpeed(400);
        playerShip.setForwardSpeed(100);

        playerShip.setType(EntityType.PLAYER);
        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity entity : world.getEntities(Player.class)){
            world.removeEntity(entity);
        }
    }

    @Override
    public EntityType type() {
        return EntityType.PLAYER;
    }

}

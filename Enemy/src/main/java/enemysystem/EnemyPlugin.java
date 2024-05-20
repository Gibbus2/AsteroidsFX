package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.PluginType;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
public class EnemyPlugin implements IGamePluginService {

    private Entity ship;
    @Override
    public void start(GameData gameData, World world) {
        ship = createShip(gameData);
        world.addEntity(ship);
    }
    private Entity createShip(GameData gameData){
        Entity ship = new Enemy(2);
        ship.setPolygonCoordinates(-5,-5,10,0,-5,5);
        ship.setX((double) gameData.getDisplayHeight() /2 - 50);
        ship.setY(50);
        ship.setRadius(8);
        ship.setRotation(90); // spawn looking down
        ship.setRotationSpeed(300);
        ship.setForwardSpeed(100);
        return ship;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(ship);
    }

    @Override
    public PluginType type() {
        return PluginType.ENEMY;
    }
}

package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.PluginType;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {

    private Entity ship;
    @Override
    public void start(GameData gameData, World world) {
        ship = createShip(gameData);
        world.addEntity(ship);
    }
    private Entity createShip(GameData gameData){
        Entity ship = new Enemy();
        ship.setPolygonCoordinates(-5,-5,10,0,-5,5);
        ship.setX(gameData.getDisplayHeight()/2 - 50);
        ship.setY(50);
        ship.setRadius(8);
        ship.setRotation(90); // spawn looking down
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

package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

public interface IEntityProcessingService {

    /**
     *
     *
     *
     * @param gameData
     * @param world
     * @throws
     */
    void process(GameData gameData, World world);

    void collision(GameData gameData, World world, Entity entity1, Entity entity2);
}


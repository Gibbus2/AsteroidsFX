package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

public class PlayerPluginTest {
    @Mock
    private GameData gameData;
    @Mock
    private World world;
    @Mock
    private Player player;

    @BeforeEach
    public void init(){
        System.out.println("BEFORE");
        gameData = mock(GameData.class);
        world = mock(World.class);
        player = mock(Player.class);

        when(world.getEntities(Player.class)).thenReturn(List.of(player));
    }

    @Test
    public void testStart() {
        PlayerPlugin playerPlugin = new PlayerPlugin();
        when(gameData.getDisplayHeight()).thenReturn(800);
        when(gameData.getDisplayWidth()).thenReturn(800);

        // Call the start method
        playerPlugin.start(gameData, world);

        verify(world).addEntity(Mockito.any(Entity.class));
    }

    @Test
    public void testStop() {
        PlayerPlugin playerPlugin = new PlayerPlugin();

        Entity player1 = new Player(0.6);
        world.addEntity(player1);

        playerPlugin.stop(gameData, world);

        verify(world, times(1)).removeEntity(Mockito.any(Entity.class));
    }
}

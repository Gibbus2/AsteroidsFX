package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

public class PlayerControlSystemTest {
    @Mock
    private GameData gameData;
    @Mock
    private World world;
    @Mock
    private Player player;
    @Mock
    private GameKeys gameKeys;

    @BeforeEach
    public void init(){
        gameData = mock(GameData.class);
        world = mock(World.class);
        player = mock(Player.class);
        gameKeys = mock(GameKeys.class);

        when(gameData.getKeys()).thenReturn(gameKeys);
        when(gameData.getDelta()).thenReturn(1000L);
        when(gameData.getFrame()).thenReturn(100000L);
        when(world.getEntities(Player.class)).thenReturn(List.of(player));
        when(player.getRotation()).thenReturn(1.1);
    }

    @Test
    public void testProcessLeft(){
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        when(gameData.getKeys().isDown(GameKeys.LEFT)).thenReturn(true);

        playerControlSystem.process(gameData, world);

        verify(player, times(1)).rotate(1000L, false);
        verify(player, times(1)).setHeading(1.1);
        verify(player, times(2)).getY();
        verify(player, times(2)).getX();
    }

    @Test
    public void testProcessRight(){
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        when(gameData.getKeys().isDown(GameKeys.RIGHT)).thenReturn(true);

        playerControlSystem.process(gameData, world);

        verify(player, times(1)).rotate(1000L, true);
        verify(player, times(1)).setHeading(1.1);
        verify(player, times(2)).getY();
        verify(player, times(2)).getX();
    }

    @Test
    public void testProcessForward(){
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        when(gameData.getKeys().isDown(GameKeys.UP)).thenReturn(true);

        playerControlSystem.process(gameData, world);

        verify(player, times(1)).forward(1000L);
        verify(player, times(2)).getY();
        verify(player, times(2)).getX();
    }

    @Test
    public void testProcessOutOfBounds1(){
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        when(player.getY()).thenReturn(-1.);
        when(player.getX()).thenReturn(-1.);

        playerControlSystem.process(gameData, world);

        verify(player, times(2)).getY();
        verify(player, times(2)).getX();
        verify(player, times(1)).setX(1);
        verify(player, times(1)).setY(1);
    }

    @Test
    public void testProcessOutOfBounds2(){
        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        when(player.getY()).thenReturn(200.);
        when(player.getX()).thenReturn(200.);
        when(gameData.getDisplayWidth()).thenReturn(100);
        when(gameData.getDisplayHeight()).thenReturn(100);

        playerControlSystem.process(gameData, world);

        verify(player, times(2)).getY();
        verify(player, times(2)).getX();
        verify(player, times(1)).setX(99);
        verify(player, times(1)).setY(99);
    }
}
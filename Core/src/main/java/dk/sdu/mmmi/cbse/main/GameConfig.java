package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.map.IMapSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

@Configuration
public class GameConfig {

    public GameConfig(){

    }
    @Bean
    public Game game(){
        return new Game(getEntityProcessingServices(), getPostEntityProcessingServices(), getPluginServices(), getMapSPIs());
    }

    @Bean
    public List<IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    @Bean
    public List<IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    @Bean
    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    @Bean
    public List<IMapSPI> getMapSPIs(){
        return ServiceLoader.load(IMapSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}

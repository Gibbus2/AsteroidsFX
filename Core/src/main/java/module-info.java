module Core {
    requires Common;
    requires javafx.graphics;
    requires CommonMap;
    opens dk.sdu.mmmi.cbse.main to javafx.graphics;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.map.IMapSPI;
}



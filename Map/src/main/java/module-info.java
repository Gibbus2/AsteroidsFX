import dk.sdu.mmmi.cbse.common.map.IMapSPI;

module Map {
    requires CommonMap;
    requires javafx.graphics;
    provides IMapSPI with dk.sdu.mmmi.cbse.map.Map;
}
package dk.sdu.mmmi.cbse.map;

import dk.sdu.mmmi.cbse.common.map.IMapSPI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class Map implements IMapSPI {

    @Override
    public ImageView getMap() {
        System.out.println("Returning map");
        InputStream is = Map.class.getResourceAsStream("/map.jpg");
        if(is != null) {
            Image image = new Image(is);
            return new ImageView(image);
        }else {
            return null;
        }
    }
}

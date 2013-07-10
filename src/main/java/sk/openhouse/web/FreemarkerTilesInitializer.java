package sk.openhouse.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;

/**
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class FreemarkerTilesInitializer extends AbstractTilesInitializer {

    /* definitions */
    private List<String> definitions = new ArrayList<String>();

    /* paths to properties and toolbox */
    private String velocityProperties;
    private String velocityToolbox;

    /** @param definitions - tiles.xml, if not set default is in WEB-INF */
    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    /** @param velocityToolbox path to the velocity properties file */
    public void setVeolocityProperties(String velocityProperties) {
        this.velocityProperties = velocityProperties;
    }

    /** @param velocityToolbox path to the velocity toolbox xml file */
    public void setVeolocityToolbox(String velocityToolbox) {
        this.velocityToolbox = velocityToolbox;
    }

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {

    	FreeMarkerTilesContainerFactory containerFactory = 
                new FreeMarkerTilesContainerFactory();
        containerFactory.setDefinitions(definitions);
        //containerFactory.setVeolocityProperties(velocityProperties);
        //containerFactory.setVeolocityToolbox(velocityToolbox);

        return containerFactory;
    }

}

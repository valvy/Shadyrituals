package nl.hvanderheijden.prutengine;

import nl.hvanderheijden.prutengine.core.math.Vector2;
import nl.hvanderheijden.prutengine.exceptions.InitException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by heikovanderheijden on 08.04.17.
 */
public final class SettingsManager {

    private final static Logger logger = LogManager.getLogger(SettingsManager.class.getName());

    private static SettingsManager instance;

    private final Vector2<Integer> world_size;



    private boolean debugMode = false;

    private  String ip = "";

    private SettingsManager() throws InitException {
        world_size = new Vector2<Integer>(0,0);
        try (final InputStream in = SettingsManager.class.getResourceAsStream("/shadyrituals.properties")){
            Properties properties = new Properties();
            properties.load(in);
            //Load in the secret
            this.ip =  properties.getProperty("ipadress");
            world_size.x = Integer.parseInt(properties.getProperty("worldsizeX"));
            world_size.y = Integer.parseInt(properties.getProperty("worldsizeY"));
            debugMode = Boolean.getBoolean(properties.getProperty("DEBUG"));

        } catch (IOException e) {
            logger.error(e);
            throw new InitException("Could not initialize the settings");
        }
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public String getIp() {
        return ip;
    }

    public Vector2<Integer> getWorld_size() {
        return world_size;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static SettingsManager getInstance() throws InitException {
        if(instance == null){
            instance = new SettingsManager();
        }

        return instance;
    }
}

package com.shb.dev.server.route;

import com.shb.dev.server.asset.ShbAssetResolver;
import org.apache.log4j.Logger;

/**
 * @author Mohammad Rahmati, 5/10/2017 7:43 PM
 */
public class ShbAssetHandler
        extends ShbRouteService{
    protected static ShbAssetResolver shbAssetResolver = null;

    public ShbAssetResolver getShbAssetResolver() {
        if(shbAssetResolver == null) {
            String assetPath =
                    shbServerConfig.getAssetPath();
            if(assetPath == null || assetPath.isEmpty()) {
                logger.error("asset path is null.");
                return null;
            }
            shbAssetResolver = ShbAssetResolver
                    .getInstance(assetPath);
        }
        return shbAssetResolver;
    }
}

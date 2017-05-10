package com.shb.dev.server.route;

import com.shb.dev.server.asset.ShbAsset;
import com.shb.dev.server.asset.ShbAssetResolver;
import com.shb.dev.server.response.ShbResponse;
import com.shb.dev.server.session.ShbSession;

import javax.ws.rs.core.Response;

/**
 * @author Mohammad Rahmati, 5/10/2017 8:46 PM
 */
public class ShbAssetService {
    public static ShbResponse getAsset(
            ShbSession session,
            ShbAssetResolver assetResolver,
            String assetPath) {
        ShbAsset asset = assetResolver
                .resolve(assetPath);
        return new ShbResponse(
                Response.Status.OK,
                asset.getBytes());
    }
}

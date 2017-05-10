package com.shb.dev.sample.rest;

import com.shb.dev.server.asset.ShbAsset;
import com.shb.dev.server.asset.ShbAssetResolver;
import com.shb.dev.server.response.ShbResponse;
import com.shb.dev.server.session.ShbSession;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Mohammad Rahmati, 5/10/2017 8:16 PM
 */
public class AssetService {
    public static ShbResponse getRoot(
            ShbSession session,
            ShbAssetResolver assetResolver) {
        ShbAsset asset =
                assetResolver.resolve("index.html");
        return new ShbResponse(
                Response.Status.OK,
                asset.getBytes(),
                MediaType.TEXT_HTML);
    }

//    public static ShbResponse getAsset(
//            ShbSession session,
//            ShbAsset shbAsset) {
//        return new ShbResponse()shbAsset.getBytes()
//    }
}

package com.shb.dev.sample.rest;

import com.shb.dev.sample.model.OfferModel;
import com.shb.dev.server.response.ShbResponse;
import com.shb.dev.server.session.ShbSession;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Rahmati, 5/13/2017 1:43 PM
 */
public class OfferRest {
    public static ShbResponse allOffer(ShbSession session) {
        List<OfferModel> offers = new ArrayList<>();
        offers.add(new OfferModel(1, "ali", 1000, "no-image-256.png"));
        offers.add(new OfferModel(2, "ali2", 2000, "no-image-256.png"));
        offers.add(new OfferModel(3, "ali3", 3000, "no-image-256.png"));
        offers.add(new OfferModel(4, "ali4", 4000, "no-image-256.png"));

        return new ShbResponse(Status.OK, offers,
                MediaType.APPLICATION_JSON);
    }
}

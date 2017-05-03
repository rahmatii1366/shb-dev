package com.shb.dev.server.route;

import com.shb.dev.server.asset.ShbAsset;
import com.shb.dev.server.asset.ShbAssetResolver;
import com.shb.dev.server.config.ShbConfigReader;
import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbServerConfig;
import com.shb.dev.server.role.ShbRole;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.InputStream;

/**
 * @author Mohammad Rahmati, 5/3/2017 7:28 AM
 */
@Singleton
@Path("/")
public class ShbRouteService {
    @Context
    protected Configuration config;
    protected ShbServerConfig shbServerConfig = null;
    protected ShbRouterConfig shbRouterConfig = null;
    protected ShbRouteResolver routeResolver;
    protected ShbAssetResolver assetResolver;

    @PostConstruct
    public void init()
            throws Exception {
        shbServerConfig = (ShbServerConfig) config
                .getProperty(ShbServerConfig
                        .SHB_SERVER_CONFIG);
        File file = new File((String) shbServerConfig
                .getAssetPath());
        assetResolver = ShbAssetResolver
                .getInstance(file.getPath());
        shbRouterConfig = (ShbRouterConfig) config
                .getProperty(ShbRouterConfig
                        .SHB_ROUTER_CONFIG);
        routeResolver = new ShbRouteResolver(
                shbRouterConfig);
    }

    @GET
    @ShbRole
    @Path("/asset/{path:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response getAsset(
            @PathParam("path") String path,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        ShbAsset resolve = assetResolver.resolve(
                "asset/".concat(path));
        return Response.ok(resolve.getBytes()).build();
    }

    @GET
    @ShbRole
    @Path("/node_modules/{path:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response getNodeModule(
            @PathParam("path") String path,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        ShbAsset resolve = assetResolver.resolve(
                "node_modules/".concat(path));
        return Response.ok(resolve.getBytes()).build();
    }

    @GET
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response getRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        ShbAsset asset = null;
        if(route == null || route.isEmpty()) {
            asset = assetResolver
                    .resolve("asset/index.html");
            return Response.ok(asset.getBytes())
                    .header("Content-Type", "text/html")
                    .build();
        }
        routeResolver.resolveRoute(route, "GET", info);
        return Response.ok("this page is loaded").build();
    }

    @POST
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response postRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        return Response.ok("this page is loaded").build();
    }

    @PUT
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response putRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        return Response.ok("this page is loaded").build();
    }

    @DELETE
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        return Response.ok("this page is loaded").build();
    }

    @OPTIONS
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response optionsRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        return Response.ok("this page is loaded").build();
    }

    @HEAD
    @ShbRole
    @Path("{route:.*}")
    @Produces(MediaType.TEXT_HTML)
    public Response headRoute(
            @PathParam("route") String route,
            @Context HttpHeaders httpHeaders,
            @Context UriInfo info) {
        return Response.ok("this page is loaded").build();
    }
}

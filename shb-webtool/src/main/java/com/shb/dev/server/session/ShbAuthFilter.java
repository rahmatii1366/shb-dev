package com.shb.dev.server.session;

import com.shb.dev.server.role.ShbRole;
import com.shb.dev.server.role.ShbRoleType;
import org.glassfish.jersey.server.internal.routing.UriRoutingContext;
import org.glassfish.jersey.server.model.ResourceMethodInvoker;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

/**
 * @author Mohammad Rahmati, 5/3/2017 10:15 AM
 */
@Deprecated
public class ShbAuthFilter {
    protected ShbRoleType getResourceRuleType(
            ContainerRequestContext requestContext) {
        UriRoutingContext routingContext = null;
        if(requestContext.getUriInfo()
                instanceof UriRoutingContext)
            routingContext = (UriRoutingContext)
                    requestContext.getUriInfo();
        if(routingContext == null)
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .type("text/plain; charset=UTF-8")
                            .build());

        ResourceMethodInvoker endpoint = null;
        if(routingContext.getEndpoint()
                instanceof ResourceMethodInvoker)
            endpoint = (ResourceMethodInvoker)
                    routingContext.getEndpoint();
        if(endpoint == null)
            requestContext.abortWith(
                    Response.status(Response.Status.BAD_REQUEST)
                            .type("text/plain; charset=UTF-8")
                            .build());

        Class<?> resourceClass = endpoint.getResourceClass();
        Method resourceMethod = endpoint.getResourceMethod();

        ShbRole annotationClass = resourceClass
                .getAnnotation(ShbRole.class);
        ShbRole annotationMethod = resourceMethod
                .getAnnotation(ShbRole.class);

        if(annotationMethod != null)
            return annotationMethod.roleType();
        else if(annotationClass != null)
            return annotationClass.roleType();
        else
            return ShbRoleType.GUEST;
    }
}

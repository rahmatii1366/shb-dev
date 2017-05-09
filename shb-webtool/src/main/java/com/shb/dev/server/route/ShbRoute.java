package com.shb.dev.server.route;

import com.shb.dev.server.role.ShbRoleType;

import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Mohammad Rahmati, 5/3/2017 10:57 AM
 */
public class ShbRoute {
    private Class targetClass;
    private Method targetMethod;
    private Parameter[] parameters;
    private ShbRoleType roleType;

    public ShbRoute(Class targetClass,
            Method targetMethod,
            Parameter[] parameters) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.parameters = parameters;
    }


    public Response call()
            throws IllegalAccessException,
            InstantiationException,
            InvocationTargetException {
        return (Response) targetMethod.invoke(
                targetClass.newInstance(), parameters);
    }
}

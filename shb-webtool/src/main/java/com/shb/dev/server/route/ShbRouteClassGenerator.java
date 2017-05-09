package com.shb.dev.server.route;

import com.shadbarg.secure.random.SecureRandomMaker;
import com.shadbarg.secure.random.SecureRandomType;
import com.shadbarg.secure.util.HexConverter;
import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbRouterConfig.ShbRouteConfig;
import sun.misc.Unsafe;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;
import static javax.tools.JavaFileObject.Kind.SOURCE;

/**
 * @author Mohammad Rahmati, 5/7/2017 5:20 PM
 */
public class ShbRouteClassGenerator {
    private static final String packageName =
            "com.shb.dev.server.rest.custom";
    private static final int CLASS_LEN = 16;
    private static final int METHOD_LEN = 16;

    public static Set<Class<?>> generateRouteClasses(
            ShbRouterConfig routerConfig)
            throws Exception {
        Set<String> routes = routerConfig.getRoutes();
        Set<Class<?>> classes = new HashSet<>();
        for(String route : routes) {
            final String className = getRandomName(CLASS_LEN);
            final String fullClassName = packageName
                    .replace('.', '/')
                    .concat("/")
                    .concat(className);
            StringBuilder sb = initializeRouteClass(
                    route, className);
            Set<String> httpMethods =
                    routerConfig.getHttpMethods(route);
            if(httpMethods != null) {
                for(String httpMethod : httpMethods) {
                    String fixHttpMethod = httpMethod;
                    if(httpMethod.contains("#"))
                        fixHttpMethod = httpMethod.substring(
                                0, httpMethod.indexOf("#"));
                    appendRouteMethod(fixHttpMethod,
                            routerConfig.getRouteConfig(
                                    route, httpMethod), sb);
                }
            }

            sb.append("}");

            File f = new File("d:/classes/".concat(className).concat(".txt"));
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(sb.toString().getBytes());
            fos.close();
            classes.add(registerClass(fullClassName, sb));
        }
        return classes;
//        final StringBuilder sb = new StringBuilder();
//        sb.append(" public String toString() {\n");
//        sb.append("     return \"HelloWorld - Java Dynamic Class Creation was written by Rob Austin\";");
//        sb.append(" }\n");
//        sb.append("}\n");
    }

    static StringBuilder initializeRouteClass(
            String route,
            String className)
            throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("package ".concat(packageName).concat(";\n"));
        sb.append("import com.shb.dev.server.role.ShbRole;\n");
        sb.append("import javax.ws.rs.core.*;\n");
        sb.append("import javax.ws.rs.*;\n");
        sb.append("import javax.annotation.PostConstruct;\n");
        sb.append("import javax.inject.Singleton;\n");
        sb.append("import java.lang.reflect.Method;\n");
        sb.append("import com.shb.dev.server.route.*;\n");
        sb.append("@Path(\""
                .concat(route)
                .concat("\")\n"));
        sb.append("public class ".concat(className)
                .concat(" extends ShbRouteService {\n"));
        return sb;
    }

    static void appendRouteMethod(
            String httpMethod,
            ShbRouteConfig routeConfig,
            StringBuilder sb) throws Exception {
        sb.append("@".concat(httpMethod).concat("\n"));
        if(routeConfig.getPathParams() != null) {
            String path = new String("");
            for(String pathParam :
                    routeConfig.getPathParams()) {
                path = path.concat("/{")
                        .concat(pathParam).concat("}");
            }
            sb.append("@Path(\"".concat(path).concat("\")\n"));
        }
        sb.append("@Produces(MediaType.APPLICATION_JSON)\n");
        String methodName = getRandomName(METHOD_LEN);
        sb.append("public Response ".concat(methodName)
                .concat("("));
        boolean clr = false;
        List<String> params = new ArrayList<>();
        if(routeConfig.getPathParams() != null) {
            for(String pathParam :
                    routeConfig.getPathParams()) {
                params.add(pathParam);
                sb.append("@PathParam(\"".concat(pathParam)
                        .concat("\") String ")
                        .concat(pathParam).concat(","));
                clr = true;
            }
        }
        if(routeConfig.getQueryParams() != null) {
            for(String queryParam :
                    routeConfig.getQueryParams()) {
                params.add(queryParam);
                sb.append("@QueryParam(\"".concat(queryParam)
                        .concat("\") String ")
                        .concat(queryParam).concat(","));
                clr = true;
            }
        }
        if (clr)
            sb.deleteCharAt(sb.length() - 1);
        sb.append(") throws Exception {\n");
        String resource = routeConfig.getHandler();
        String callMethodName = resource.substring(
                resource.lastIndexOf(".") + 1);
        String callClass = resource.substring(
                0, resource.lastIndexOf("."));
        sb.append("Class c = Class.forName(\"".concat(callClass)
                .concat("\");\n"));
        String methodDecStr = "Method m = c.getDeclaredMethod(\""
                        .concat(callMethodName).concat("\",");
        for(String p : params)
            methodDecStr = methodDecStr.concat("String.class,");
        sb.append(methodDecStr);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");\n");
        sb.append("return (Response)m.invoke(null,");
        for (String p : params)
            sb.append(p.concat(","));
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");\n");
        sb.append("}\n");
    }

    private static String getRandomName(int len)
            throws Exception {
        return "ALIAS".concat(HexConverter.toHexString(
                SecureRandomMaker.makeByteArray(
                        len, SecureRandomType.SHA_1_PRNG)));
    }

    private static Class registerClass(String fullClassName, StringBuilder sb) throws NoSuchFieldException, IllegalAccessException {
        // A byte array output stream containing the bytes that would be written to the .class file
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final SimpleJavaFileObject simpleJavaFileObject
                = new SimpleJavaFileObject(URI.create(fullClassName + ".java"), SOURCE) {

            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return sb;
            }

            @Override
            public OutputStream openOutputStream() throws IOException {
                return byteArrayOutputStream;
            }
        };

        final JavaFileManager javaFileManager = new ForwardingJavaFileManager(
                ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null)) {

            @Override
            public JavaFileObject getJavaFileForOutput(Location location,
                                                       String className,
                                                       JavaFileObject.Kind kind,
                                                       FileObject sibling) throws IOException {
                return simpleJavaFileObject;
            }
        };

        ToolProvider.getSystemJavaCompiler().getTask(
                null, javaFileManager, null, null, null, singletonList(simpleJavaFileObject)).call();

        final byte[] bytes = byteArrayOutputStream.toByteArray();

        // use the unsafe class to load in the class bytes
        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        final Unsafe unsafe = (Unsafe) f.get(null);
        final Class aClass = unsafe.defineClass(
                fullClassName, bytes, 0, bytes.length,
                ShbRouteClassGenerator.class.getClassLoader(),
                null);
        return aClass;
    }
}

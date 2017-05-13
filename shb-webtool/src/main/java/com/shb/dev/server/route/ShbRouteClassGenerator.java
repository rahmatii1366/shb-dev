package com.shb.dev.server.route;

import com.shadbarg.secure.random.SecureRandomMaker;
import com.shadbarg.secure.random.SecureRandomType;
import com.shadbarg.secure.util.HexConverter;
import com.shb.dev.server.config.ShbRouterConfig;
import com.shb.dev.server.config.ShbRouterConfig.ShbRouteConfig;
import com.shb.dev.server.role.ShbRoleType;
import com.shb.dev.server.session.ShbSession;
import org.apache.log4j.Logger;
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
    final static Logger logger =
            Logger.getLogger(ShbRouteClassGenerator.class);
    private static final String packageName =
            "com.shb.dev.server.rest.custom";
    private static final int CLASS_LEN = 16;
    private static final int METHOD_LEN = 16;

    public static Set<Class<?>> generateRouteClasses(
            ShbRouterConfig routerConfig)
            throws Exception {
        Set<String> routes = routerConfig.getRoutes();
        Set<Class<?>> classes = new HashSet<>();
        /**
         * setRoot = "/" default for index.html
         */
        boolean setRoot = false;
        for(String route : routes) {
            final String className = getRandomName(CLASS_LEN);
            final String fullClassName = packageName
                    .replace('.', '/')
                    .concat("/")
                    .concat(className);
            StringBuilder classSource = createClassSource(
                    routerConfig, route, className);

            writeClassToFile(className, classSource);

            classes.add(registerClass(
                    fullClassName, classSource));
        }
        if(setRoot == false) {

        }
        return classes;
//        final StringBuilder sb = new StringBuilder();
//        sb.append(" public String toString() {\n");
//        sb.append("     return \"HelloWorld - Java Dynamic Class Creation was written by Rob Austin\";");
//        sb.append(" }\n");
//        sb.append("}\n");
    }

    static StringBuilder createClassSource(
            ShbRouterConfig routerConfig,
            String route, String className
    ) throws Exception {
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
                ShbRouteConfig routeConfig = routerConfig
                        .getRouteConfig(route, httpMethod);
                appendRouteMethod(
                            route.concat(httpMethod),
                            fixHttpMethod,
                            routeConfig, sb);
            }
        }

        sb.append("}");
        return sb;
    }

    static void writeClassToFile(
            String className,
            StringBuilder classSource)
            throws IOException {
        File f = new File("d:/classes/"
                .concat(className)
                .concat(".txt"));
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(classSource.toString().getBytes());
        fos.close();
    }

    static StringBuilder initializeRouteClass(
            String route,
            String className)
            throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append("package ".concat(packageName).concat(";\n"));
        sb.append("import javax.ws.rs.core.*;\n");
        sb.append("import javax.ws.rs.*;\n");
        sb.append("import javax.inject.Singleton;\n");
        sb.append("import java.lang.reflect.Method;\n");
        sb.append("import com.shb.dev.server.route.*;\n");
        sb.append("import com.shb.dev.server.role.*;\n");
        sb.append("import com.shb.dev.server.response.*;\n");
        sb.append("import com.shb.dev.server.session.*;\n");
        sb.append("import com.shb.dev.server.asset.ShbAssetResolver;\n");
        sb.append("@Singleton\n");
        sb.append("@Path(\"".concat(route).concat("\")\n"));
        sb.append("public class ".concat(className)
                .concat(" extends ShbRouteService {\n"));
        return sb;
    }

    static void appendRouteMethod(
            String urlPath,
            String httpMethod,
            ShbRouteConfig routeConfig,
            StringBuilder sb)
            throws Exception {
        sb.append("@".concat(httpMethod).concat("\n"));
        String path = new String("");
        if(routeConfig.getPathParams() != null) {
            for(String pathParam :
                    routeConfig.getPathParams()) {
                path = path.concat("/{")
                        .concat(pathParam).concat("}");
            }
            sb.append("@Path(\"".concat(path).concat("\")\n"));
        }

        sb.append("@Produces(MediaType.APPLICATION_JSON)\n");
        String methodName = getRandomName(METHOD_LEN);
        ShbRoleType methodRoleType = ShbRoleType.ADMIN;
        try {
            methodRoleType = ShbRoleType
                    .getFromName(routeConfig.getRole());
//            sb.append("@ShbRole(roleType = ShbRoleType."
//                    .concat(methodRoleType.getName()).concat(")\n"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        sb.append("public Response ".concat(methodName)
                .concat("(@Context HttpHeaders httpHeaders,"));
        boolean clr = true;
        List<String> params = new ArrayList<>();
        if(routeConfig.getPathParams() != null) {
            for(String pathParam :
                    routeConfig.getPathParams()) {
                String pParam = pathParam;
                if(pathParam.contains(":"))
                    pParam = pathParam.substring(0,
                            pathParam.indexOf(":"));
                params.add(pathParam);
                sb.append("@PathParam(\""
                        .concat(pParam)
                        .concat("\") String ")
                        .concat(pParam.concat(",")));
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
        String callMethodName = null;
        String callClassName = null;
        if(resource != null && !resource.isEmpty()) {
            callMethodName = resource.substring(
                    resource.lastIndexOf(".") + 1);
            callClassName = resource.substring(
                    0, resource.lastIndexOf("."));
        }
        if((callClassName == null || callMethodName == null)
                && !routeConfig.isAsset()) {
            throw new Exception("handler is incorrect.");
        }

        String paramListStr = "";
        Class<?>[] paramList = new Class<?>[params.size() + 1];
        int i = 0;
        paramList[i++] = ShbSession.class;
        for(String p : params) {
            paramListStr = paramListStr.concat("String.class,");
            paramList[i++] = String.class;
        }
        sb.append("ShbRoleType methodRoleType=ShbRoleType."
                .concat(methodRoleType.getName())
                .concat(";\n"));
        sb.append("ShbResponse shbResponse = UNAUTHORIZED_RESPONSE;\n");
        sb.append("ShbSession session = doAuthorization(httpHeaders);\n");
        sb.append("if(methodRoleType.isValid(session.getRoleType())) {\n"
                .concat("try {\n"));
        boolean isAsset = routeConfig.isAsset();
        if(isAsset)
            sb.append("ShbAssetResolver assetResolver = "
                            .concat("registerAssetResolver(\"")
                    .concat(urlPath)
                    .concat("\",\"")
                    .concat(routeConfig.getAssetPath())
                    .concat("\");\n"));
        String registerMethod = "Method m = registerMethod(\""
                .concat(urlPath)
                .concat("\",\"");
        if(isAsset)
            if(callClassName == null || callMethodName == null)
                registerMethod = registerMethod.concat(
                        "com.shb.dev.server.route.ShbAssetService")
                        .concat("\",\"getAsset\",");
            else
                registerMethod = registerMethod.concat(callClassName)
                        .concat("\",\"")
                        .concat(callMethodName).concat("\",");
        else
            registerMethod = registerMethod.concat(callClassName)
                    .concat("\",\"")
                    .concat(callMethodName).concat("\",");
        registerMethod = registerMethod.concat("ShbSession.class,");

        if(isAsset)
            registerMethod = registerMethod
                    .concat("ShbAssetResolver.class,");
        registerMethod = registerMethod.concat(paramListStr);
        sb.append(registerMethod);
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");\n");

        if(isAsset)
            sb.append("shbResponse = invokeMethod(m, session, assetResolver,");
        else
            sb.append("shbResponse = invokeMethod(m, session,");
        for (String p : params) {
            if(p.contains(":"))
                sb.append(p.substring(0,
                        p.indexOf(":")).concat(","));
            else
                sb.append(p.concat(","));
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");\n} catch (Exception e) {\n");
        sb.append("logger.error(e);\n");
        sb.append("shbResponse = INTERNAL_SERVER_ERROR_RESPONSE;\n} \n}\n");
        sb.append("return createResponse(shbResponse, session);\n");
        sb.append("}\n");
    }

    private static String getRandomName(int len)
            throws Exception {
        return "ALIAS".concat(HexConverter.toHexString(
                SecureRandomMaker.makeByteArray(
                        len, SecureRandomType.SHA_1_PRNG)));
    }

    private static Class registerClass(
            String fullClassName,
            StringBuilder sb)
            throws NoSuchFieldException,
            IllegalAccessException {
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

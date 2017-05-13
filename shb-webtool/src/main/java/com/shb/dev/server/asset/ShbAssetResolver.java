package com.shb.dev.server.asset;

import org.apache.log4j.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * @author Mohammad Rahmati, 4/19/2017 11:00 AM
 */
public class ShbAssetResolver implements Runnable {
    private Logger logger = Logger.getLogger(
            ShbAssetResolver.class);
    private Map<String, ShbAsset> assetsMap =
            new LinkedHashMap<>();
    private Path rootPath;
    private static ReentrantLock lock = new ReentrantLock();
    private static ShbAssetResolver shbAssetResolver = null;
    private WatchService watchService;
    private WatchKey watchKey;

    private ShbAssetResolver(Path rootPath)
            throws IOException, InterruptedException {
        this.rootPath = rootPath;

        watchService = rootPath
                .getFileSystem().newWatchService();
        registerRecursive(this.rootPath, watchService);

//        this.watchKey = this.rootPath.register(
//                rootPath.getFileSystem().newWatchService(),
//                ENTRY_CREATE,
//                ENTRY_MODIFY,
//                ENTRY_DELETE
//        );

        ShbAssetResolver r = this;
        Executors.newSingleThreadExecutor()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            r.watchKey = watchService.take();
                            Executors.newScheduledThreadPool(5)
                                    .scheduleWithFixedDelay(
                                            r, 5, 10,
                                            TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static ShbAssetResolver getInstance(
            String publicPath) {
        try {
            lock.tryLock(100, TimeUnit.MILLISECONDS);
            Path path = Paths.get(publicPath);
            if(Files.exists(path)) {
                return new ShbAssetResolver(path);
            } else {
                throw new Exception("this path not exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void run() {
        for (WatchEvent we : this.watchKey.pollEvents()) {
            WatchEvent.Kind changedKind = we.kind();
            Path changedPath = (Path) we.context();
            Path dir = (Path) this.watchKey.watchable();
            Path fullPath = dir.resolve(changedPath);
            File file = new File(fullPath.toString());
            if (changedKind == ENTRY_DELETE) {
                if (file.isDirectory()) {
                    for (String key : assetsMap.keySet()) {
                        if (key.startsWith(fullPath.toString())) {
                            assetsMap.remove(key);
                        }
                    }
                } else {
                    assetsMap.remove(fullPath);
                }
            } else if (changedKind == ENTRY_MODIFY) {
                if (!file.isDirectory()) {
                    assetsMap.remove(fullPath.toString());
                }
            }
        }
    }

    private static void registerRecursive(
            final Path root,
            final WatchService watchService)
            throws IOException {
        // register all subfolders
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(
                    Path dir,
                    BasicFileAttributes attrs)
                    throws IOException {
                dir.register(watchService,
                        ENTRY_CREATE,
                        ENTRY_DELETE,
                        ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private ShbAsset reload(String path) {
        ShbAsset shbAsset = null;
        FileInputStream fileInputStream = null;
        File file = null;
        try {
            file = new File(rootPath.toString(), path);
            fileInputStream = new FileInputStream(file);
            int available = fileInputStream.available();
            String mediaType = Files
                    .probeContentType(file.toPath());
            if(mediaType == null || mediaType.isEmpty())
                mediaType = new MimetypesFileTypeMap()
                    .getContentType(file);
            byte[] bytes = new byte[available];
            int read = fileInputStream.read(bytes, 0, available);
            if(read == available) {
                shbAsset = new ShbAsset(bytes,
                        rootPath.toString(),
                        path, mediaType);
                assetsMap.put(shbAsset.getPath().toString(), shbAsset);
            }
            logger.info("load asset");
        } catch (FileNotFoundException e) {
            return catchException(e);
        } catch (IOException e) {
            return catchException(e);
        } finally {
            if(fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    return catchException(e);
                }
        }
        return shbAsset;
    }

    public ShbAsset resolve(String path) {
        if(path == null)
            return null;
        ShbAsset shbAsset = assetsMap.get(path);
        if(shbAsset != null) {
            return shbAsset;
        }
        shbAsset = reload(path);
        return shbAsset;
    }

    private ShbAsset catchException(Exception ex) {
        logger.error(ex);
        return new ShbAsset(null, rootPath.toString(),
                "not-found.txt", MediaType.TEXT_PLAIN);
    }
}

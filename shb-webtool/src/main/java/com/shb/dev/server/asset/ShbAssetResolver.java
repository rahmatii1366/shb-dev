package com.shb.dev.server.asset;

import org.apache.log4j.Logger;

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
    private Path publicPath;
    private static ReentrantLock lock = new ReentrantLock();
    private static ShbAssetResolver shbAssetResolver = null;
    private WatchService watchService;
    private WatchKey watchKey;

    private ShbAssetResolver(Path publicPath)
            throws IOException, InterruptedException {
        this.publicPath = publicPath;

        watchService = publicPath
                .getFileSystem().newWatchService();
        registerRecursive(this.publicPath, watchService);

//        this.watchKey = this.publicPath.register(
//                publicPath.getFileSystem().newWatchService(),
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

    public ShbAsset reload(String path) {
        ShbAsset shbAsset = null;
        FileInputStream fileInputStream = null;
        File f = null;
        try {
            f = new File(publicPath.toString(), path);
            fileInputStream = new FileInputStream(f);
            int available = fileInputStream.available();
            byte[] bytes = new byte[available];
            int read = fileInputStream.read(bytes, 0, available);
            if(read == available) {
                shbAsset = new ShbAsset(
                        bytes, path, publicPath.toString());
                assetsMap.put(shbAsset.getPath().toString(), shbAsset);
            }
            logger.info("load asset");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
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


}

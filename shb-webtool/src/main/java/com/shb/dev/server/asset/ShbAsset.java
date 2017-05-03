package com.shb.dev.server.asset;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Mohammad Rahmati, 4/19/2017 1:27 PM
 */
public class ShbAsset {
    private byte[] bytes;
    private String relativePath;
    private String rootPath;
    private Path path;

    public ShbAsset(
            byte[] bytes,
            String relativePath,
            String rootPath)
            throws IOException {
        this.bytes = bytes;
        this.relativePath = relativePath;
        this.rootPath = rootPath;
        this.path = Paths.get(rootPath, relativePath);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Path getPath() {
        return path;
    }
}

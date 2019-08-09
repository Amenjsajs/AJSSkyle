package com.ajs.components;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DirectoriesPath {
    private static final Path CURRENT = Paths.get("").toAbsolutePath();
    private static final Path AVATAR = Paths.get(CURRENT+"/sauvegarde/images/avatar");
    private static final Path IMAGES_SAVED = Paths.get(CURRENT+"/sauvegarde/images");
    private static final Path FILES_SAVED = Paths.get(CURRENT+"/sauvegarde/files");
    private static final Path IMAGES = Paths.get(CURRENT+"/images");
    private static final Path ICON = Paths.get(CURRENT+"/images/icon");

    private static Path getPath(Path path){
        if(!Files.exists(path)){
            path.toFile().mkdirs();
        }
        return path;
    }

    public static Path getAvatarPath() {
        return getPath(AVATAR);
    }

    public static Path getImagesSavedPath() {
        return getPath(IMAGES_SAVED);
    }

    public static Path getFilesSavePath() {
        return getPath(FILES_SAVED);
    }

    public static Path getImagesPath() {
        return getPath(IMAGES);
    }

    public static Path getIconPath() {
        return getPath(ICON);
    }
}

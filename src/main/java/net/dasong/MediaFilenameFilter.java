package net.dasong;

import java.io.File;
import java.io.FilenameFilter;

public class MediaFilenameFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        if (name.startsWith("IMG_") && (name.endsWith("MOV") || name.endsWith("JPG"))) {
            return true;
        }

        return false;
    }
}

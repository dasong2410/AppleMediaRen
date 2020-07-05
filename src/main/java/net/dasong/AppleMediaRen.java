package net.dasong;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.common.base.CharMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AppleMediaRen {
    public int getRandom() {
        Random random = new Random();
        int rd = random.nextInt(899999) + 100000;

        return rd;
    }

    public String getMediaCreateDate(File file, String suffix) {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException e) {
            System.out.println(file.getName());
            System.out.println(e.getMessage());

            return null;
        } catch (IOException e) {
            System.out.println(file.getName());
            System.out.println(e.getMessage());

            return null;
        }

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
//                System.out.println(tag.getDirectoryName() + " -> " + tag.getTagName() + " -> " + tag.getDescription());

                if (suffix.equals("MOV") && tag.getDirectoryName().equals("QuickTime Metadata") && tag.getTagName().equals("Creation Date")
                        || suffix.equals("JPG") && tag.getDirectoryName().equals("Exif IFD0") && tag.getTagName().equals("Date/Time")) {
//                    System.out.println(tag.getTagName() + " -> " + tag.getDescription());
                    // MOV 2020-07-03T11:01:54+0800
                    // JPG 2019:10:01 12:21:42
                    String dateStr = CharMatcher.inRange('0', '9').retainFrom(tag.getDescription());

                    return dateStr.substring(0, 14);
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage:\n  java -jar AppleMediaRen.jar media_dir");
            System.exit(1);
        }

        AppleMediaRen appleMediaRen = new AppleMediaRen();

        String dirStr = args[0];
        File fileDir = new File(dirStr);
        File[] mediaFiles = fileDir.listFiles(new MediaFilenameFilter());

        for (File file : mediaFiles) {
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.length() - 3, fileName.length());
            String datetimeStr = appleMediaRen.getMediaCreateDate(file, suffix);

            if (datetimeStr != null) {
                String dateStr = datetimeStr.substring(0, 8);
                String timeStr = datetimeStr.substring(8, 14);

                String newFileName = "IMG_" + dateStr + "_" + timeStr + "_" + appleMediaRen.getRandom() + "." + suffix;
                String newFilePath = dirStr + "/" + newFileName;
                File newFile = new File(newFilePath);

                if (newFile.exists()) {
                    System.out.println("File already exist: " + newFileName);
                } else {
//                    System.out.println("mv " + fileName + " " + newFileName);
                    System.out.println("Rename file: [" + fileName + "] -> [" + newFileName + "]");
                    file.renameTo(newFile);
                }
            } else {
                System.out.println("Failed to extract date from file: " + fileName);
            }
        }
    }
}

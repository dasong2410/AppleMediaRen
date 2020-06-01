package net.dasong;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File("/Users/marcus/Downloads/IMG_7249.MOV"));

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag.getDirectoryName() + " -> " + tag.getTagName() + " -> " + tag.getDescription());

                if(tag.getTagName().equals("[QuickTime] Creation Time")){
                    System.out.println(tag.getDescription());
                }
            }
        }
    }
}

package net.dasong;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class Ren {

	public String getDateStr(Metadata metadata) {
		String dtStr = null;

		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				// System.out.format("[%s] - %s = %s \n", directory.getName(),
				// tag.getTagName(), tag.getDescription());
				if (tag.getTagName().equals("Date/Time Original")) {
					dtStr = tag.getDescription().replace(":", "");

					return dtStr;
				}
			}

			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					System.err.format("ERROR: %s", error);
				}
			}
		}

		return dtStr;
	}

	public static void main(String[] args) throws ImageProcessingException, IOException {
//		String dirStr = "E:/迅雷下载/apple/tmp";
		String dirStr = "D:\\apple\\img";
		File fileDir = new File(dirStr);

		for (File file : fileDir.listFiles()) {
			Metadata metadata = ImageMetadataReader.readMetadata(file);

			Random random = new Random();
			int rd = random.nextInt(899999) + 100000;

			Ren ren = new Ren();
			String dtStr = ren.getDateStr(metadata);
			String dtField = "null";
			String tmField = "null";

			if (dtStr != null) {
				String[] dtStrs = dtStr.split(" ");
				dtField = dtStrs[0];
				tmField = dtStrs[1];
			} else {
				continue;
			}

			String newFilename = "IMG_" + dtField + "_" + tmField + "_" + rd + ".JPG";
			System.out.println(newFilename);

			File newFile = new File(dirStr + "/" + newFilename);

			if (newFile.exists()) {
				System.out.println(newFile.getAbsolutePath() + " exist.");
			} else {
				System.out.println(newFile.getAbsolutePath() + " does not exist, rename file.");

				file.renameTo(newFile);
			}

		}
	}

}
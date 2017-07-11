package com.course.aca.service.system;

import com.coremedia.iso.IsoFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

/**
 * // todo - ner!!!!
 */
@Service
public class FileManager {

    /**
     * Moves specified file from source to destination.
     *
     * @param source Source file's path.
     * @param destination Destination file's path.
     * @param fileName File's name, that will be moved.
     *
     * @return True, if movement is successfully done, otherwise returns False.
     */
    public boolean moveFile(String source, String destination, String fileName) {
        Path sourcePath = Paths.get(source + File.separator + fileName);
        Path targetPath = Paths.get(destination + File.separator + fileName);
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     *
     *
     * @param multipartFile
     * @param fileDirectory
     * @param fileName
     * @throws IOException
     */
    public void writeFile(MultipartFile multipartFile, String fileDirectory , String fileName) throws IOException {
        byte [] bytes = multipartFile.getBytes();

        File file = new File(fileDirectory + File.separator + fileName);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(bytes);
        outputStream.close();
    }

    /**
     * Reckons multimedia file's duration.
     *
     * @param path File's directory direct path.
     * @param fileName File's Name.
     *
     * @return Multimedia file's duration.
     *
     * @throws IOException Chained up from {@link IsoFile}'s constructor.
     */
    public long getVideoFileDuration(String path, String fileName) throws IOException {
        IsoFile isoFile = new IsoFile(path + File.separator + fileName);
        return isoFile.getMovieBox().getMovieHeaderBox().getDuration();
    }
}

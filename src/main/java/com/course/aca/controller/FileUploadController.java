package com.course.aca.controller;

import com.course.aca.annotation.BelongToTeacher;
import com.course.aca.exception.IllegalParameterException;
import com.course.aca.model.dto.ItemInfo;
import com.course.aca.model.entity.PathComponentsEntity;
import com.course.aca.service.TeacherService;
import com.course.aca.service.system.DirectoryHandler;
import com.course.aca.service.system.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

// TODO: 7/1/2017 tooooooo much logic for simple controller class. maybe needs refactoring
/**
 * A controller class
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
@RestController
public class FileUploadController {
    private static final long FILE_SIZE = 100 * 1024 * 1024;
    private static final String TMP_DIRECTORY = "tmpFiles";
    private static final String FILE_TMP_DIRECTORY = getCatalinaTmpPath();
    private final List<String> extensions = Arrays.asList(".mp4", ".doc", ".docx", ".pdf");
    private final FileManager fileManager;
    private final TeacherService teacherService;

    /**
     * Initializes autowired beans in this class.
     *
     * @param fileManager
     * @param teacherService
     */
    @Autowired
    public FileUploadController(FileManager fileManager, TeacherService teacherService) {
        this.fileManager = fileManager;
        this.teacherService = teacherService;
    }



    @BelongToTeacher
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity upload(@RequestHeader("Authorization") String authToken,
                                 @RequestParam("courseId") int courseId,
                                 @RequestParam("sectionId") int sectionId,
                                 @RequestParam("name") String name,
                                 @RequestParam("isPublic") boolean isPublic,
                                 @RequestParam MultipartFile file) {

        try {
            if (teacherService.getCourseIdBySection(sectionId) != courseId)
                return new ResponseEntity<>("section-course doesn't exist", HttpStatus.BAD_REQUEST);
        } catch (IllegalParameterException e) {
            e.printStackTrace();
            return new ResponseEntity<>("IllegalParameterException", HttpStatus.BAD_REQUEST);
        }

        if (!file.isEmpty()) {
            final String fileName = file.getOriginalFilename();
            final String fileExtension = fileName.substring(fileName.lastIndexOf("."));


            if (!extensions.contains(fileExtension))
                return new ResponseEntity<>("unsupported file type",
                        HttpStatus.BAD_REQUEST);

            if (file.getSize() > FILE_SIZE)
                return new ResponseEntity<>("file size must be less than 100 Mb",
                        HttpStatus.BAD_REQUEST);

            try {
                String sectionName = teacherService.getSectionName(sectionId);
                PathComponentsEntity components = teacherService.getPathComponents(courseId);

                final String target = DirectoryHandler.getUploadPath(components,sectionName);

                Path path = Paths.get(target, fileName);
                if (Files.notExists(path)) {
                    Files.createDirectories(path);
                    fileManager.writeFile(file, FILE_TMP_DIRECTORY, fileName);
                    fileManager.moveFile(FILE_TMP_DIRECTORY, target, fileName);
                } else {
                    return new ResponseEntity<>("file already exists", HttpStatus.BAD_REQUEST);
                }

                //add info about file in DB
                ItemInfo itemInfo = getItemInfo(isPublic,name,fileExtension,fileName,sectionId);
                teacherService.addFile(itemInfo);

                //add update course info( add materials, totalHours)
                long duration;
                if (fileExtension.equals(".mp4")) {
                    duration = fileManager.getVideoFileDuration(target, fileName);
                } else {
                    duration = 0;
                }

                teacherService.updateCourseDuration(courseId, duration);

                return new ResponseEntity<>("you successfully uploaded file " + fileName,
                        HttpStatus.OK);

            } catch (IOException e) {

                return new ResponseEntity<>("you failed to upload " + fileName,
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("file is empty",
                    HttpStatus.BAD_REQUEST);
        }
    }

    private static final String getCatalinaTmpPath() {
        File file = new File(System.getProperty("catalina.home") + File.separator + TMP_DIRECTORY);
        if (!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }

    private ItemInfo getItemInfo(boolean isPublic,String name,String extension,String fileName,int sectionId){
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setPublic(isPublic);
        itemInfo.setName(name);
        itemInfo.setResourceType(extension.equals(".mp4") ? "video" : "document");
        itemInfo.setResourceName(fileName);
        itemInfo.setSectionId(sectionId);
        return itemInfo;
    }
}

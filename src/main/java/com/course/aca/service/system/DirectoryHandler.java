package com.course.aca.service.system;

import com.course.aca.model.entity.PathComponentsEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides public and static methods, which manipulates with hard disk drive's file system.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class DirectoryHandler {

    // TODO: 6/30/2017 Rename this variable using naming conventions
    /**
     *
     */
    public static final String root = System.getProperty("user.home") + File.separator + "project";

    public static boolean createCategory(String category) throws IOException {
        return createDirectory(root, category);
    }

    /**
     * Creates subcategory directory in hard disk drive using passed path and subcategoryName.
     *
     * @param categoryName Category's name, in which new subcategory's directory will be created.
     * @param subcategoryName Subcategory's name, that will be newly created.
     *
     * @return True, if new directory is successfully created. Otherwise returns False.
     *
     * @throws IOException Chained up from {@link #createDirectory(String, String)}
     */
    public static boolean createSubcategory(String categoryName, String subcategoryName) throws IOException {
        String path = root + File.separator + categoryName;
        return createDirectory(path, subcategoryName);
    }

    /**
     * Creates course directory in hard disk drive using passed path and courseName.
     *
     * @param categoryName Category directory's name. used in path combination.
     * @param subcategoryName Subcategory directory's name, used in path combination.
     * @param courseName Course's name that will be newly created.
     *
     * @return True, if new directory is successfully created. Otherwise returns False.
     *
     * @throws IOException Chained up from {@link #createDirectory(String, String)}
     */
    public static boolean createCourse(String categoryName, String subcategoryName, String courseName) throws IOException {
        String path = root + File.separator + categoryName + File.separator + subcategoryName;
        return createDirectory(path, courseName);
    }

    /**
     *
     *
     * @param categoryName Category directory's name. used in path combination.
     * @param subcategoryName Subcategory directory's name, used in path combination.
     * @param courseName Course directory's name, used in path combination.
     * @param sectionName Section's name that will be newly created.
     *
     * @return True, if new directory is successfully created. Otherwise returns False.
     *
     * @throws IOException Chained up from {@link #createDirectory(String, String)}
     */
    public static boolean createCourseSection(String categoryName, String subcategoryName, String courseName, String sectionName)
            throws IOException {
        String path = root + File.separator + categoryName + File.separator + subcategoryName + File.separator + courseName;
        return createDirectory(path, sectionName);
    }

    /**
     * Creates directory with name passed by argument, relative to root, also passed by argument.
     *
     * @param root
     * @param directoryName
     * @return
     * @throws IOException
     */
    private synchronized static boolean createDirectory(String root, String directoryName) throws IOException {
        Path path = Paths.get(root, directoryName);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
            return true;
        }
        return false;
    }

    /**
     *
     *
     * @param components
     * @param section
     * @return
     */
    public static String getUploadPath(PathComponentsEntity components, String section){
        return DirectoryHandler.root + File.separator +
                components.getCategoryName() + File.separator +
                components.getSubcategoryName() + File.separator +
                components.getCourseName() + File.separator +
                section;
    }


}

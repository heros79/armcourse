package com.course.aca.service;

import com.course.aca.dao.MainDao;
import com.course.aca.model.dto.Category;
import com.course.aca.model.dto.CategoryInformation;
import com.course.aca.model.dto.SubCategory;
import com.course.aca.model.entity.CategoryEntity;
import com.course.aca.model.entity.SubCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides public methods associated with index.html
 * page for manipulations with Data gotten from DB.
 *
 * Uses @{@link MainDao} bean for this purposes.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */

@Service
public class MainService {

    /**
     * Instance of @{@link MainDao} bean.
     * Provides @{@link MainDao}'s functionality in this class
     * Initialized in constructor
     *
     */
    private final MainDao mainDao;

    /**
     * This fields store data from DataBase.
     *
     * Initialized in none "lazy" way, in constructor.
     */
    private List<CategoryEntity> categoryEntities;
    private List<SubCategoryEntity> subCategoryEntities;

    /**
     * Initializes @{@link MainDao} bean "autowiredely".
     * Bean is defined in classpath:spring/spring-bean
     *
     * Also initializes storage variables in this class.
     *
      * @param mainDao
     */
    @Autowired
    public MainService(MainDao mainDao) {
        this.mainDao = mainDao;

        try {
            //Initializes categoryEntities
            categoryEntities = mainDao.getCategories();
            //Initializes subCategoryEntities
            subCategoryEntities = mainDao.getSubCategories();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets categoryEntities and subCategoryEntities data from mainDao and
     * wraps it into @{@link CategoryInformation} DTO.
     *
     * @return @{@link List<CategoryInformation>} which contains combined
     * data from @{@link CategoryEntity} and @{@link SubCategoryEntity}
     */
    public List<CategoryInformation> getCategoriesInfo(){
        List<CategoryInformation> categoryInformations = new ArrayList<>();

        for(CategoryEntity categoryEntity : categoryEntities){
            CategoryInformation categoryInformation = new CategoryInformation();

            Category category = new Category();
            category.setId(categoryEntity.getId());
            category.setName(categoryEntity.getName());

            categoryInformation.setCategory(category);

            List<SubCategory> subCategories = new ArrayList<>();

            subCategoryEntities.stream()
            .filter(s -> (s.getCategoryId() == category.getId()))
            .forEach(s -> subCategories.add((new SubCategory(s.getId(), s.getName()))));

            categoryInformation.setSubCategories(subCategories);

            categoryInformations.add(categoryInformation);
        }

        return categoryInformations;
    }


}

package com.course.aca.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLConnection;

/**
 * Provides by default implemented download functionality.
 *
 * Contains following methods: downloadFile.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public interface FileDownloader {

    /**
     *  Provides download functionality.
     *
     * @param path Stores direct path to requested file;
     *
     * @return {@link InputStreamResource} wrapped into {@link ResponseEntity} with "OK" HttpStatus if file exists.
     * Otherwise returns {@link ResponseEntity} with "BAD_REQUEST" HttpStatus.
     */
    default ResponseEntity downloadFile(String path) {

        File file = new File(path);

        if (!file.isFile())
            return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(mimeType));
        headers.setContentLength(file.length());
        headers.setCacheControl("no-cache");
        headers.set("Content-Disposition", "attachment; filename=" + file.getName());

        try {
            return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}

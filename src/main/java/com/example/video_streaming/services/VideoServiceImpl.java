package com.example.video_streaming.services;

import com.example.video_streaming.models.Video;
import com.example.video_streaming.repositories.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService{

    @Value("${file.path}")
    String DIR;

    @Autowired
    VideoRepository videoRepository;

    @PostConstruct
    public void init(){
        File file=new File(DIR);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder Created");

        }
        else {
            System.out.println("Folder already created");
        }
    }


    @Override
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Override
    public Video getVideoById(String id) {
        return videoRepository
                .findById(id)
                .orElseThrow(
                        ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Couldn't get Video"));
    }

    @Override
    public Video saveVideo(Video video, MultipartFile file) {


        try{
            if (file.isEmpty()) {
                throw new RuntimeException("Upload Valid File");

            }
            String filePath=file.getOriginalFilename();
            String contentType=file.getContentType();

            InputStream inputStream=file.getInputStream();

            assert filePath != null;

            String cleanPath=   StringUtils.cleanPath(filePath); //clean Path
                 String cleanDir=StringUtils.cleanPath(DIR); //clean Dir
            Path path=Paths.get(cleanDir,cleanPath); //get final path

            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING); //save the video file

            video.setContentType(contentType);
            video.setVideoUrl(path.toString());

            return videoRepository.save(video);
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


    }

package com.example.video_streaming.services;

import com.example.video_streaming.models.Video;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

     List<Video> getAllVideos();
     Video getVideoById(String id);
     Video saveVideo(Video video, MultipartFile file);
     public void createThumbnail(String videoId,String folderPath);
}

package com.example.video_streaming.controllers;

import com.example.video_streaming.errors.ErrorResponse;
import com.example.video_streaming.models.Video;
import com.example.video_streaming.services.VideoService;
import com.example.video_streaming.services.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
    @Autowired
    private VideoServiceImpl videoService;

    @GetMapping("")
    public ResponseEntity<?> getAllVideos()
    {
        try {
            List<Video> videos=videoService.getAllVideos();
            return ResponseEntity.ok().body(videos);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getMessage(),e.getStatusCode());
        } catch (RuntimeException e) {
            return  ResponseEntity.internalServerError().body("Server Error");
        }
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveVideo(@RequestParam("title") String title,@RequestParam("description") String description, @RequestParam("file") MultipartFile file){
        try {
            if(title.isEmpty()){
                return  ResponseEntity.badRequest().body(new ErrorResponse("Error Occurred","Add a Title!!"));
            }
            if(description.isEmpty()){
                return  ResponseEntity.badRequest().body(new ErrorResponse("Error Occurred","Add a description!!"));
            }
            System.out.println("got");
            Video video = new Video();
            video.setId(UUID.randomUUID().toString());
            video.setTitle(title);
            video.setDescription(description);

            Video savedVideo = videoService.saveVideo(video, file);
            return ResponseEntity.ok().body(savedVideo);
        } catch (RuntimeException ex) {
            ErrorResponse errorResponse = new ErrorResponse("File Upload Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( errorResponse);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse("File Upload Error", "Something unexpected has occurred!!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

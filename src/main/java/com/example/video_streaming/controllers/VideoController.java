package com.example.video_streaming.controllers;

import com.example.video_streaming.AppConstants;
import com.example.video_streaming.errors.ErrorResponse;
import com.example.video_streaming.models.Video;
import com.example.video_streaming.services.VideoService;
import com.example.video_streaming.services.VideoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<?> getVideoById(@PathVariable String videoId){
        System.out.println("got");
        try {
            Video video=videoService.getVideoById(videoId);
            String contentType=video.getContentType();
            String path=video.getVideoUrl();
            Path videoPath = Path.of(path);
            Resource resource= new FileSystemResource(path);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        } catch (ResponseStatusException e) {
            ErrorResponse errorResponse=new ErrorResponse("File Access Error",e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

    }

    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<?> streamVideo(@PathVariable String videoId, @RequestHeader(value = "Range",required = false) String range ){

        try{
            Video video=videoService.getVideoById(videoId);

            Path path= Paths.get(video.getVideoUrl());
            String contentType=video.getContentType();
            if (contentType == null) {
                contentType="application/octet-stream";
            }

            long fileLength= path.toFile().length();

            if (range==null){
                return  ResponseEntity.ok().body(MediaType.parseMediaType(contentType));
            }
            long rangeStart;
            long rangeEnd;

           String[] ranges= range.replace("bytes=","").split("-");

           rangeStart=Long.parseLong(ranges[0]);
           rangeEnd=rangeStart+ AppConstants.CHUNK_SIZE -1;

            System.out.println("start (bytes): "+rangeStart);
            System.out.println("end (bytes): "+rangeEnd);
           if (rangeEnd>=fileLength){
               rangeEnd=fileLength-1;
           }

            InputStream inputStream;
           try {
               inputStream=Files.newInputStream(path);
               inputStream.skip(rangeStart);

               long contentLength=rangeEnd-rangeStart+1;

               byte[] data=new byte[(int) contentLength];
               int read=inputStream.read(data,0,data.length);

               System.out.println("start (bytes): "+rangeStart);
               System.out.println("end (bytes): "+rangeEnd);
               System.out.println("read (bytes): "+read);


               HttpHeaders httpHeaders=AppConstants.getHTTPHeadersForVideo(rangeStart,rangeEnd,fileLength,contentLength);

               return  ResponseEntity.
                       status(HttpStatus.PARTIAL_CONTENT)
                       .headers(httpHeaders)
                       .contentType(MediaType.parseMediaType(contentType))
                       .body(new ByteArrayResource(data));

           }catch (IOException e){
               ErrorResponse errorResponse=new ErrorResponse("File Access Error",e.getMessage());
               return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
           }


        } catch (RuntimeException e) {
            ErrorResponse errorResponse=new ErrorResponse("File Access Error",e.getMessage());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }


    }

    @GetMapping("/getThumbnail/{videoId}")
    public ResponseEntity<?> serveThumbnail(@PathVariable String videoId){
        try{
            Video video = videoService.getVideoById(videoId);
            String thumbnailUrl = video.getThumbnailUrl();
            Path thumbnailPath = Paths.get(thumbnailUrl);
            Resource resource = new FileSystemResource(thumbnailPath);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        }catch (Exception e)
        {
            ErrorResponse response=new ErrorResponse(e.toString(),e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }


    }
    @GetMapping("/video/{videoId}")
    public ResponseEntity<?> getVideoDataById(@PathVariable String videoId ){
        try{
            System.out.println("got");
            Video video = videoService.getVideoById(videoId);
            return ResponseEntity.ok().body(video);
        } catch (ResponseStatusException e) {
            ErrorResponse errorResponse=new ErrorResponse(e.getReason(),e.getMessage());
            return  new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


}

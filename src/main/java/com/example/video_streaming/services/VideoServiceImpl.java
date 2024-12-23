package com.example.video_streaming.services;

import com.example.video_streaming.AppConstants;
import com.example.video_streaming.models.Video;
import com.example.video_streaming.repositories.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
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
        try {
            // Validate input file
            if (file.isEmpty()) {
                throw new RuntimeException("Not a valid file.");
            }

            System.out.println("File uploading");

            // Get file details
            String filePath = file.getOriginalFilename().replace(" ","_");
            String contentType = file.getContentType();

            if (filePath == null) {
                throw new RuntimeException("File name cannot be null.");
            }

            System.out.println("Content Type: " + contentType);
            System.out.println("Original File Name: " + filePath);

            // Clean paths to avoid potential security issues
            String cleanPath = StringUtils.cleanPath(filePath);
            String cleanDir = StringUtils.cleanPath(DIR);

            // Create a directory for the video using its ID
            Path videoDirPath = Paths.get(cleanDir, video.getId());
            if (!Files.exists(videoDirPath)) {
                Files.createDirectories(videoDirPath);
            }
            System.out.println("Video directory created: " + videoDirPath);

            // Save the uploaded file in the video directory
            Path originalFilePath = videoDirPath.resolve(cleanPath);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, originalFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("File saved: " + originalFilePath);

            // Check if the file is an AVI file
            Path finalPath = originalFilePath; // Path to be set as video URL
            if ("video/x-msvideo".equals(contentType)||"video/avi".equals(contentType)) {
                System.out.println("AVI file detected. Converting to MP4...");

                // Convert file name to MP4
                String mp4FileName = cleanPath.replace(".avi", ".mp4");
                Path mp4FilePath = videoDirPath.resolve(mp4FileName);

                System.out.println("Target MP4 Path: " + mp4FilePath);

                // Ensure original file exists
                if (!Files.exists(originalFilePath)) {
                    throw new FileNotFoundException("Original AVI file not found: " + originalFilePath);
                }

                // Perform AVI to MP4 conversion
                convertAviToMp4(originalFilePath.toString(), mp4FilePath.toString());
                System.out.println("AVI to MP4 conversion successful: " + mp4FilePath);

                // Update final path and content type
                finalPath = mp4FilePath;
                contentType = "video/mp4";
            }

            // Create a thumbnail for the video
            String thumbnailPath = videoDirPath.resolve("thumbnail.png").toString();
            createThumbnail(finalPath.toString(), thumbnailPath);
            System.out.println("Thumbnail created: " + thumbnailPath);

            video.setContentType(contentType);
            video.setVideoUrl(finalPath.toString());
            video.setThumbnailUrl(thumbnailPath);


            return videoRepository.save(video);

        } catch (IOException | RuntimeException | InterruptedException ex) {
            throw new RuntimeException("Error while saving video: " + ex.getMessage(), ex);
        }
    }

    public void createThumbnail(String videoPath,String folderPath){


        //ffmpeg -i videos\\a7a14ef9-af02-4e0a-a8e6-a37ffe7c05b4\\clideo_editor_eba83ec432ae4674b6012019ad0e01af.mp4 -vf "thumbnail" -frames:v 1 videos\\a7a14ef9-af02-4e0a-a8e6-a37ffe7c05b4\\thumbnail.png

        String command = "ffmpeg" +
                " -i \"" + videoPath + "\"" + // Input video file
                " -vf \"thumbnail\"" + // Video filter to generate a thumbnail
                " -frames:v 1" + // Capture one frame
                " -update 1" + // Ensure single-image output
                " \"" + folderPath +  "\"" // Output thumbnail path
                ;
        System.out.println(command);

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder(); // To capture error output

        try {
            // Create a process to execute the FFmpeg command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(false); // Separate error and output streams
            Process process = processBuilder.start();

            // Capture standard output
            try (BufferedReader stdOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdOutputReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Capture error output
            try (BufferedReader errorOutputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorOutputReader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg command failed with exit code " + exitCode
                        + "\nError: " + errorOutput.toString());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error executing FFmpeg command", e);
        }

        // Log error output if needed
        if (errorOutput.length() > 0) {
            System.err.println("FFmpeg Error Output:");
            System.err.println(errorOutput.toString());
        }
    }

    public void convertAviToMp4(String inputFilePath, String outputFilePath) throws IOException, InterruptedException {
        // Command to convert AVI to MP4 using FFmpeg (wrapping paths in quotes)
        String command = String.format("ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict experimental \"%s\"", inputFilePath, outputFilePath);

        System.out.println("Executing FFmpeg command: " + command);

        // Use ProcessBuilder to execute the command
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true); // Combine error and output streams
        Process process = processBuilder.start();

        // Capture the combined output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line); // Print FFmpeg progress to console
            }
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg command failed with exit code " + exitCode
                    + "\nOutput:\n" + output.toString());
        }

        System.out.println("FFmpeg command completed successfully.");
    }}

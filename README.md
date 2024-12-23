# Video Upload and Conversion Project

This project is a web application that allows users to upload, convert, and view videos. The backend is built using **Spring Boot**, the frontend is built with **React**, and **FFmpeg** is used for video processing. The main feature of this application is to upload **AVI** files, automatically convert them to **MP4** format, and generate thumbnails for the videos.

## Features

- **Upload Videos**: Users can upload videos in various formats (currently supports **AVI**).
- **Video Streaming in Chunks**: Videos are streamed in 1MB chunks. The backend will send 1MB of video data each time it's requested by the browser. This allows for efficient video streaming even for large video files.
- **Convert AVI to MP4**: The backend uses **FFmpeg** to convert uploaded **AVI** videos to **MP4** format.
- **Generate Thumbnails**: Thumbnails for uploaded videos are automatically generated and stored.
- **View Uploaded Videos**: After uploading and converting the videos, users can view the uploaded videos in the browser.

## Technology Stack

- **Frontend**:
  - **React**: A JavaScript library for building the user interface of the application.
  - **Axios**: To make HTTP requests to the backend for uploading videos and fetching data.

- **Backend**:
  - **Spring Boot**: A Java framework used for building the backend REST API.
  - **FFmpeg**: A powerful multimedia processing tool to convert videos from **AVI** format to **MP4** and generate thumbnails.
  
- **Database**: Local Host for storing Videos and Postgres for storing metadata.



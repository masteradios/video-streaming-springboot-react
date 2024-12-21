package com.example.video_streaming.repositories;

import com.example.video_streaming.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video,String> {


}

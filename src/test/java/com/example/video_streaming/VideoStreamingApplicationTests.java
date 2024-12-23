package com.example.video_streaming;

import com.example.video_streaming.services.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VideoStreamingApplicationTests {
	@Autowired
	VideoService videoService;
	@Test
	void contextLoads() {



		videoService.createThumbnail("videos\\\\a7a14ef9-af02-4e0a-a8e6-a37ffe7c05b4\\\\clideo_editor_eba83ec432ae4674b6012019ad0e01af.mp4","videos\\\\a7a14ef9-af02-4e0a-a8e6-a37ffe7c05b4");
	}

}

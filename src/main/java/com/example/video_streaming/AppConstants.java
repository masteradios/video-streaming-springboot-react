package com.example.video_streaming;

import org.springframework.http.HttpHeaders;

public class AppConstants {
    public static  final int CHUNK_SIZE=1024*1024;
    public  static HttpHeaders getHTTPHeadersForVideo(long rangeStart,long rangeEnd,long fileLength,long contentLength){
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Range","bytes "+rangeStart+"-"+rangeEnd+"/"+fileLength);
        httpHeaders.add("Cache-Control","no-cache,no-store,must-revalidate");
        httpHeaders.add("Pragma","no-cache");
        httpHeaders.add("Expires","0");
        httpHeaders.add("X-Content-Type-Options","nosniff");
        httpHeaders.setContentLength(contentLength);
        return  httpHeaders;
    }
}

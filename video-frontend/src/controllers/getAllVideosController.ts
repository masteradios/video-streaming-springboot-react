import ErrorResponse from "../models/errorResponse";
import Video from "../models/video";
import getAllVideosService from "../services/getAllVideosService";

async function getAllVideosController(): Promise<Video[]> {
    
    try {
      const videos = await getAllVideosService();

      return videos;
    } catch (error) {
      throw error as ErrorResponse; // Ensure the error is typed as ErrorResponse
    }
}
export default getAllVideosController;
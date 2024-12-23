import ErrorResponse from "../models/errorResponse";
import getVideoByIdService from "../services/getVideoByIdService";

async function getVideoByIdController(id:string) {
    try {
      const videos = await getVideoByIdService(id);

      return videos;
    } catch (error) {
      throw error as ErrorResponse; // Ensure the error is typed as ErrorResponse
    }
}
export default getVideoByIdController;
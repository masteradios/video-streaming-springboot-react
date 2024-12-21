// controllers/videoController.ts
import { AxiosProgressEvent } from "axios";
import ErrorResponse from "../models/errorResponse";
import Video from "../models/video";
import { uploadVideoService } from "../services/uploadVideoService";


export const uploadVideoController = async (
  formData: FormData,
  uploadPercentageRef: React.MutableRefObject<number>
): Promise<Video> => {
  const onUploadProgress = (progressEvent: AxiosProgressEvent) => {
    uploadPercentageRef.current = Math.round(
      (progressEvent.loaded * 100) / progressEvent.total!
    );
  };

  try {
    return await uploadVideoService(formData, onUploadProgress);
  } catch (error) {
    throw error as ErrorResponse; // Ensure the error is typed as ErrorResponse
  }
};

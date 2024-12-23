// controllers/videoController.ts
import { AxiosProgressEvent } from "axios";
import ErrorResponse from "../models/errorResponse";
import Video from "../models/video";
import { uploadVideoService } from "../services/uploadVideoService";
import { Dispatch } from "react";


export const uploadVideoController = async (
  formData: FormData,
  setPercentage: Dispatch<React.SetStateAction<number>>
): Promise<Video> => {
  const onUploadProgress = (progressEvent: AxiosProgressEvent) => {
    const percentage = Math.round(
      (progressEvent.loaded * 100) / progressEvent.total!
    );
    setPercentage(percentage);
  };

  try {
    return await uploadVideoService(formData, onUploadProgress);
  } catch (error) {
    throw error as ErrorResponse; // Ensure the error is typed as ErrorResponse
  }
};

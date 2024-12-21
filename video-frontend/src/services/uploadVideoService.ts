// services/videoService.ts
import axios, { AxiosError, AxiosProgressEvent, AxiosResponse } from "axios";
import Video from "../models/video";
import { url } from "../constants";
import ErrorResponse from "../models/errorResponse";

export const uploadVideoService = async (
  formData: FormData,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void
): Promise<Video> => {
  try {
    const response: AxiosResponse<Video> = await axios.post(
      `${url}/save`,
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
        onUploadProgress: onUploadProgress,
      }
    );
    return response.data as Video;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const axiosError = error as AxiosError;
      const errorData: ErrorResponse = (axiosError.response
        ?.data as ErrorResponse) || {
        status: "error",
        message: "An unexpected error occurred",
      };
      throw errorData;
    }
    const errorResponse: ErrorResponse = {
      status: "Upload Failed",
      message: "An unexpected error occurred",
    };
    throw  errorResponse;
  }
};

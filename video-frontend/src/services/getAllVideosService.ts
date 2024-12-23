import axios, { AxiosError } from "axios";
import { url } from "../constants";
import ErrorResponse from "../models/errorResponse";
import Video from "../models/video";

async function getAllVideosService():Promise<Video[]> {
  try {
    const response = await axios.get(`${url}`, {
      headers: { "Content-Type": "application/json" },
    });
      return response.data as Video[];
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
      status: "Failed",
      message: "An unexpected error occurred",
    };
    throw errorResponse;
  }
}
export default getAllVideosService;

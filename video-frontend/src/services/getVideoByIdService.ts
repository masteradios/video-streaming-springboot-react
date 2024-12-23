import axios, { AxiosError } from "axios";
import { url } from "../constants";
import Video from "../models/video";
import ErrorResponse from "../models/errorResponse";

async function getVideoByIdService(id: string):Promise<Video> {
    try { 
        const res = await axios.get(`${url}/video/${id}`, {
          headers: { "Content-Type": "application/json" },
        });
        return res.data as Video;
    }
    catch (error) {
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
export default getVideoByIdService;

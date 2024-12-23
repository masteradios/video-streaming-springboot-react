import { useLocation, useParams } from "react-router-dom";
import { url } from "../../constants";
import Video from "../../models/video";
import { useEffect, useState } from "react";
import getVideoByIdController from "../../controllers/getVideoByIdController";
import { toast } from "react-toastify";
import ErrorResponse from "../../models/errorResponse";
function SingleVideoScreen() {
  const { id } = useParams(); // Get the video id from the URL
  const location = useLocation();
  //const video: Video | null = location.state?.video;
  const [video, setVideo] = useState<Video | null>(location.state?.video);
  const [isLoading, setIsLoading] = useState(false);
  const getVideoById = async () => {
    setIsLoading(true);
    try {
      const video: Video = await getVideoByIdController(id!);
      setVideo(video);
    } catch (error) {
      const err = error as ErrorResponse;
      toast.error(err.message || "Upload failed");
    }
    setIsLoading(false);
  };
  useEffect(() => {
    if (!video) {
      getVideoById();
    }
  }, [video]);

  return isLoading ? (
    <div className="flex justify-center items-center h-screen w-screen">Loading</div>
  ) : (
    video && (
      <div className=" flex flex-col items-center  bg-slate-300">
        <video
          className="border border-spacing-5 border-gray-600  h-[340px] w-full "
          src={`${url}/stream/range/${id}`}
          controls
        ></video>

        <div className="flex flex-col text-black w-full px-3 py-2 bg-white flex-grow font-bold overflow-hidden">
          <h2 className="break-words pb-3 text-2xl">{video!.title}</h2>
          <p className="text-sm break-words">{video!.description}</p>
        </div>
      </div>
    )
  );
}
export default SingleVideoScreen;

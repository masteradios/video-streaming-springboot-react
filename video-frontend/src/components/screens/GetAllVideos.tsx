import { List } from "flowbite-react";
import Video from "../../models/video";
import { url } from "../../constants";
import { useEffect, useState } from "react";
import ErrorResponse from "../../models/errorResponse";
import { toast, ToastContainer } from "react-toastify";
import getAllVideosController from "../../controllers/getAllVideosController";
import { Link } from "react-router-dom";

function GetAllVideosScreen() {
  const [videos, setVideos] = useState<Video[] | null>(null);

  useEffect(() => {
    getAllVideos();
  }, []);

  async function getAllVideos() {
    try {
      const videosList = await getAllVideosController();
      setVideos(videosList);
    } catch (error) {
      const err = error as ErrorResponse;
      toast.error(err.message || " An error Occurred");
    }
  }

  return (
    <div className="bg-slate-800 h-screen overflow-auto w-screen text-white flex flex-col px-4 py-2 space-y-3">
      {videos &&
        videos.map((video) => {
          return <VideoTile video={video} />;
        })}
    </div>
  );
}

function VideoTile({ video }: { video: Video }) {
  return (
    <Link to={`/video/${video.id}`} state={{ video }}>
      <div className=" bg-gray-300 rounded-lg flex overflow: hidden, white-space: nowrap, and text-overflow: ellipsis">
        <img
          className="h-[140px] w-[220px] rounded-tl-lg rounded-bl-lg flex-shrink-0"
          src={`${url}/getThumbnail/${video.id}`}
          alt=""
        />
        <div className="flex flex-col align-top px-3 py-2 overflow-hidden">
          <h2 className="text-black font-semibold text-xl whitespace-nowrap overflow-hidden text-ellipsis">
            {video.title}
          </h2>
        </div>
      </div>
    </Link>
  );
}

export default GetAllVideosScreen;

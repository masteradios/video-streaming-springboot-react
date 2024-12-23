import { ChangeEvent, useRef, useState } from "react";
import { Bounce, toast, ToastContainer } from "react-toastify";
import Video from "../../models/video";
import ErrorResponse from "../../models/errorResponse";
import UploadForm from "../UploadForm";
import { uploadVideoController } from "../../controllers/uploadVideoController";
import { Link } from "react-router-dom";
import { url } from "../../constants";

function AddaVideoScreen() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [savedVideo, setSavedVideo] = useState<Video | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const fileRef = useRef<HTMLInputElement | null>(null);
  const [percentage, setPercentage] = useState(0);
  const handleDescription = (e: ChangeEvent<HTMLInputElement>) => {
    setDescription(e.target.value);
  };

  const handleTitle = (e: ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const handleFormSubmit = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (title == "") {
      toast.error("enter a Title!!");
    }
    if (description == "") {
      toast.error("Enter Description!!");
    }
    if (!file) {
      toast.error("Select a Video!");
    }

    if (title != "" && description != "" && file) {
      setIsLoading(true);
      try {
        const formData = new FormData();
        formData.append("title", title);
        formData.append("description", description);
        formData.append("file", file);
        const video: Video = await uploadVideoController(
          formData,
          setPercentage
        );
        setSavedVideo(video);
      } catch (error) {
        const err = error as ErrorResponse;
        toast.error(err.message || "Upload failed");
      }

      setIsLoading(false);
      resetForm();
    }
  };

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files ? e.target.files[0] : null;
    setFile(selectedFile);
  };

  const resetForm = () => {
    setTitle("");
    setDescription("");
    if (fileRef.current) {
      fileRef.current.value = ""; // Reset the input value to null
    }
    setFile(null);
  };

  return (
    <div
      className={`h-screen flex flex-col items-center ${
        savedVideo ? "justify-between" : "justify-center"
      }  w-full px-5 py-4`}
    >
      <Link
        className="place-self-end rounded-md bg-black text-white py-1 px-3 text-xl"
        to={"/getAllVideos"}
      >
        <button>Get All Videos</button>
      </Link>
      <div className={`my-2 flex align-top ${savedVideo?"justify-between":"justify-center"} m-0  space-x-4 w-full h-full`}>
        {savedVideo && (
          <video
            src={`${url}/stream/range/${savedVideo.id}`}
            className="w-[600px] h-[300px] object-cover border rounded-lg shadow-lg flex-shrink-0"
            controls // Optional: Adds video controls
          ></video>
        )}
        <div className="h-full flex flex-col shadow-lg rounded-md bg-slate-400 p-5 gap-y-2 items-center justify-center w-2/5">
          <h1 className="text-black font-bold text-2xl mb-2">
            Video Streaming App
          </h1>
          <UploadForm
            title={title}
            handleTitle={handleTitle}
            description={description}
            handleDescription={handleDescription}
            fileRef={fileRef}
            handleFileChange={handleFileChange}
            isLoading={isLoading}
            percentage={percentage}
            handleFormSubmit={handleFormSubmit}
          />
        </div>
      </div>

      <ToastContainer
        transition={Bounce}
        className="text-center"
        position="top-center"
        autoClose={3000} // Automatically close after 3 seconds
        hideProgressBar={true} // Disable the progress bar
        newestOnTop={true} // Show new toast on top
        closeOnClick={true} // Close on click
        draggable={false} // Disable dragging
        pauseOnHover={true} // Pause on hover
        theme="colored" // Colored theme
      />
    </div>
  );
}

export default AddaVideoScreen;

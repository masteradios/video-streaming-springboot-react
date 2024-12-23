import { BrowserRouter, Routes,Route } from "react-router-dom";
import AddaVideoScreen from "./components/screens/AddaVideoScreen";
import GetAllVideosScreen from "./components/screens/GetAllVideos";
import SingleVideoScreen from "./components/screens/SingleVideoScreen";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AddaVideoScreen />} />
        <Route path="/getAllVideos" element={<GetAllVideosScreen />} />
        <Route path="/video/:id" element={<SingleVideoScreen />} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;

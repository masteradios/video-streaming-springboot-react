import { FileInput, Label } from "flowbite-react";

type FileInputComponentProps = {
  fileRef: React.MutableRefObject<HTMLInputElement | null>,
  onChange: React.ChangeEventHandler<HTMLInputElement>;
};
function FileInputComponent({ fileRef, onChange }: FileInputComponentProps) {
  return (
    <div className="w-full">
      <div className="mb-2">
        <Label
          className="text-black"
          htmlFor="file-upload"
          value="Upload file"
        />
      </div>
      <FileInput
        ref={fileRef}
        onChange={onChange}
        accept="video/*"
        id="file-upload"
        className="w-full my-3"
      />
    </div>
  );
}
export default FileInputComponent;

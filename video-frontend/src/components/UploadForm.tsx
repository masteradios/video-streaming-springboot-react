import ButtonComponent from "./ButtonComponent";
import FileInputComponent from "./FileInputComponent";
import ProgressComponent from "./ProgressComponent";
import { ChangeEventHandler, MouseEventHandler, MutableRefObject } from "react";
import TextInput from "./TextInput";

type UploadFormProps = {
  title: string;
  handleTitle: ChangeEventHandler<HTMLInputElement>;
  description: string;
  handleDescription: ChangeEventHandler<HTMLInputElement>;
  fileRef: MutableRefObject<HTMLInputElement | null>;
  handleFileChange: ChangeEventHandler<HTMLInputElement>;
  isLoading: boolean;
  percentage: number;
  handleFormSubmit: MouseEventHandler<HTMLButtonElement>;
};

function UploadForm({
  title,
  handleTitle,
  description,
  handleDescription,
  fileRef,
  handleFileChange,
  isLoading,
  percentage,
  handleFormSubmit,
}: UploadFormProps) {
  return (
    <form className="w-full h-fit flex flex-col justify-center">
      <h2 className="text-black mb-2">Enter Video Title</h2>

      <TextInput text="Title" value={title} onChange={handleTitle} />
      <h2 className="text-black m-2">Enter Video Description</h2>
      <div className="flex-col w-full">
        <TextInput
          text="Description"
          value={description}
          onChange={handleDescription}
          maxLength={255}
        />
        <h1 className="text-sm text-right self-end">
          {description.length}/{255}
        </h1>
      </div>

      <FileInputComponent fileRef={fileRef} onChange={handleFileChange} />
      {isLoading && <ProgressComponent percentage={percentage} />}

      <ButtonComponent buttonTitle="Upload" onClick={handleFormSubmit} />
    </form>
  );
}
export default UploadForm;

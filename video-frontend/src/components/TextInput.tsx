import { ChangeEventHandler } from "react";

type TextInputProps = {
  text: string,
  value:string,
  onChange: ChangeEventHandler<HTMLInputElement>,
  maxLength?:number
};

function TextInput({text,value,onChange,maxLength}:TextInputProps) {
    return (
      <input
        placeholder={`Enter ${text}...`}
        type="text"
        value={value}
        maxLength={maxLength}
        onChange={onChange}
        className="w-full focus:outline-none rounded-md p-2"
      />
    );
 }

export default TextInput;
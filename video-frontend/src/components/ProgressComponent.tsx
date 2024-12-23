import { Progress } from "flowbite-react";


type ProgressComponentProps = {
  percentage:number
};
function ProgressComponent({ percentage }: ProgressComponentProps) {
  return (
    <div className="my-3 ">
      <Progress
        className="w-full text-center"
        progress={percentage}
        progressLabelPosition="inside"
        textLabelPosition="outside"
        size="sm"
        color="blue"
      />
    </div>
  );
}
export default ProgressComponent;

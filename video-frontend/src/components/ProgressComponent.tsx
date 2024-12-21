import { Progress } from "flowbite-react";


type ProgressComponentProps = {
  percentageRef: React.MutableRefObject<number>;
};
function ProgressComponent({ percentageRef }: ProgressComponentProps) {
  return (
    <div className="my-3 ">
      <Progress
        className="w-full text-center"
        progress={percentageRef.current}
        progressLabelPosition="inside"
        textLabelPosition="outside"
        size="sm"
        color="blue"
      />
    </div>
  );
}
export default ProgressComponent;

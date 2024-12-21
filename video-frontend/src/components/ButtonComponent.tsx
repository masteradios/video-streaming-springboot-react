

type ButtonComponentProps = {
  buttonTitle:string,
  onClick: React.MouseEventHandler<HTMLButtonElement>;
};
function ButtonComponent({buttonTitle,onClick}:ButtonComponentProps) {
  return (
    <button
      onClick={onClick}
      className="rounded-md bg-black text-white p-3 text-center"
    >
      {buttonTitle}
    </button>
  );
}
export default ButtonComponent;
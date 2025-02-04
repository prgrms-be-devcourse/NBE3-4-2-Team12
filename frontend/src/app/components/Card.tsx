type CardProps = {
  title: string;
  categoryNames: string;
  status: string;
};

const categoryColors: Record<string, string> = {
  self_development: "bg-blue-200",
  hobby: "bg-yellow-200",
  exercise: "bg-purple-200",
};

const statusMapping: Record<string, string> = {
  RECRUITING: "모집중",
  CLOSED: "마감",
};

const statusColors: Record<string, string> = {
  모집중: "bg-green-200",
  마감: "bg-red-200",
};

const categoryMapping: Record<string, string> = {
  self_development: "자기 개발",
  hobby: "취미",
  exercise: "운동",
};

const Card = ({ title, categoryNames, status }: CardProps) => {
  console.log("Category:", categoryNames);
  const displayedStatus = statusMapping[status] || status;
  const displayedCategory = categoryMapping[categoryNames] || categoryNames;

  return (
    <div className="border p-4 rounded shadow-md w-64 bg-white flex flex-col justify-between">
      {/* 제목 */}
      <h2 className="text-md font-semibold">{title}</h2>

      {/* 카테고리 */}
      <span
        className={`text-sm px-3 py-1 rounded-md mt-2 inline-block w-fit ${
          categoryColors[categoryNames] || "bg-gray-200"
        }`}
      >
        {displayedCategory}
      </span>

      {/* 하단: 모집 상태 & 참가 버튼 */}
      <div className="flex justify-between items-center mt-4">
        {/* 모집 상태 */}
        <span
          className={`text-sm px-3 py-1 rounded-md ${
            statusColors[displayedStatus] || "bg-gray-200"
          }`}
        >
          {displayedStatus}
        </span>

        {/* 참가하기 버튼 */}
        <button className="bg-gray-600 text-white px-4 py-1 rounded-md text-sm">
          참가하기
        </button>
      </div>
    </div>
  );
};

export default Card;

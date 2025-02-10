"use client";

import { useRouter } from "next/navigation";
import {joinGroup} from "@/app/api";

type Category = {
  id: number;
  type: string;
  name: string;
};

type CardProps = {
  id: string;
  title: string;
  category: Category[];
  status: string;
};

const categoryColors: Record<string, string> = {
  STUDY: "bg-blue-200",
  HOBBY: "bg-yellow-200",
  EXERCISE: "bg-purple-200",
};

const statusMapping: Record<string, string> = {
  RECRUITING: "모집중",
  COMPLETED: "마감",
};

const statusColors: Record<string, string> = {
  모집중: "bg-green-200",
  마감: "bg-red-200",
};

const categoryMapping: Record<string, string> = {
  STUDY: "자기 개발",
  HOBBY: "취미",
  EXERCISE: "운동",
};

const Card = ({ id, title, category, status }: CardProps) => {
  const router = useRouter();
  const displayedStatus = statusMapping[status] || status;
  const displayedCategory =
    category.length > 0
      ? categoryMapping[category[0].name] || category[0].name
      : "미정"; // 첫 번째 카테고리만 표시, 기본값 "미정"

  const handleJoin = async (event: React.MouseEvent) => {
    event.stopPropagation();
    try {
      await joinGroup(Number(id));
      alert("그룹 참가 성공!");
      router.push("/");
    } catch (error: any) {
      const errorMessage = error?.response?.data?.message || "그룹 참가에 실패했습니다.";
      alert(errorMessage);
    }
  }

  return (
    <div
      className="border p-4 rounded shadow-md w-64 bg-white flex flex-col justify-between"
      onClick={()=>router.push(`/groups/${id}`)}
    >
      {/* 제목 */}
      <h2 className="text-md font-semibold">{title}</h2>

      {/* 카테고리 */}
      <span
        className={`text-sm px-3 py-1 rounded-md mt-2 inline-block w-fit ${
          categoryColors[displayedCategory] || "bg-gray-200"
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
        <button
            onClick={handleJoin}
            className="bg-gray-600 text-white px-4 py-1 rounded-md text-sm">
          참가하기
        </button>
      </div>
    </div>
  );
};

export default Card;

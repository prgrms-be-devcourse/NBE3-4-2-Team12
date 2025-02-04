import MainMenu from "./components/MainMenu";
import CardList from "./components/CardList";

const API_BASE_URL = "http://localhost:8080";

// ✅ 서버에서 직접 API 호출
const getGroups = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/groups`, {
      cache: "no-store", // 최신 데이터 가져오기
    });
    if (!response.ok) throw new Error("Failed to fetch groups");
    return await response.json();
  } catch (error) {
    console.error("Error fetching groups:", error);
    return []; // 오류 발생 시 빈 배열 반환
  }
};

export default async function Home() {
  const groups = await getGroups(); // 서버에서 데이터 가져오기

  return (
    <div className="min-h-screen bg-gray-50">
      <MainMenu />
      <CardList groups={groups} />
    </div>
  );
}

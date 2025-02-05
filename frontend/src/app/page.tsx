import MainMenu from "./components/MainMenu";
import CardList from "./components/CardList";
import { getGroups } from "./api/group";

export default async function Home() {
  const groups = await getGroups(); // 서버에서 데이터 가져오기

  return (
    <div className="min-h-screen bg-gray-50">
      <MainMenu />
      <CardList groups={groups} />
    </div>
  );
}

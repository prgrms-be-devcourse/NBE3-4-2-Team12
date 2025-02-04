import Card from "./Card";

type CardListProps = {
  groups: { title: string; categoryNames: string; status: string }[];
};

const CardList = ({ groups }: CardListProps) => {
  console.log("📌 groups 데이터:", groups);
  if (!groups || groups.length === 0) {
    return <p className="text-center text-gray-500">등록된 그룹이 없습니다.</p>;
  }

  return (
    <div className="flex justify-center">
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 p-4 max-w-6xl w-full">
        {groups.map((group, index) => (
          <Card key={index} {...group} />
        ))}
      </div>
    </div>
  );
};

export default CardList;

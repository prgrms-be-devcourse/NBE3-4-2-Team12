import Card from "./Card";

type CardListProps = {
  groups: {
    id: string;
    title: string;
    category: { id: number; type: string; name: string }[];
    status: string;
  }[];
};

const CardList = ({ groups }: CardListProps) => {
  if (!groups || groups.length === 0) {
    return <p className="text-center text-gray-500">등록된 그룹이 없습니다.</p>;
  }

  return (
    <div className="flex justify-center">
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 p-4 max-w-6xl w-full">
        {groups.map((group) => (
          <Card key={group.id} {...group} />
        ))}
      </div>
    </div>
  );
};

export default CardList;

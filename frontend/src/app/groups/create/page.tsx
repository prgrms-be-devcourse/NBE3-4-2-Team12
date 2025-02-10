"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { getCategories } from "@/app/api/categories";
import { createGroup } from "@/app/api/group";
import MainMenu from "@/app/components/MainMenu";
// votemodal 추가
import VoteModal from "@/app/components/VoteModal";


type Category = {
    id: number;
    type: string;
    name: string;
};

type LocationVote = {
    name: string;
};

export default function CreateGroupPage() {
    const router = useRouter();
    const [title, setTitle] = useState("");
    const [categories, setCategories] = useState<Category[]>([]);
    const [selectedCategories, setSelectedCategories] = useState<Category[]>([]);
    const [categoryIds, setCategoryIds] = useState<number[]>([]);
    const [maxParticipants, setMaxParticipants] = useState<number | "">("");
    const [description, setDescription] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

    //  모달 관련 상태 추가
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newLocation, setNewLocation] = useState("");
    const [locations, setLocations] = useState<LocationVote[]>([]);

    useEffect(() => {
        async function fetchCategories() {
            try {
                const data = await getCategories();
                setCategories(data);
            } catch (error) {
                console.error("카테고리 데이터를 불러오는 중 오류 발생:", error);
            }
        }
        fetchCategories();
    }, []);

    const categoryTypes = [...new Set(categories.map((c) => c.type))];

    const filteredCategories = (type: string) =>
        categories.filter((c) => c.type === type);

    const handleCategorySelect = (category: Category) => {
        if (!selectedCategories.find((c) => c.id === category.id)) {
            setSelectedCategories([...selectedCategories, category]);
            setCategoryIds([...categoryIds, category.id]);
        }
    };

    const handleCategoryRemove = (categoryId: number) => {
        setSelectedCategories(selectedCategories.filter((c) => c.id !== categoryId));
        setCategoryIds(categoryIds.filter((id) => id !== categoryId));
    };

    // 장소 추가 기능
    const handleAddLocation = () => {
        if (newLocation.trim() !== "" && !locations.some(loc => loc.name === newLocation)) {
            setLocations([...locations, { name: newLocation }]);
            setNewLocation("");
        }
    };

    //  장소 삭제 기능
    const handleDeleteLocation = (index: number) => {
        setLocations(locations.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!title.trim() || categoryIds.length === 0 || !maxParticipants) {
            setErrorMessage("모든 필수 항목을 입력하세요.");
            return;
        }

        setLoading(true);
        setErrorMessage("");

        const requestData = {
            title,
            description,
            maxParticipants: Number(maxParticipants),
            categoryIds,
            locations: locations.map(loc => loc.name), // 추가된 장소 포함
            status: "RECRUITING",
        };

        try {
            const response = await createGroup(requestData);
            console.log("모임 생성 성공:", response);
            alert("모임이 성공적으로 생성되었습니다!");
            router.push("/");
        } catch (error) {
            setErrorMessage("모임 생성 중 오류가 발생했습니다. 다시 시도해주세요.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <MainMenu />
            <div className="max-w-4xl mx-auto bg-white p-10 mt-10 rounded-lg shadow-lg">
                <form className="space-y-6" onSubmit={handleSubmit}>
                    {/*  제목 입력 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">제목</label>
                        <input
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            placeholder="제목을 입력하세요."
                        />
                    </div>

                    {/* 모집 구분 선택 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">모집 구분</label>
                        <select
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            onChange={(e) => {
                                const selectedType = e.target.value;
                                if (!selectedType) return;

                                // 중복 추가 방지
                                const newCategories = filteredCategories(selectedType).filter(
                                    (c) => !categories.some((existing) => existing.id === c.id)
                                );

                                setCategories((prev) => [...prev, ...newCategories]);
                            }}
                        >
                            <option value="">선택하세요</option>
                            {categoryTypes.map((type, index) => (
                                <option key={`type-${index}`} value={type}>
                                    {type}
                                </option>
                            ))}
                        </select>
                    </div>

                    {/* 모집 분야 선택 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">모집 분야</label>
                        <select
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            onChange={(e) => {
                                const selectedCategory = categories.find(
                                    (c) => c.name === e.target.value
                                );
                                if (selectedCategory && !selectedCategories.some((c) => c.id === selectedCategory.id)) {
                                    setSelectedCategories([...selectedCategories, selectedCategory]);
                                    setCategoryIds([...categoryIds, selectedCategory.id]);
                                }
                            }}
                        >
                            <option value="">모집 분야 선택</option>
                            {categories.map((category) => (
                                <option key={`category-${category.id}-${category.type}`} value={category.name}>
                                    {category.name}
                                </option>
                            ))}
                        </select>

                        {/* 선택된 모집 분야를 라벨로 표시 */}
                        <div className="mt-3 flex flex-wrap gap-2">
                            {selectedCategories.map((category) => (
                                <span key={`selected-${category.id}-${category.type}`} className="bg-blue-100 text-blue-800 text-sm font-semibold px-3 py-1 rounded-full">
                        {category.type} - {category.name}
                                    <button
                                        onClick={() => handleCategoryRemove(category.id)}
                                        className="ml-2 text-red-600 font-bold"
                                    >
                            ✕
                        </button>
                    </span>
                            ))}
                        </div>
                    </div>

                    {/* 모집 인원 설정 (숫자만 입력 가능) */}
                    <div className="flex items-center space-x-2">
                        <label className="block text-gray-700 font-semibold">최대 인원</label>
                        <div className="flex items-center border border-gray-300 rounded-md">
                            {/* 감소 버튼 */}
                            <button
                                type="button"
                                onClick={() => setMaxParticipants((prev) => Math.max(1, Number(prev) - 1))}
                                className="px-3 py-2 bg-gray-200 hover:bg-gray-300 rounded-l-md"
                            >
                                -
                            </button>

                            {/* 숫자 입력 (기본 버튼 제거 + 문자 입력 차단) */}
                            <input
                                type="number" // 🔥 "number" 대신 "text"로 변경 (문자 강제 차단)
                                value={maxParticipants}
                                onChange={(e) => {
                                    const value = e.target.value;
                                    const numValue = Number(value);
                                    // 최소값 1 이상으로 설정
                                    if (value === "" || (!isNaN(numValue) && numValue > 0)) {
                                        setMaxParticipants(value === "" ? "" : numValue);
                                    }
                                }}
                                className="w-16 px-2 text-center border-none focus:outline-none"
                                inputMode="numeric"
                                min={1}
                                style={{
                                    appearance: "none", // 기본 UI 제거
                                    MozAppearance: "textfield", // 파이어폭스 대응
                                }}
                            />

                            {/* 증가 버튼 */}
                            <button
                                type="button"
                                onClick={() => setMaxParticipants((prev) => Number(prev) + 1)}
                                className="px-3 py-2 bg-gray-200 hover:bg-gray-300 rounded-r-md"
                            >
                                +
                            </button>
                        </div>
                    </div>

                    <style>
                        {`
  /* 기본 숫자 증감 버튼 완전히 숨김 */
  input[type="number"]::-webkit-outer-spin-button,
  input[type="number"]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
`}
                    </style>


                    {/*  장소 투표 생성 버튼 */}
                        <button
                            type="button"
                            onClick={() => setIsModalOpen(true)}
                            className="bg-gray-700 text-white px-4 py-2 rounded-md"
                        >
                            장소 투표 생성
                        </button>

                    {/*  장소 투표 모달 VoteModal component로 대체*/}
                    <VoteModal
                        isOpen={isModalOpen}
                        onClose={() => setIsModalOpen(false)}
                        onSubmit={(locations) => {
                            // 투표 장소들을 처리하는 로직
                            console.log('Selected locations:', locations);
                            setIsModalOpen(false);
                        }}
                    />

                        {/*  내용 입력 칸 */}
                        <div>
                            <label className="block text-gray-700 font-semibold">내용</label>
                            <textarea
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                className="w-full px-4 py-2 border border-gray-300 rounded-md h-32"
                                placeholder="내용을 입력하세요."
                            ></textarea>
                        </div>

                        {/*  오류 메시지 */}
                        {errorMessage && <p className="text-red-500">{errorMessage}</p>}

                        {/*  "돌아가기 / 등록" 버튼 추가 (하단 정렬) */}
                        <div className="flex justify-end space-x-4 mt-6">
                            <button
                                type="button"
                                onClick={() => router.back()}
                                className="bg-gray-300 text-gray-700 px-6 py-2 rounded-md"
                            >
                                돌아가기
                            </button>
                            <button
                                type="submit"
                                className="bg-green-500 text-white px-6 py-2 rounded-md"
                                disabled={loading}
                            >
                                {loading ? "등록 중..." : "등록"}
                            </button>
                        </div>
                </form>
            </div>
        </div>
);
}

"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { getCategories } from "@/app/api/categories";
import { createGroup } from "@/app/api/group";
import MainMenu from "@/app/components/MainMenu";

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
            router.push("/groups");
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

                    {/* 모집 구분 / 모집 분야 선택 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">모집 구분</label>
                        <select
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            onChange={(e) => {
                                const selectedType = e.target.value;
                                if (selectedType) {
                                    const filtered = filteredCategories(selectedType);
                                    if (filtered.length > 0) {
                                        handleCategorySelect(filtered[0]);
                                    }
                                }
                            }}
                        >
                            <option value="">선택하세요</option>
                            {categoryTypes.map((type, index) => (
                                <option key={index} value={type}>
                                    {type}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div>
                        <label className="block text-gray-700 font-semibold">모집 분야</label>
                        <select
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            onChange={(e) => {
                                const selectedCategory = categories.find(
                                    (c) => c.name === e.target.value
                                );
                                if (selectedCategory) {
                                    handleCategorySelect(selectedCategory);
                                }
                            }}
                        >
                            <option value="">모집 분야 선택</option>
                            {categories.map((category) => (
                                <option key={category.id} value={category.name}>
                                    {category.name}
                                </option>
                            ))}
                        </select>

                        <div className="mt-3 flex flex-wrap gap-2">
                            {selectedCategories.map((category) => (
                                <span key={category.id} className="bg-blue-100 text-blue-800 text-sm font-semibold px-3 py-1 rounded-full">
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

                    {/*  모집 인원 설정 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">최대 인원</label>
                        <input
                            type="number"
                            value={maxParticipants}
                            onChange={(e) => setMaxParticipants(Number(e.target.value) || "")}
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            min="1"
                        />
                    </div>

                    {/*  장소 투표 생성 버튼 */}
                    <button
                        type="button"
                        onClick={() => setIsModalOpen(true)}
                        className="bg-gray-700 text-white px-4 py-2 rounded-md"
                    >
                        장소 투표 생성
                    </button>

                    {/*  장소 투표 모달 */}
                    {isModalOpen && (
                        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
                            <div className="bg-white p-6 rounded-lg shadow-lg w-96">
                                <h3 className="text-lg font-bold mb-4">장소 추가</h3>
                                <input
                                    type="text"
                                    value={newLocation}
                                    onChange={(e) => setNewLocation(e.target.value)}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-md"
                                    placeholder="장소 입력"
                                />
                                <button onClick={handleAddLocation} className="mt-2 bg-blue-500 text-white px-4 py-2 rounded-md">추가</button>
                                <ul className="mt-4">
                                    {locations.map((loc, index) => (
                                        <li key={index} className="flex justify-between items-center bg-gray-200 p-2 rounded-md">
                                            {loc.name}
                                            <button onClick={() => handleDeleteLocation(index)} className="text-red-500 font-bold">✕</button>
                                        </li>
                                    ))}
                                </ul>
                                <button onClick={() => setIsModalOpen(false)} className="mt-4 bg-gray-500 text-white px-4 py-2 rounded-md">닫기</button>
                            </div>
                        </div>
                    )}

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
                            onClick={() => router.push("/groups")}
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

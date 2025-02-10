"use client";

import {useEffect, useState} from "react";
import {addCategory, deleteCategory, getCategories, modifyCategory} from "@/app/api";

const CATEGORY_TYPES = ["EXERCISE", "STUDY", "HOBBY"];

const AdminCategoriesPage = () => {
    const [categories, setCategories] = useState<{ id: number; type: string; name: string }[]>([]);
    const [type, setType] = useState("EXERCISE");
    const [name, setName] = useState("");
    const [editCategoryId, setEditCategoryId] = useState<number | null>(null);
    const [editType, setEditType] = useState("EXERCISE");
    const [editName, setEditName] = useState("");
    const [loading, setLoading] = useState(true);

    // 🔹 카테고리 목록 불러오기
    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getCategories();
            setCategories(data);
            setLoading(false);
        };
        fetchCategories();
    }, []);

    // 🔹 카테고리 추가 핸들러
    const handleAddCategory = async () => {
        if (!name.trim()) {
            alert("카테고리 이름을 입력해주세요.");
            return;
        }

        const newCategory = await addCategory(type, name);
        if (newCategory) {
            setCategories([...categories, newCategory]); // UI에 반영
            setName(""); // 입력값 초기화
        }
    };

    // 🔹 카테고리 삭제 핸들러
    const handleDeleteCategory = async (categoryId: number) => {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        const success = await deleteCategory(categoryId);
        if (success) {
            setCategories(categories.filter((cat) => cat.id !== categoryId)); // UI에서 삭제 반영
        }
    };

    // 🔹 카테고리 수정 핸들러
    const handleEditCategory = async () => {
        if (!editName.trim()) {
            alert("수정할 카테고리 이름을 입력해주세요.");
            return;
        }

        const updatedCategory = await modifyCategory(editCategoryId!, editType, editName);
        if (updatedCategory) {
            setCategories(
                categories.map((category) =>
                    category.id === editCategoryId
                        ? {...category, type: editType, name: editName}
                        : category
                )
            );
            setEditCategoryId(null); // 수정 모드 종료
            setEditType("EXERCISE");
            setEditName(""); // 입력값 초기화
        }
    };

    if (loading) {
        return <div className="text-center mt-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-2xl mx-auto mt-12 p-8 bg-white rounded-xl shadow-xl">
            <h1 className="text-2xl font-bold text-center mb-6">카테고리 관리</h1>

            {/* 🔹 카테고리 추가 폼 */}
            <div className="flex gap-4 mb-6">
                <select
                    value={type}
                    onChange={(e) => setType(e.target.value)}
                    className="border px-4 py-2 rounded"
                >
                    {CATEGORY_TYPES.map((t) => (
                        <option key={t} value={t}>
                            {t}
                        </option>
                    ))}
                </select>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="카테고리 이름"
                    className="border px-4 py-2 rounded flex-1"
                />
                <button
                    onClick={handleAddCategory}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    추가
                </button>
            </div>

            {/* 🔹 카테고리 수정 폼 */}
            {editCategoryId !== null && (
                <div className="flex gap-4 mb-6">
                    <select
                        value={editType}
                        onChange={(e) => setEditType(e.target.value)}
                        className="border px-4 py-2 rounded"
                    >
                        {CATEGORY_TYPES.map((t) => (
                            <option key={t} value={t}>
                                {t}
                            </option>
                        ))}
                    </select>
                    <input
                        type="text"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                        placeholder="수정할 카테고리 이름"
                        className="border px-4 py-2 rounded flex-1"
                    />
                    <button
                        onClick={handleEditCategory}
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                    >
                        수정
                    </button>
                    <button
                        onClick={() => {
                            setEditCategoryId(null); // 수정 취소
                            setEditType("EXERCISE");
                            setEditName("");
                        }}
                        className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                    >
                        취소
                    </button>
                </div>
            )}

            {/* 🔹 카테고리 목록 */}
            <table className="w-full border-collapse border border-gray-300">
                <thead>
                <tr className="bg-gray-200">
                    <th className="border border-gray-300 px-4 py-2">유형</th>
                    <th className="border border-gray-300 px-4 py-2">이름</th>
                    <th className="border border-gray-300 px-4 py-2">수정</th>
                    <th className="border border-gray-300 px-4 py-2">삭제</th>
                </tr>
                </thead>
                <tbody>
                {categories.length === 0 ? (
                    <tr>
                        <td colSpan={4} className="text-center py-4">
                            등록된 카테고리가 없습니다.
                        </td>
                    </tr>
                ) : (
                    categories.map((category) => (
                        <tr key={category.id} className="text-center">
                            <td className="border border-gray-300 px-4 py-2">{category.type}</td>
                            <td className="border border-gray-300 px-4 py-2">{category.name}</td>
                            <td className="border border-gray-300 px-4 py-2">
                                <button
                                    onClick={() => {
                                        setEditCategoryId(category.id);
                                        setEditType(category.type);
                                        setEditName(category.name);
                                    }}
                                    className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                                >
                                    수정
                                </button>
                            </td>
                            <td className="border border-gray-300 px-4 py-2">
                                <button
                                    onClick={() => handleDeleteCategory(category.id)}
                                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                >
                                    삭제
                                </button>
                            </td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>
        </div>
    );
};

export default AdminCategoriesPage;

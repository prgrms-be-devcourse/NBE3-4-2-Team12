"use client";

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import {adminDeleteGroup, checkAdminAuth, getGroups} from "@/app/api";

const AdminGroupsPage = () => {
    const [admin, setAdmin] = useState(null);
    const [groups, setGroups] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        const fetchAdmin = async () => {
            const adminData = await checkAdminAuth();
            if (!adminData) {
                alert("관리자 로그인이 필요합니다.");
                router.push("/admin/login"); // 로그인 페이지로 이동
            } else {
                setAdmin(adminData);
            }
            setLoading(false);
        };

        fetchAdmin();
    }, []);

    // 그룹 목록 불러오기
    useEffect(() => {
        const fetchGroups = async () => {
            const data = await getGroups();
            setGroups(data);
            setLoading(false);
        };
        fetchGroups();
    }, []);

    // 그룹 삭제 핸들러
    const handleDeleteGroup = async (groupId: number) => {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        try {
            await adminDeleteGroup(groupId);
            alert("모임이 삭제되었습니다.");
            setGroups((prev) => prev.filter((group) => group.id !== groupId));
        } catch (error: any) {
            alert(error.message);
        }
    };

    if (loading) {
        return <div className="text-center mt-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto mt-12 p-8 bg-white rounded-xl shadow-xl">
            <h1 className="text-2xl font-bold text-center mb-6">모임 관리</h1>
            <table className="w-full border-collapse border border-gray-300">
                <thead>
                <tr className="bg-gray-200">
                    <th className="border border-gray-300 px-4 py-2">제목</th>
                    <th className="border border-gray-300 px-4 py-2">작성자</th>
                    <th className="border border-gray-300 px-4 py-2">상태</th>
                    <th className="border border-gray-300 px-4 py-2">생성일</th>
                    <th className="border border-gray-300 px-4 py-2">삭제</th>
                </tr>
                </thead>
                <tbody>
                {groups.length === 0 ? (
                    <tr>
                        <td colSpan={5} className="text-center py-4">등록된 모임이 없습니다.</td>
                    </tr>
                ) : (
                    groups.map((group) => (
                        <tr key={group.id} className="text-center">
                            <td className="border border-gray-300 px-4 py-2">{group.title}</td>
                            <td className="border border-gray-300 px-4 py-2">{group.author}</td>
                            <td className="border border-gray-300 px-4 py-2">{group.status}</td>
                            <td className="border border-gray-300 px-4 py-2">{new Date(group.createdAt).toISOString().split("T")[0]}</td>
                            <td className="border border-gray-300 px-4 py-2">
                                <button
                                    onClick={() => handleDeleteGroup(group.id)}
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

            <div className="text-center mt-6">
                <button
                    onClick={() => router.push("/admin")}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600"
                >
                    관리자 메인으로
                </button>
            </div>
        </div>
    );
};

export default AdminGroupsPage;

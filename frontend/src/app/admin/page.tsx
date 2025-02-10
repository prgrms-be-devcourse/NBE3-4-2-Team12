"use client";

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import {checkAdminAuth} from "@/app/api";

const AdminMainPage = () => {
    const [admin, setAdmin] = useState(null);
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

    if (loading) {
        return <div className="text-center mt-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-lg mx-auto mt-12 p-8 bg-white rounded-xl shadow-xl text-lg">
            <h1 className="text-2xl font-bold text-center mb-6">관리자 페이지</h1>
            <p className="text-center text-gray-700 mb-6">
                {admin?.name}님, 관리할 항목을 선택하세요.
            </p>

            <div className="space-y-4">
                <button
                    onClick={() => router.push("/admin/categories")}
                    className="w-full bg-blue-500 text-white py-3 rounded-lg text-lg hover:bg-blue-600"
                >
                    카테고리 관리
                </button>
                <button
                    onClick={() => router.push("/admin/groups")}
                    className="w-full bg-green-500 text-white py-3 rounded-lg text-lg hover:bg-green-600"
                >
                    모임 관리
                </button>
            </div>
        </div>
    );
};

export default AdminMainPage;
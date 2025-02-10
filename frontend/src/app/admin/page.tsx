"use client";

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import {adminLogout, checkAdminAuth} from "@/app/api";

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

    const handleLogout = async () => {
        const result = await adminLogout();
        if (result) {
            setAdmin(null); // 로그아웃 후 관리자 정보 초기화
            alert("로그아웃 되었습니다.");
            router.push("/admin/login"); // 로그인 페이지로 이동
        } else {
            alert("로그아웃 실패. 다시 시도해 주세요.");
        }
    };

    if (loading) {
        return <div className="text-center mt-10">로딩 중...</div>;
    }

    return (
        <div className="max-w-lg mx-auto mt-12 p-8 bg-white rounded-xl shadow-xl text-lg">
            <h1 className="text-2xl font-bold text-center mb-6">관리자 페이지</h1>
            <p className="text-center text-gray-700 mb-6">
                관리할 항목을 선택하세요.
            </p>

            <div className="space-y-4">
                <button
                    onClick={handleLogout}
                    className="w-full bg-red-500 text-white py-3 rounded-lg text-lg hover:bg-red-600"
                >
                    관리자 로그아웃
                </button>
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
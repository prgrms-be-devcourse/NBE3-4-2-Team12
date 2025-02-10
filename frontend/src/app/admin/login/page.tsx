"use client";

import React, {useState} from "react";
import {useRouter} from "next/navigation";
import {adminLogin} from "@/app/api";

const AdminLoginPage = () => {
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const router = useRouter();

    // 입력값 변경 핸들러
    const handleChange = (setter) => (e) => {
        setter(e.target.value);
    };

    // 로그인 처리
    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            await adminLogin({id, password});
            alert("관리자 로그인 성공!");
            router.push("/admin"); // 로그인 성공 시 대시보드로 이동
        } catch (err) {
            setError("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md p-8 bg-white rounded-xl shadow-xl">
                <h1 className="text-2xl font-bold text-center mb-6">관리자 로그인</h1>
                {error && <p className="text-red-500 text-center mb-4">{error}</p>}
                <form onSubmit={handleLogin}>
                    <div className="mb-4">
                        <label className="block text-gray-700 mb-2">아이디</label>
                        <input
                            type="text"
                            value={id}
                            onChange={handleChange(setId)}
                            className="w-full p-3 border rounded-lg"
                            placeholder="아이디를 입력하세요"
                            required
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block text-gray-700 mb-2">비밀번호</label>
                        <input
                            type="password"
                            value={password}
                            onChange={handleChange(setPassword)}
                            className="w-full p-3 border rounded-lg"
                            placeholder="비밀번호를 입력하세요"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        disabled={loading}
                        className={`w-full py-3 rounded-lg text-lg ${
                            loading ? "bg-gray-500" : "bg-blue-500"
                        } text-white hover:bg-blue-600`}
                    >
                        {loading ? "로그인 중..." : "로그인"}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AdminLoginPage;
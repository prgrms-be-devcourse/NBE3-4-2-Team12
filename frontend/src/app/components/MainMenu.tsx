"use client";

import Link from "next/link";
import {getCurrentUser, kakaoLogin, kakaoLogout} from "@/app/api";
import {useEffect, useState} from "react";

const MainMenu = () => {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const user = await getCurrentUser();
                if (user) {
                    setIsLoggedIn(true);
                }
            } catch (error) {
                setIsLoggedIn(false);
            }
        };

        checkLoginStatus();
    }, []);

    const handleKakaoLogin = async () => {
        kakaoLogin();
        setIsLoggedIn(true);
    };

    const handleKakaoLogout = async () => {
        kakaoLogout();
        setIsLoggedIn(false);
    };

    return (
        <nav className="flex justify-between items-center p-4 bg-gray-100 shadow-md">
            <Link href="/">
                <h1 className="text-lg font-bold">MOYODANG</h1>
            </Link>
            <div className="space-x-4">
                {isLoggedIn && (
                    <Link href="/groups/create">
                        <button className="bg-green-500 text-white px-4 py-2 rounded">
                            모임 만들기
                        </button>
                    </Link>
                )}
                {isLoggedIn && (
                    <Link href="/profile" className="text-gray-700">
                        내정보
                    </Link>
                )}
                {isLoggedIn ? (
                    <button onClick={handleKakaoLogout} className="text-gray-700">
                        로그아웃
                    </button>
                ) : (
                    <button onClick={handleKakaoLogin} className="text-gray-700">
                        로그인
                    </button>
                )}
            </div>
        </nav>
    );
};

export default MainMenu;

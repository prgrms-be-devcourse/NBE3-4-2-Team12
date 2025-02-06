"use client";

import Link from "next/link";
import { kakaoLogin } from "../api/auth";

const handleKakaoLogin = async () => {
  kakaoLogin();
};

const MainMenu = () => {
  return (
    <nav className="flex justify-between items-center p-4 bg-gray-100 shadow-md">
      <h1 className="text-lg font-bold">MOYODANG</h1>
      <div className="space-x-4">
        <button className="bg-green-500 text-white px-4 py-2 rounded">
          모임 만들기
        </button>
        <Link href="/profile" className="text-gray-700">
          내정보
        </Link>
        <Link href="/signup" className="text-gray-700">
          회원가입
        </Link>
        <button onClick={handleKakaoLogin} className="text-gray-700">
          로그인
        </button>
      </div>
    </nav>
  );
};

export default MainMenu;

import { api } from "./axiosInstance";


// 현재 로그인된 유저 정보 조회
export const getCurrentUser = async () => {
    try{
        const response = await api.get("/members");
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

//카카오 로그인
export const kakaoLogin = async () => {
    window.location.href = "http://localhost:8080/auth/kakao/login";
};

//카카오 로그아웃
export const kakaoLogout = async () => {
    window.location.href = "http://localhost:8080/auth/kakao/logout";
}

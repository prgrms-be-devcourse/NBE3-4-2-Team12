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
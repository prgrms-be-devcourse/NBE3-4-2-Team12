import {api} from "./axiosInstance"; // axios 인스턴스 import

// 프로필 수정
export async function updateUserProfile(requestData: any) {
    try {
        const response = await api.put("/members", requestData);
        return response.data;
    } catch (error) {
        console.error("프로필 수정 중 에러 발생:", error);
        throw error;
    }
}

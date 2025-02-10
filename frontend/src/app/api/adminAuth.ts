import {api} from "./axiosInstance";

// 관리자 로그인
export async function adminLogin(requestData: { id: string; password: string }) {
    try {
        const response = await api.post("/admin/login", requestData);
        return response.data;
    } catch (error) {
        console.error("관리자 로그인 중 오류 발생:", error);
        throw error;
    }
}

// 관리자 로그인 상태 확인
export async function checkAdminAuth() {
    try {
        const response = await api.get("/admin/info");
        return response.data; // 관리자 정보 반환
    } catch (error) {
        console.error("관리자 인증 확인 실패:", error);
        return null; // 인증 실패 시 null 반환
    }
}

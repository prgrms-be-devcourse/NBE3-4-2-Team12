import {api} from "./axiosInstance";

// 관리자 로그인
export async function adminLogin(requestData: { adminName: string; password: string }) {
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
        const response = await api.get("/admin");
        return response.data; // 관리자 인증 정보
    } catch (error) {
        console.error("관리자 인증 확인 실패:", error);
        return null; // 인증 실패 시 null 반환
    }
}

// 관리자 로그아웃
export async function adminLogout() {
    try {
        const response = await api.post("/admin/logout");
        return response.data; // 로그아웃 성공 여부
    } catch (error) {
        console.error("관리자 인증 확인 실패:", error);
        return null; // 인증 실패 시 null 반환
    }
}

// 관리자 권한 모임 삭제
export async function adminDeleteGroup(groupId: number): Promise<any> {
    try {
        const response = await api.delete(`/admin/group/${groupId}`);
        return response.data; // 삭제 성공 여부
    } catch (error) {
        console.error("모임 삭제 실패:", error);
        return null; // 실패 시 null 반환
    }
}

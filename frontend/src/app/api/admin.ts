import { api } from "./axiosInstance";

// 관리자 로그인
export const loginAdmin = async (adminName: string, password: string) => {
    try {
        const response = await api.post("/admin/login", { adminName, password });
        return response.data;
    } catch (error) {
        console.error("로그인 실패:", error);
        throw error;
    }
};

// 관리자 로그아웃
export const logoutAdmin = async () => {
    try {
        const response = await api.post("/admin/logout");
        return response.data;
    } catch (error) {
        console.error("로그아웃 실패:", error);
        throw error;
    }
};

// 게시물 삭제
export const deleteGroup = async (groupId: number) => {
    try {
        const response = await api.delete(`/admin/group/${groupId}`);
        return response.data;
    } catch (error) {
        console.error("그룹 삭제 실패:", error);
        throw error;
    }
};

// 카테고리 생성
export const createCategory = async (categoryRequestDto: CategoryRequestDto) => {
    try {
        const response = await api.post("/categories", categoryRequestDto);
        return response.data;
    } catch (error) {
        console.error("카테고리 생성 실패:", error);
        throw error;
    }
};

// 카테고리 수정
export const modifyCategory = async (id: number, categoryRequestDto: CategoryRequestDto) => {
    try {
        const response = await api.put(`/categories/${id}`, categoryRequestDto);
        return response.data;
    } catch (error) {
        console.error("카테고리 수정 실패:", error);
        throw error;
    }
};

// 카테고리 삭제
export const deleteCategory = async (id: number) => {
    try {
        const response = await api.delete(`/categories/${id}`);
        return response.data;
    } catch (error) {
        console.error("카테고리 삭제 실패:", error);
        throw error;
    }
};
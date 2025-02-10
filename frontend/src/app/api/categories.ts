import {api} from "@/app/api/axiosInstance";

//  백엔드에서 카테고리 목록 가져오는 API 함수$
export async function getCategories() {
    try {
        const response = await api.get("/categories");
        return response.data;
    } catch (error) {
        console.error("카테고리 데이터를 불러오는 중 오류 발생:", error);
        return [];
    }
}

// 새 카테고리 추가하기
export const addCategory = async (type: string, name: string) => {
    try {
        const response = await api.post("/categories", {type, name});
        return response.data;
    } catch (error) {
        console.error("카테고리 추가 실패:", error);
        return null;
    }
};

// 카테고리 수정하기
export const modifyCategory = async (id: number, type: string, name: string) => {
    try {
        const response = await api.put(`/categories/${id}`, {type, name});
        return response.data;
    } catch (error) {
        console.error("카테고리 수정 실패:", error);
        return null;
    }
};

// 카테고리 삭제하기
export const deleteCategory = async (categoryId: number) => {
    try {
        await api.delete(`/categories/${categoryId}`);
        return true;
    } catch (error) {
        console.error("카테고리 삭제 실패:", error);
        return false;
    }
};


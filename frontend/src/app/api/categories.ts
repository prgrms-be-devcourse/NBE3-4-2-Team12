import { api } from "@/app/api/axiosInstance";

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

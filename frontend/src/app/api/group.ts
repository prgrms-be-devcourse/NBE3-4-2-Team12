import { api } from "./axiosInstance";

// 그룹 목록 조회
export const getGroups = async () => {
    try{
        const response = await api.get("/groups");
        console.log("API response:", response.data);
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
};

// 특정 그룹 조회
export const getGroup = async (id: number) => {
    try{
        const response = await api.get(`/groups/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};
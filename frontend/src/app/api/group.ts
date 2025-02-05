import { api } from "./axiosInstance";

// 그룹 목록 조회
export const getGroups = async () => {
    try{
        const response = await api.get("/groups");
        if(response.status !== 200){
            console.error("API 호출 실패:", response.data);
            return [];
        }
        return response.data;
    } catch (error) {
        console.error(error);
        return [];
    }
};

// 특정 그룹 조회
export const getGroup = async (id: number) => {
    try{
        const response = await api.get(`/groups/${id}`,{
            headers:{
                "Content-Type":"application/json",
            }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

//그룹 참가
export const joinGroup = async () => {
    try{
        const response = await api.post(`/groups/join`,{
            headers:{
                "Content-Type":"application/json",
            }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};
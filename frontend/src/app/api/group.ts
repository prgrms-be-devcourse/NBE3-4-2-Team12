import { api } from "./axiosInstance";

//그룹 생성
export async function createGroup(requestData: any) {
    try {
        const response = await api.post("/groups", requestData);
        return response.data;
    } catch (error) {
        console.error("모임 생성 중 오류 발생:", error);
        throw error;
    }
}
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

//그룹 수정
export const updateGroup = async (id: number, updateData: any) => {
    try {
        const response = await api.put(`/groups/${id}`, updateData, {
            headers: {
                "Content-Type": "application/json",
            },
        });
        return response.data;
    } catch (error) {
        console.error("그룹 수정 중 오류 발생:", error);
        throw error;
    }
};

//그룹 삭제
export const deleteGroup = async (id: number) => {
    try{
        const response = await api.delete(`/groups/${id}`,{
            headers:{
                "Content-Type":"application/json",
            }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}
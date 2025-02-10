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
export const joinGroup = async (groupId : number) => {
    try{
        const response = await api.post(`/groups/join`, {
            groupId , // 요청 본문에 groupId 포함
        },{
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

// 로그인된 사용자가 참가 중인 모임 목록 조회
export const getUserGroups = async () => {
    try {
        const response = await api.get("/groups/member", {
            headers: {
                "Content-Type": "application/json",
            },
        });
        return response.data;
    } catch (error) {
        console.error("사용자 참가 중인 모임 목록 조회 중 오류 발생:", error);
        throw error;
    }
};
import {api} from "./axiosInstance";

export async function submitVote(groupId: number, voteId: number) {
    try {
        const response = await api.post(`/voters/${groupId}/${voteId}`);
        return response.data;
    } catch (error) {
        console.error("투표 진행 중 오류 발생:", error);
        throw error;
    }
}

export async function cancelVote(groupId: number, voteId: number) {
    try {
        const response = await api.delete(`/voters/${groupId}/${voteId}`);
        return response.data;
    } catch (error) {
        console.error("투표 취소 중 오류 발생:", error);
        throw error;
    }
}

export async function memberVoteStatus(groupId: number) {
    try {
        const response = await api.get(`/voters/group/${groupId}`);
        return response.data;
    } catch (error) {
        console.error("사용자 투표 상태 조회 중 오류 발생:", error);
        throw error;
    }
}
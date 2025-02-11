// api/vote.ts
import { api } from "./axiosInstance";

interface MostVotedLocation {
    address: string;
    latitude: number;
    longitude: number;
}

interface VoteResultDto {
    mostVotedLocations: MostVotedLocation[];
}

export const getMostVotedLocations = async (groupId: number): Promise<VoteResultDto> => {
    try {
        const response = await api.get(`/votes/groups/${groupId}/most-voted`);
        return response.data;
    } catch (error) {
        console.error("최다 투표 장소 조회 중 오류 발생:", error);
        throw error;
    }
};

export const createVote = async (groupId: number, voteData: {
    location: string;
    address: string;
    latitude: number;
    longitude: number;
}) => {
    try {
        const response = await api.post(`/votes/groups/${groupId}/votes`, voteData);
        return response.data;
    } catch (error) {
        console.error("투표 생성 중 오류 발생:", error);
        throw error;
    }
};


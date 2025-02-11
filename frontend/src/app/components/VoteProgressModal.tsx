"use client";

import {useEffect, useState} from "react";
import {cancelVote, getVotes, memberVoteStatus, submitVote} from "@/app/api"; // 메서드 가져오기

type Vote = {
    id: number;
    location: string;
    address: string;
};

type VoteModalProps = {
    groupId: number;
    onClose: () => void;
};

export default function VoteModal({groupId, onClose}: VoteModalProps) {
    const [votes, setVotes] = useState<Vote[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedVoteIds, setSelectedVoteIds] = useState<number[]>([]); // 선택한 투표 ID들을 배열로 관리

    useEffect(() => {
        async function fetchVotes() {
            try {
                // 투표 목록 불러오기
                const data = await getVotes(groupId);
                setVotes(data);

                // 사용자의 투표 상태 불러오기
                const userVotes = await memberVoteStatus(groupId);
                setSelectedVoteIds(userVotes.voteIds); // 응답에서 voteIds만 추출하여 설정
            } catch (error) {
                console.error("투표 목록 불러오기 또는 사용자 투표 상태 조회 실패:", error);
            } finally {
                setLoading(false);
            }
        }

        fetchVotes();
    }, [groupId]);

    // 투표 옵션 클릭 이벤트 핸들러
    const handleVoteClick = async (voteId: number) => {
        try {
            if (selectedVoteIds.includes(voteId)) {
                // 이미 선택한 투표를 다시 클릭하면 취소
                await cancelVote(groupId, voteId);
                setSelectedVoteIds(selectedVoteIds.filter(id => id !== voteId));
                alert("투표가 취소되었습니다.");
            } else {
                // 새로운 투표 진행
                await submitVote(groupId, voteId);
                setSelectedVoteIds([...selectedVoteIds, voteId]);
                alert("투표가 반영되었습니다.");
            }
        } catch (error) {
            console.error("투표 처리 중 오류 발생:", error);
            alert("투표 처리 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-96">
                <h2 className="text-xl font-bold mb-4">장소 투표</h2>

                {loading ? (
                    <p>로딩 중...</p>
                ) : votes.length === 0 ? (
                    <p>투표 옵션이 없습니다.</p>
                ) : (
                    <ul className="space-y-2">
                        {votes.map((vote) => (
                            <li
                                key={vote.id}
                                className={`p-3 border rounded-lg shadow-sm cursor-pointer ${
                                    selectedVoteIds.includes(vote.id)
                                        ? "bg-blue-500 text-white"
                                        : "hover:bg-gray-200"
                                }`}
                                onClick={() => handleVoteClick(vote.id)}
                            >
                                <p className="font-semibold">{vote.location}</p>
                                <p className="text-gray-600">{vote.address}</p>
                            </li>
                        ))}
                    </ul>
                )}

                <button
                    className="mt-4 px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                    onClick={onClose}
                >
                    닫기
                </button>
            </div>
        </div>
    );
}

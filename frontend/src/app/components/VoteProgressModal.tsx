"use client";

import {useEffect, useState} from "react";

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

    useEffect(() => {
        async function fetchVotes() {
            try {
                const response = await fetch(`/groups/${groupId}/votes`);
                const data: Vote[] = await response.json();
                setVotes(data);
            } catch (error) {
                console.error("투표 목록 불러오기 실패:", error);
            } finally {
                setLoading(false);
            }
        }

        fetchVotes();
    }, [groupId]);

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
                            <li key={vote.id} className="p-3 border rounded-lg shadow-sm">
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
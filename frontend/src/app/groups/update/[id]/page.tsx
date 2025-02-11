"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { getGroup, updateGroup } from "@/app/api/group"; // 그룹 정보 불러오기, 수정 API
import MainMenu from "@/app/components/MainMenu";

type GroupStatus = "RECRUITING" | "NOT_RECRUITING" | "COMPLETED" | "VOTING" | "DELETED";

export default function EditGroupPage() {
    const router = useRouter();
    const { id } = useParams(); // URL에서 그룹 ID 가져오기
    const [title, setTitle] = useState("");
    const [maxParticipants, setMaxParticipants] = useState<number | "">("");
    const [description, setDescription] = useState("");
    const [status, setStatus] = useState<GroupStatus>("RECRUITING"); // 그룹 상태 추가
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

    // 기존 그룹 정보 불러오기 (수정)
    useEffect(() => {
        async function fetchGroup() {
            try {
                const data = await getGroup(id);
                console.log("불러온 그룹 데이터:", data);
                setTitle(data.title);
                setDescription(data.description);
                setMaxParticipants(data.maxParticipants);
                setStatus(data.groupStatus); // 수정된 필드명
            } catch (error) {
                console.error("그룹 데이터를 불러오는 중 오류 발생:", error);
            }
        }
        fetchGroup();
    }, [id]);

    //  그룹 수정 요청
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!title.trim() || !maxParticipants) {
            setErrorMessage("모든 필수 항목을 입력하세요.");
            return;
        }

        setLoading(true);
        setErrorMessage("");

        const requestData = {
            title,
            description,
            maxParticipants: Number(maxParticipants), // 숫자로 변환
            groupStatus: status || "RECRUITING"
        };

        console.log("보내는 데이터:", requestData); // 백엔드로 보내기 전 확인

        try {
            const response = await updateGroup(id, requestData); // PUT 요청 24, 58 stringdmfh emfdj
            console.log("그룹 수정 성공:", response);
            alert("그룹 정보가 성공적으로 수정되었습니다!");
            router.push("/");
        } catch (error) {
            console.error("수정 요청 실패:", error);
            setErrorMessage("그룹 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <MainMenu />
            <div className="max-w-4xl mx-auto bg-white p-10 mt-10 rounded-lg shadow-lg">
                <form className="space-y-6" onSubmit={handleSubmit}>
                    {/* 제목 입력 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">제목</label>
                        <input
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            placeholder="제목을 입력하세요."
                        />
                    </div>

                    {/* 그룹 상태 변경 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">그룹 상태</label>
                        <select
                            className="w-full px-4 py-2 border border-gray-300 rounded-md"
                            value={status}
                            onChange={(e) => {
                                const newStatus = e.target.value as GroupStatus;
                                if (newStatus === status) {
                                    // 같은 값이어도 강제로 업데이트 트리거
                                    setStatus("");
                                    setTimeout(() => setStatus(newStatus), 0);
                                } else {
                                    setStatus(newStatus);
                                }
                            }}
                        >
                            <option value="RECRUITING">모집 중</option>
                            <option value="NOT_RECRUITING">모집 마감</option>
                            <option value="COMPLETED">투표 완료</option>
                        </select>

                    </div>

                    {/* 모집 인원 설정 */}
                    <div className="flex items-center space-x-2">
                        <label className="block text-gray-700 font-semibold">최대 인원</label>
                        <input
                            type="number"
                            value={maxParticipants}
                            onChange={(e) => setMaxParticipants(e.target.value)}
                            className="w-16 px-2 text-center border border-gray-300 rounded-md"
                        />
                    </div>

                    {/* 내용 입력 */}
                    <div>
                        <label className="block text-gray-700 font-semibold">내용</label>
                        <textarea
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-md h-32"
                            placeholder="내용을 입력하세요."
                        ></textarea>
                    </div>

                    {/* 오류 메시지 */}
                    {errorMessage && <p className="text-red-500">{errorMessage}</p>}

                    {/* 버튼 */}
                    <div className="flex justify-end space-x-4 mt-6">
                        <button type="button" onClick={() => router.back()}
                                className="bg-gray-300 text-gray-700 px-6 py-2 rounded-md">돌아가기
                        </button>
                        <button type="submit" className="bg-green-500 text-white px-6 py-2 rounded-md" disabled={loading}>
                            {loading ? "수정 중..." : "수정하기"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
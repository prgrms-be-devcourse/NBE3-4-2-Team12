// components/VoteModal.tsx
import { useState } from 'react';
import KakaoMap from './KakaoMap';
import { createVote } from '@/app/api/vote';

interface VoteModalProps {
    isOpen: boolean;
    onClose: () => void;
    groupId: number;
    onVoteCreated: () => void;  // 투표 생성 후 부모 컴포넌트 갱신용
}

interface LocationData {
    address: string;
    latitude: number;
    longitude: number;
}

export default function VoteModal({ isOpen, onClose, groupId, onVoteCreated }: VoteModalProps) {
    // 상태 관리
    const [location, setLocation] = useState<string>("");  // 장소명
    const [selectedLocation, setSelectedLocation] = useState<LocationData | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>("");

    // 지도에서 위치 선택 시 호출되는 함수
    const handleLocationSelect = (locationData: LocationData) => {
        setSelectedLocation(locationData);
        setError("");  // 에러 메시지 초기화
    };

    // 폼 제출 처리
    const handleSubmit = async () => {
        try {
            // 입력 검증
            if (!location.trim()) {
                setError("장소명을 입력해주세요.");
                return;
            }
            if (!selectedLocation) {
                setError("지도에서 위치를 선택해주세요.");
                return;
            }

            setLoading(true);
            setError("");

            // 투표 생성 API 호출
            await createVote(groupId, {
                location: location.trim(),
                address: selectedLocation.address,
                latitude: selectedLocation.latitude,
                longitude: selectedLocation.longitude
            });

            // 성공 처리
            onVoteCreated();  // 부모 컴포넌트 갱신
            onClose();        // 모달 닫기

        } catch (error) {
            setError("투표 장소 생성 중 오류가 발생했습니다.");
            console.error("투표 생성 오류:", error);
        } finally {
            setLoading(false);
        }
    };

    // 모달 초기화 함수
    const handleClose = () => {
        setLocation("");
        setSelectedLocation(null);
        setError("");
        onClose();
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 w-[600px] max-h-[90vh] overflow-y-auto">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="text-xl font-bold">투표 장소 추가</h3>
                    <button
                        onClick={handleClose}
                        className="text-gray-500 hover:text-gray-700"
                    >
                        ✕
                    </button>
                </div>

                {/* 장소명 입력 */}
                <div className="mb-4">
                    <label className="block text-gray-700 font-medium mb-2">
                        장소명 *
                    </label>
                    <input
                        type="text"
                        value={location}
                        onChange={(e) => setLocation(e.target.value)}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="예) 스타벅스 강남점"
                    />
                </div>

                {/* 지도 */}
                <div className="mb-4">
                    <label className="block text-gray-700 font-medium mb-2">
                        위치 선택 *
                    </label>
                    <div className="h-96 rounded-md overflow-hidden border">
                        <KakaoMap onLocationSelect={handleLocationSelect} />
                    </div>
                </div>

                {/* 선택된 주소 표시 */}
                <div className="mb-6">
                    <label className="block text-gray-700 font-medium mb-2">
                        선택된 주소
                    </label>
                    <input
                        type="text"
                        value={selectedLocation?.address || ''}
                        readOnly
                        className="w-full px-3 py-2 border rounded-md bg-gray-50"
                        placeholder="지도에서 위치를 선택하세요"
                    />
                </div>

                {/* 에러 메시지 */}
                {error && (
                    <div className="mb-4 text-red-500 text-sm">
                        {error}
                    </div>
                )}

                {/* 버튼 영역 */}
                <div className="flex justify-end gap-2">
                    <button
                        onClick={handleClose}
                        className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors"
                        disabled={loading}
                    >
                        취소
                    </button>
                    <button
                        onClick={handleSubmit}
                        className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
                        disabled={loading}
                    >
                        {loading ? '처리 중...' : '추가하기'}
                    </button>
                </div>
            </div>
        </div>
    );
}
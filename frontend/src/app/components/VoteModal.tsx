import { useState } from 'react';
import KakaoMap from './KakaoMap';


type VoteLocation = {
    location: string;    // 장소명
    address: string;     // 주소
    latitude: number;    // 위도
    longitude: number;   // 경도
};

type VoteModalProps = {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (locations: VoteLocation[]) => void;
};

export default function VoteModal({ isOpen, onClose, onSubmit }: VoteModalProps) {
    const [newLocation, setNewLocation] = useState("");
    const [address, setAddress] = useState("");
    const [latitude, setLatitude] = useState<number | null>(null);
    const [longitude, setLongitude] = useState<number | null>(null);
    const [voteLocations, setVoteLocations] = useState<VoteLocation[]>([]);

    const handleAddVoteLocation = () => {
        if (!newLocation || !address) {
            alert("장소명과 주소를 입력해주세요.");
            return;
        }

        const newVoteLocation: VoteLocation = {
            location: newLocation,
            address: address,
            latitude: latitude || 0,
            longitude: longitude || 0
        };

        setVoteLocations([...voteLocations, newVoteLocation]);
        setNewLocation("");
        setAddress("");
        setLatitude(null);
        setLongitude(null);
    };

    const handleDeleteVoteLocation = (index: number) => {
        setVoteLocations(voteLocations.filter((_, i) => i !== index));
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
            <div className="bg-white p-6 rounded-lg shadow-lg w-[600px]">
                <h3 className="text-lg font-bold mb-4">모임 장소 투표</h3>

                {/* 장소명 입력 */}
                <div className="mb-4">
                    <label className="block text-gray-700 font-semibold mb-2">장소명</label>
                    <input
                        type="text"
                        value={newLocation}
                        onChange={(e) => setNewLocation(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md"
                        placeholder="장소명을 입력하세요"
                    />
                </div>

                {/* 주소 입력 */}
                <div className="mb-4">
                    <label className="block text-gray-700 font-semibold mb-2">주소</label>
                    <input
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md"
                        placeholder="주소를 입력하세요"
                    />
                </div>

                {/* 지도 영역 */}
                <div className="mb-4">
                    <label className="block text-gray-700 font-semibold mb-2">위치 선택</label>
                    <div className="w-full h-96 rounded-md overflow-hidden">
                        <KakaoMap
                            onLocationSelect={({ address, latitude, longitude }) => {
                                setAddress(address);
                                setLatitude(latitude);
                                setLongitude(longitude);
                            }}
                        />
                    </div>
                </div>

                {/* 위도/경도 표시 (읽기 전용) */}
                <div className="grid grid-cols-2 gap-4 mb-4">
                    <div>
                        <label className="block text-gray-700 font-semibold mb-2">위도</label>
                        <input
                            type="text"
                            value={latitude || ''}
                            readOnly
                            className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-md"
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-semibold mb-2">경도</label>
                        <input
                            type="text"
                            value={longitude || ''}
                            readOnly
                            className="w-full px-4 py-2 bg-gray-100 border border-gray-300 rounded-md"
                        />
                    </div>
                </div>

                {/* 추가된 투표 장소 목록 */}
                <div className="mt-4">
                    <h4 className="font-semibold mb-2">추가된 투표 장소</h4>
                    <ul className="space-y-2">
                        {voteLocations.map((loc, index) => (
                            <li key={index} className="flex justify-between items-center bg-gray-100 p-3 rounded-md">
                                <div>
                                    <div className="font-semibold">{loc.location}</div>
                                    <div className="text-sm text-gray-600">{loc.address}</div>
                                </div>
                                <button
                                    onClick={() => handleDeleteVoteLocation(index)}
                                    className="text-red-500"
                                >
                                    ✕
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>

                {/* 버튼 영역 */}
                <div className="flex justify-end space-x-2 mt-4">
                    <button
                        type="button"
                        onClick={onClose}
                        className="px-4 py-2 bg-gray-500 text-white rounded-md"
                    >
                        취소
                    </button>
                    <button
                        type="button"
                        onClick={() => {
                            onSubmit(voteLocations);
                            onClose();
                        }}
                        className="px-4 py-2 bg-blue-500 text-white rounded-md"
                    >
                        확인
                    </button>
                </div>
            </div>
        </div>
    );
}
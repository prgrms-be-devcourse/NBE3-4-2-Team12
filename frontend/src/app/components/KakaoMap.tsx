// components/KakaoMap.tsx - 카카오맵 컴포넌트 생성
import { useEffect, useRef } from 'react';

interface KakaoMapProps {
    onLocationSelect: (location: {
        address: string;
        latitude: number;
        longitude: number;
    }) => void;
}

export default function KakaoMap({ onLocationSelect }: KakaoMapProps) {
    const mapRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!mapRef.current) return;

        const options = {
            center: new kakao.maps.LatLng(37.566826, 126.978656),
            level: 3
        };

        const map = new kakao.maps.Map(mapRef.current, options);
        const geocoder = new kakao.maps.services.Geocoder();

        // 클릭 이벤트 등록
        kakao.maps.event.addListener(map, 'click', (mouseEvent: any) => {
            const latlng = mouseEvent.latLng;

            // 좌표를 주소로 변환
            geocoder.coord2Address(latlng.getLng(), latlng.getLat(), (result, status) => {
                if (status === kakao.maps.services.Status.OK) {
                    const address = result[0].address.address_name;
                    onLocationSelect({
                        address,
                        latitude: latlng.getLat(),
                        longitude: latlng.getLng()
                    });
                }
            });
        });

    }, [onLocationSelect]);

    return <div ref={mapRef} style={{ width: '100%', height: '400px' }} />;
}
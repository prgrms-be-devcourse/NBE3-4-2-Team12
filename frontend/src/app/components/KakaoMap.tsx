// components/KakaoMap.tsx
'use client';

import { useEffect, useRef } from 'react';

interface KakaoMapProps {
    onLocationSelect: (location: {
        address: string;
        latitude: number;
        longitude: number;
    }) => void;
    selectedLocation?:{address:string; latitude:number; longitude:number;};
}

export default function KakaoMap({ onLocationSelect, selectedLocation }: KakaoMapProps) {
    const mapRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!mapRef.current) return;

        // 카카오맵 로드
        window.kakao.maps.load(() => {
            const options = {
                center: new window.kakao.maps.LatLng(37.566826, 126.978656),
                level: 3
            };

            const map = new window.kakao.maps.Map(mapRef.current, options);
            const geocoder = new window.kakao.maps.services.Geocoder();
            let marker = new window.kakao.maps.Marker({map})

            if (selectedLocation){
                const position = new window.kakao.maps.LatLng(selectedLocation.latitude, selectedLocation.longitude);
                marker.setPosition(position);
                marker.setMap(map);
                map.setCenter(position);
            }

            window.kakao.maps.event.addListener(map, 'click', (mouseEvent: any) => {
                const latlng = mouseEvent.latLng;

                geocoder.coord2Address(
                    latlng.getLng(),
                    latlng.getLat(),
                    (result, status) => {
                        if (status === window.kakao.maps.services.Status.OK && result[0]) {
                            const address = result[0].address.address_name;
                            onLocationSelect({
                                address,
                                latitude: latlng.getLat(),
                                longitude: latlng.getLng()
                            });
                        }
                    }
                );
            });
        });
    }, [onLocationSelect, selectedLocation]);

    return <div ref={mapRef} className="w-full h-[400px] rounded-lg" />;
}
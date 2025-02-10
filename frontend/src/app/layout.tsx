import "@/app/styles/tailwind.css";
//KakaoMap api사용위한 import
import Script from 'next/script';


export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
})  {
    return (
        <html lang="ko">
        <head>
            <Script
                src={`//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_APP_KEY&libraries=services,clusterer&autoload=false`}
                strategy="beforeInteractive"
            />
        </head>
        <body>{children}</body>
        </html>
    );
}
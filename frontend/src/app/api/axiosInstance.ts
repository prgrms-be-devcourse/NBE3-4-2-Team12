import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

export const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
    withCredentials: true,
})

// 요청 인터셉터 추가
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');  // 또는 다른 방식으로 토큰 가져오기
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);
import axios, { AxiosRequestConfig, AxiosError } from 'axios';
import { API_URL } from '@/constants/api';
import { PATH } from '@/constants/path';

const axiosInstance = axios.create({
  baseURL: API_URL.BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor (accessToken 자동 추가)
axiosInstance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    }, 
    error => {
        return Promise.reject(error);
    }
)

// token 재발급
const refresh = async () => {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        if(!refreshToken) throw new Error('Refresh token not found');

        const response = await axios.post(API_URL.BASE + API_URL.REFRESH, {refreshToken});
        const { newAccessToken } = response.data.accessToken;
        const { newRefreshToken} = response.data.refreshToken;

        localStorage.setItem('accessToken', newAccessToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        return newAccessToken;
    } catch (err) {
        throw new Error('Token refresh failed');
    }
};

// Response interceptor
axiosInstance.interceptors.response.use(
    res => res,
    async (error: AxiosError) => {
        const originalRequest = error.config as AxiosRequestConfig & { _retry?: boolean };

        if(error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const newAccessToken = await refresh();

                originalRequest.headers = {
                    ...originalRequest.headers,
                    Authorization: `Bearer ${newAccessToken}`,
                };

                return axiosInstance(originalRequest);
            } catch (refreshError) {
                // refresh 실패 → 로그아웃 처리
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                window.location.href = PATH.LOGIN; // 로그인 페이지로 이동
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
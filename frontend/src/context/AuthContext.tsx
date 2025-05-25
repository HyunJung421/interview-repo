import React, { createContext, useState } from 'react';
import { PATH } from '@/constants/path';

interface AuthContextType {
    userName: string | null,
    accessToken: string | null,
    setSession: (accessToken: string, refreshToken: string, userName: string) => void;
    removeSession: () => void;
    isAuthenticated: boolean;
}

// createContext: 전역 데이터를 담을 '상자'
export const AuthContext = createContext<AuthContextType | null>(null); 

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    // useState(() => {}) 형태는 지연 초기화 방식임. 컴포넌트가 처음 렌더링될 때 한 번만 조회함
    // useState(localStorage.getItem('accessToken')) 이런 형태는 컴포넌트가 매 렌더링마다 조회되기 때문에 불필요한 연산이 일어날 수 있음
    const [userName, setUserName] = useState<string | null>(() => 
        localStorage.getItem('userName')
    );
    const [accessToken, setAccessToken] = useState<string | null>(() => 
        localStorage.getItem('accessToken')
    );
    
    const setSession = (newAccessToken: string, newRefreshToken: string, userName: string) => {
        localStorage.setItem('accessToken', newAccessToken);
        localStorage.setItem('refreshToken', newRefreshToken);
        localStorage.setItem('userName', userName);
        setAccessToken(newAccessToken);
        setUserName(userName);
    };

    const removeSession = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userName');
        setAccessToken(null);
        window.location.href = PATH.LOGIN; // 로그인 페이지로 이동
    };

    // useEffect: 화면이 처음 렌더링되면 실행되는 코드
    // 페이지 새로고침하거나 앱이 시작될 때 실행됨
    // 두번째 인자로 [](빈 배열)을 주면 오직 처음 1번만 실행됨
    // 위에서 useState에서 초기화하는 로직이 있을 경우, 아래 로직은 필요 없음
    //
    // useEffect(() => {
    //     const storedAccessToken = localStorage.getItem('accessToken');
    //     if(storedAccessToken) {
    //         setAccessToken(storedAccessToken);
    //     }
    // }, []);

    const isAuthenticated = !!accessToken;

    return (
        <AuthContext.Provider value={{ userName, accessToken, setSession, removeSession, isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};
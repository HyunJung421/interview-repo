/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth'; 
import { login } from '@/api/loginApi';
import { PATH } from '@/constants/path';

const LoginPage = () => {
  const { setSession, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [id, setId] = useState('');
  const [idValid, setIdValid] = useState<boolean>(false);
  const [password, setPassword] = useState('');
  const [pwValid, setPwValid] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const doLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await login({
        id: id,
        password: password
      });
      setError(null);
      setSession(res.data.accessToken, res.data.refreshToken, id);
      navigate(PATH.INTERVIEW);
    } catch (err: unknown) {
      if(err.response.data.code == 'E10000') {
        setError('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
      } else {
        setError('아이디 또는 비밀번호가 틀렸습니다.');
      }
    }
  };

  useEffect(() => {
    setIdValid(id.trim() !== '');
    setPwValid(password.trim() !== '');
  }, [id, password]);

  useEffect(() => {
    if (isAuthenticated) {
      navigate(PATH.INTERVIEW);
    }
  }, [isAuthenticated, navigate]);
  
  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md"
        style={{ boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)' }}>
        <h1 className="text-2xl font-bold text-center text-blue-900 mb-6">면접 REPO</h1>
        <form onSubmit={doLogin}>
          <div className="mb-4">
            <input
              type="text"
              id="username"
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="아이디"
              onChange={e => setId(e.target.value)}
            />
          </div>
          <div className="mb-4">
            <input
              type="password"
              id="password"
              className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="비밀번호"
              onChange={e => setPassword(e.target.value)}
            />
          </div>
          {error ? <div className="mb-4 text-center text-red-500">{error}</div> : ''}
          <button
            type="submit"
            disabled={!(idValid && pwValid)}
            className={`w-full text-white py-3 rounded-lg transition ${idValid && pwValid ? 'bg-blue-900 hover:bg-blue-800' : 'bg-gray-900/30 cursor-not-allowed'}`}
          >
            로그인
          </button>
        </form>
        <div className="mt-4 text-center text-gray-500 text-sm">
          <a href="#" className="hover:underline text-gray-500">아이디 찾기</a> | <a href="#" className="hover:underline text-gray-500">비밀번호 찾기</a> | <a href="#" className="hover:underline text-gray-500">회원가입</a>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
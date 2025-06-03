import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { findPassword } from '@/api/loginApi';
import { PATH } from '@/constants/path';
import LoadingLayer from '@/components/common/LoadingLayer';

const FindPasswordPage = () => {
  const navigate = useNavigate();
  const [id, setId] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [isSuccess, setIsSuccess] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);
  const [isLoading, setIsLoading] = useState(false);  // 로딩 상태 추가
  const [inputErrors, setInputErrors] = useState({
    id: '',
    email: ''
  });

  useEffect(() => {
    const validateForm = () => {
      const newErrors = {
        id: '',
        email: ''
      };

      if (id.trim() === '') {
        newErrors.id = '아이디를 입력해주세요';
      }

      if (email.trim() === '') {
        newErrors.email = '이메일을 입력해주세요';
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        newErrors.email = '올바른 이메일 형식이 아닙니다';
      }

      setInputErrors(newErrors);
      setIsFormValid(!newErrors.id && !newErrors.email);
    };

    validateForm();
  }, [id, email]);

  const handleFindPassword = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!isFormValid) {
      return;
    }

    setIsLoading(true);  // API 호출 전 로딩 시작
    try {
      await findPassword({ id, email });
      setIsSuccess(true);
      setError(null);
    } catch (err: any) {
      if(err.response?.data?.code === 'E10000') {
        setError('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
      } else {
        setError('일치하는 정보를 찾을 수 없습니다.');
      }
    } finally {
      setIsLoading(false);  // API 호출 완료 후 로딩 종료
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-100 px-4">
      {isLoading && <LoadingLayer />}  {/* 로딩 레이어 추가 */}
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md"
        style={{ boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)' }}>
        <h1 className="text-2xl font-bold text-center text-blue-900 mb-6">비밀번호 찾기</h1>
        {!isSuccess ? (
          <form onSubmit={handleFindPassword}>
            <div className="mb-4">
              <input
                type="text"
                className={`w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  inputErrors.id ? 'border-red-500' : ''
                }`}
                placeholder="아이디"
                value={id}
                onChange={e => setId(e.target.value)}
              />
              {inputErrors.id && (
                <p className="mt-1 text-sm text-red-500">{inputErrors.id}</p>
              )}
            </div>
            <div className="mb-4">
              <input
                type="email"
                className={`w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                  inputErrors.email ? 'border-red-500' : ''
                }`}
                placeholder="이메일"
                value={email}
                onChange={e => setEmail(e.target.value)}
              />
              {inputErrors.email && (
                <p className="mt-1 text-sm text-red-500">{inputErrors.email}</p>
              )}
            </div>
            {error && <div className="mb-4 text-center text-red-500">{error}</div>}
            <button
              type="submit"
              disabled={!isFormValid || isLoading}  // 로딩 중에도 버튼 비활성화
              className={`w-full text-white py-3 rounded-lg transition ${
                isFormValid && !isLoading
                  ? 'bg-blue-900 hover:bg-blue-800' 
                  : 'bg-gray-900/30 cursor-not-allowed'
              }`}
            >
              {isLoading ? '처리 중...' : '다음'}  {/* 로딩 중일 때 버튼 텍스트 변경 */}
            </button>
          </form>
        ) : (
          <div className="text-center">
            <p className="mb-4">입력하신 이메일로 임시 비밀번호가 발송되었습니다.</p>
            <button
              onClick={() => navigate(PATH.LOGIN)}
              className="w-full bg-blue-900 text-white py-3 rounded-lg hover:bg-blue-800 transition"
            >
              로그인하기
            </button>
          </div>
        )}
        <div className="mt-4 text-center">
          <button
            onClick={() => navigate(PATH.LOGIN)}
            className="text-gray-500 hover:underline"
          >
            로그인 페이지로 돌아가기
          </button>
        </div>
      </div>
    </div>
  );
};

export default FindPasswordPage;
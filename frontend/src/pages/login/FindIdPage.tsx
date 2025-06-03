import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { findId } from '@/api/loginApi';
import { PATH } from '@/constants/path';


const FindIdPage = () => {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [isFound, setIsFound] = useState(false);
  const [foundId, setFoundId] = useState('');
  const [createdAt, setCreatedAt] = useState('');

  const handleFindId = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await findId({ name, email });
      setFoundId(response.data.id);
      setCreatedAt(response.data.createdAt.slice(0, 10));
      setIsFound(true);
      setError(null);
    } catch (err: any) {
      if(err.response?.data?.code === 'E10000') {
        setError('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
      } else {
        setError('일치하는 정보를 찾을 수 없습니다.');
      }
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md"
        style={{ boxShadow: '10px 10px 10px rgba(0, 0, 0, 0.1)' }}>
        <h1 className="text-2xl font-bold text-center text-blue-900 mb-6">아이디 찾기</h1>
        {!isFound ? (
          <form onSubmit={handleFindId}>
            <div className="mb-4">
              <input
                type="text"
                className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="이름"
                value={name}
                onChange={e => setName(e.target.value)}
              />
            </div>
            <div className="mb-4">
              <input
                type="email"
                className="w-full px-4 py-3 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="이메일"
                value={email}
                onChange={e => setEmail(e.target.value)}
              />
            </div>
            {error && <div className="mb-4 text-center text-red-500">{error}</div>}
            <button
              type="submit"
              className="w-full bg-blue-900 text-white py-3 rounded-lg hover:bg-blue-800 transition"
            >
              아이디 찾기
            </button>
          </form>
        ) : (
          <div className="text-center">
            <p className="mb-4">아이디: {foundId}</p>
            <p className="mb-4">가입일: {createdAt}</p>
            <button
                onClick={() => navigate(PATH.LOGIN, { state: { foundId } })}
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

export default FindIdPage;
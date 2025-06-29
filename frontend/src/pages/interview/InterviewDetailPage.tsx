import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { MdDeleteForever, MdEdit, MdOutlineBookmarkBorder } from "react-icons/md";
import { getInterviewDetail } from '@/api/interviewApi';
import { InterviewDetail } from '@/types/interview';
import MDEditor from '@uiw/react-md-editor';


const InterviewDetailPage: React.FC = () => {
  const { key } = useParams(); // ← 여기서 key 받아옴
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [interview, setInterview] = useState<InterviewDetail | null>(null);

  useEffect(() => {
    const fetchInterviewDetail = async () => {
      if (!key) return; // key가 없으면 호출하지 않음
      setLoading(true);
      setError(null);
      try {
        const res = await getInterviewDetail(key);
        setInterview(res.data);
      } catch (err: unknown) {
        if (err && typeof err === 'object' && 'response' in err) {
          const error = err as { response?: { data?: { code?: string } } };
          if (error.response?.data?.code === 'E10000') {
            setError('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
          } else {
            setError('질문 상세 정보를 불러오지 못했습니다.');
          }
        } else {
          setError('질문 상세 정보를 불러오지 못했습니다.');
        }
      } finally {
        setLoading(false);
      }
    };
    fetchInterviewDetail();
  }, []);

  return (
      
    <div className="p-8 bg-white min-h-[calc(100vh-21vh)] relative">
      <div className="flex justify-end items-center mb-6">
        <div className="space-x-2 flex items-center">
          <button className="px-2 py-2 w-25 h-10 bg-white border-2 border-gray-600 font-bold text-black rounded-lg text-sm flex items-center gap-1">
            <MdOutlineBookmarkBorder size={20}/>북마크
          </button>
          <button className="px-2 py-2 w-25 h-10 bg-navy text-white rounded-lg font-bold text-sm flex items-center gap-1">
            <MdEdit size={20}/>수정</button>
          <button className="px-2 py-2 w-25 h-10 bg-red-500 text-white rounded-lg font-bold text-sm flex items-center gap-1">
            <MdDeleteForever size={20}/>삭제
          </button>
        </div>
      </div>
      
      <div className="bg-gray-50 p-6 rounded border border-gray-200">
        {/* 카테고리 및 질문 */}
        <div>
          <div className={`inline-block text-center font-bold text-xs px-2 py-1 rounded-full mb-2 ${interview?.category === 'FRONTEND' ? 'bg-lime text-green' : 'bg-babyblue text-navy'}`}>
            {interview?.category}
          </div>
          <h2 className="text-xl font-bold mb-6">{interview?.title || '로딩 중...'}</h2>
          <hr></hr>
        </div>

        {/* 질문 답변 */}
        <div className="mt-6">
          {loading && <p className="text-gray-500">로딩 중...</p>}
          {error && <p className="text-red-500">{error}</p>}
          
          {!loading && !error && interview?.content && (
            <div>
              <MDEditor.Markdown
                className="bg-gray-50"
                source={interview.content.replace(/\n/gi, "\n\n")}/>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default InterviewDetailPage;
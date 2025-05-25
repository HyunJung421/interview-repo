import React, { useState, useEffect } from 'react';
import { getInterviewList } from '@/api/interviewApi';
import { InterviewListItem } from '@/types/interview';

const PER_PAGE = 10;

const InterviewPage: React.FC = () => {
  const [page, setPage] = useState(1);
  const [questions, setQuestions] = useState<InterviewListItem[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [title, setTitle] = useState('');
  const [category, setCategory] = useState<'' | 'FRONTEND' | 'BACKEND'>('');

  useEffect(() => {
    const fetchQuestions = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await getInterviewList({
          title: title || undefined,
          category: category === '' ? undefined : category,
          currentPage: page,
          perPage: PER_PAGE,
        });
        setQuestions(res.data.list);
        setTotalCount(res.data.pageInfo.totalCount);
      } catch (err: unknown) {
        if(err.response.data.code == 'E10000') {
          setError('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        } else {
          setError('질문 목록을 불러오지 못했습니다.');
        }
      } finally {
        setLoading(false);
      }
    };
    fetchQuestions();
  }, [page, title, category]);

  const totalPages = Math.ceil(totalCount / PER_PAGE) || 1;

  return (
    <div className="p-8 bg-white min-h-[calc(100vh-21vh)] relative">
      {/* 검색 & 필터 */}
      <div className="flex gap-2 mb-8">
        <input
          placeholder="제목 또는 태그"
          className="border border-gray-300 px-3 py-2 rounded w-1/4"
          value={title}
          onChange={e => setTitle(e.target.value)}
        />
        <select
          className="border border-gray-300 px-3 py-2 rounded w-1/6"
          value={category}
          onChange={e => setCategory(e.target.value as '' | 'FRONTEND' | 'BACKEND')}
        >
          <option value="">카테고리</option>
          <option value="FRONTEND">FRONTEND</option>
          <option value="BACKEND">BACKEND</option>
        </select>
        <button className="ml-auto bg-navy text-white px-4 py-2 rounded-lg font-semibold">
          + 질문 추가
        </button>
      </div>

      {/* 질문 카드 목록 */}
      <div className="space-y-2 min-h-[200px] pb-20">
        {loading ? (
          <div className="text-center py-10 text-gray-400">불러오는 중...</div>
        ) : error ? (
          <div className="text-center py-10 text-red-500">{error}</div>
        ) : questions.length === 0 ? (
          <div className="text-center py-10 text-gray-400">질문이 없습니다.</div>
        ) : (
          questions.map((q, i) => (
            <div
              key={i}
              className="border rounded-lg flex items-center gap-4 bg-white shadow"
            >
              <span
                className={`text-middle text-center font-bold w-[120px] px-4 py-3 rounded-l-lg ${q.category === 'FRONTEND' ? 'bg-lime text-green' : 'bg-babyblue text-navy'}`}
              >
                {q.category}
              </span>
              <span>{q.title}</span>
            </div>
          ))
        )}
      </div>

      {/* 페이징 - 내부 하단 고정 */}
      <div className="absolute left-0 bottom-0 w-full bg-white flex justify-center items-center gap-1 py-4 z-10">
        <button
          className="w-8 h-8 flex items-center justify-center bg-white text-navy hover:font-bold"
          onClick={() => setPage((p) => Math.max(1, p - 1))}
          disabled={page === 1}
        >
          &lt;
        </button>
        {Array.from({ length: totalPages }, (_, i) => (
          <button
            key={i}
            onClick={() => setPage(i + 1)}
            className={`w-8 h-8 flex items-center justify-center rounded-full text-sm font-medium ${page === i + 1
              ? 'bg-navy text-white'
              : 'bg-white text-navy hover:bg-gray-100'
              }`}
          >
            {i + 1}
          </button>
        ))}
        <button
          className="w-8 h-8 flex items-center justify-center bg-white text-navy hover:font-bold"
          onClick={() => setPage((p) => Math.min(totalPages, p + 1))}
          disabled={page === totalPages}
        >
          &gt;
        </button>
      </div>
    </div>
  );
};

export default InterviewPage;

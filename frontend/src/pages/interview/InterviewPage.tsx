import React, { useState } from 'react';

const questions = [
  { type: 'FRONTEND', text: '시간 복잡도와 공간 복잡도의 차이점은 무엇인가요?' },
  { type: 'FRONTEND', text: '시간 복잡도와 공간 복잡도의 차이점은 무엇인가요?' },
  { type: 'FRONTEND', text: '시간 복잡도와 공간 복잡도의 차이점은 무엇인가요?' },
  { type: 'FRONTEND', text: '시간 복잡도와 공간 복잡도의 차이점은 무엇인가요?' },
  { type: 'BACKEND', text: 'CDN이란 무엇인가요?' },
  { type: 'BACKEND', text: 'CDN이란 무엇인가요?' },
  { type: 'BACKEND', text: '스프링 트랜잭션 속성에 대해서 설명해주세요.' },
  { type: 'BACKEND', text: '동시성 문제 중 경쟁 상태를 해결하려면 무엇이 보장되어야 하나요?' },
  { type: 'BACKEND', text: '동시성 문제 중 경쟁 상태를 해결하려면 무엇이 보장되어야 하나요?' },
  { type: 'BACKEND', text: '동시성 문제 중 경쟁 상태를 해결하려면 무엇이 보장되어야 하나요?' },
];

const InterviewPage: React.FC = () => {
  const [page, setPage] = useState(1);

  return (
      <div className="p-8 bg-white">
        {/* 검색 & 필터 */}
        <div className="flex gap-2 mb-4">
          <input
            placeholder="제목 또는 태그"
            className="border border-gray-300 px-3 py-2 rounded w-1/4"
          />
          <select className="border border-gray-300 px-3 py-2 rounded w-1/6">
            <option value="">카테고리</option>
            <option value="FRONTEND">FRONTEND</option>
            <option value="BACKEND">BACKEND</option>
          </select>
          <button className="ml-auto bg-navy text-white px-4 py-2 rounded-lg font-semibold">
            + 질문 추가
          </button>
        </div>

        {/* 질문 카드 목록 */}
        <div className="space-y-2">
          {questions.map((q, i) => (
            <div
              key={i}
              className="border rounded-lg flex items-center gap-4 bg-white shadow"
            >
              <span
                className={`text-middle text-center font-bold w-[120px] px-4 py-3 rounded-l-lg ${q.type === 'FRONTEND' ? 'bg-lime text-green' : 'bg-babyblue text-navy'
                  }`}
              >
                {q.type}
              </span>
              <span>{q.text}</span>
            </div>
          ))}
        </div>

        {/* 페이징 */}
        <div className="flex justify-center items-center gap-1 mt-6">
          <button
            className="w-8 h-8 flex items-center justify-center bg-white text-navy hover:font-bold"
            onClick={() => setPage((p) => Math.max(1, p - 1))}
          >
            &lt;
          </button>
          {Array.from({ length: 9 }, (_, i) => (
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
            onClick={() => setPage((p) => Math.min(9, p + 1))}
          >
            &gt;
          </button>
        </div>
      </div>
  );
};

export default InterviewPage;

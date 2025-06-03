import React from 'react';

interface Props {
  /** 레이아웃에 표시될 메인 컨텐츠 */
  content: React.ReactNode;
}

/** 헤더가 없는 레이아웃 컴포넌트. 로그인이나 에러 페이지와 같은 헤더가 필요 없는 페이지에서 사용됨 */
const NoHeaderLayout = ({ content }: Props) => {
  return (
    <div className="w-full min-h-screen bg-gray-50">
      <main>{content}</main>
    </div>
  );
};

export default NoHeaderLayout;
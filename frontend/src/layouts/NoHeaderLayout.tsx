import React from 'react';

interface Props {
  /** The main content to be displayed in the layout */
  content: React.ReactNode;
}

/**
 * NoHeaderLayout is a layout component that does not include a header.
 * It is used for pages that do not require a header, such as login or error pages.
 */
const NoHeaderLayout: React.FC<Props> = ({ content }) => {
  return (
    <div className="w-full min-h-screen bg-gray-50">
      <main>{content}</main>
    </div>
  );
};

export default NoHeaderLayout;
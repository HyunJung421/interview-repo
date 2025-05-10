import React from 'react';

interface Props {
  content: React.ReactNode;
}

/**
 * NoHeaderLayout is a layout component that does not include a header.
 * It is used for pages that do not require a header, such as login or error pages.
 *
 * @param {React.ReactNode} content - The main content to be displayed in the layout.
 * @returns {JSX.Element} The rendered layout component.
 */
const NoHeaderLayout: React.FC<Props> = ({ content }) => {
  return (
    <div className="w-full min-h-screen bg-gray-50">
      {/* Main content */}
      <main>{content}</main>
    </div>
  );
};

export default NoHeaderLayout;
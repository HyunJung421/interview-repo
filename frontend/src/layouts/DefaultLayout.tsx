import React from 'react';
import { useAuth } from '@/hooks/useAuth';

interface Props {
  content: React.ReactNode;
  userName: string;
}

/**
 * DefaultLayout is a layout component that includes a header with a title and a user name.
 * It is used for pages that require a header, such as the main interview page.
 *
 * @param {React.ReactNode} content - The main content to be displayed in the layout.
 * @param {string} userName - The name of the user to be displayed in the header.
 * @returns {JSX.Element} The rendered layout component.
 */
const DefaultLayout: React.FC<Props> = ({ content }) => {
  const { userName } = useAuth();

  return (
    <div className="w-full min-h-screen bg-gray-100">
      <header className="bg-white shadow px-6 py-4 flex items-center justify-between">
        <h1 className="text-2xl font-bold text-navy">면접 REPO</h1>
      
        {/* Username */}
        <div className="flex items-center space-x-2">
          <span className="font-bold text-lg">
            {userName} 님
          </span>
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="size-8">
            <path fill-rule="evenodd" d="M18.685 19.097A9.723 9.723 0 0 0 21.75 12c0-5.385-4.365-9.75-9.75-9.75S2.25 6.615 2.25 12a9.723 9.723 0 0 0 3.065 7.097A9.716 9.716 0 0 0 12 21.75a9.716 9.716 0 0 0 6.685-2.653Zm-12.54-1.285A7.486 7.486 0 0 1 12 15a7.486 7.486 0 0 1 5.855 2.812A8.224 8.224 0 0 1 12 20.25a8.224 8.224 0 0 1-5.855-2.438ZM15.75 9a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0Z" clip-rule="evenodd" />
          </svg>
        </div>
      </header>

      {/* Main content */}
      <main className="bg-white mt-10 mx-10 mb-10 px-6 py-6 rounded shadow min-h-[calc(100vh-150px)]">{content}</main>
    </div>
  );
};

export default DefaultLayout;
import { createBrowserRouter } from 'react-router-dom';
import NoHeaderLayout from './layouts/NoHeaderLayout';
import DefaultLayout from '@/layouts/DefaultLayout';
import LoginPage from '@/pages/login/LoginPage';
// import InterviewPage from '@/pages/interview/InterviewPage';

const router = createBrowserRouter([
    { path: '/login', element: <NoHeaderLayout content={<LoginPage />} /> },
    // { path: '/interview', element: <DefaultLayout content={<InterviewPage />} userName={'홍길동'} /> },
]);

export default router;
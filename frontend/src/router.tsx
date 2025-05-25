import { createBrowserRouter } from 'react-router-dom';
import NoHeaderLayout from '@/layouts/NoHeaderLayout';
import DefaultLayout from '@/layouts/DefaultLayout';
import { PATH } from '@/constants/path';
import LoginPage from '@/pages/login/LoginPage';
import InterviewPage from '@/pages/interview/InterviewPage';

const router = createBrowserRouter([
    { index: true, element: <NoHeaderLayout content={<LoginPage />} /> },
    { path: PATH.LOGIN, element: <NoHeaderLayout content={<LoginPage />} /> },
    { path: PATH.INTERVIEW, element: <DefaultLayout content={<InterviewPage />} /> },
]);

export default router;
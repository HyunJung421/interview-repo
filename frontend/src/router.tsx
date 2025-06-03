import {createBrowserRouter} from 'react-router-dom';
import NoHeaderLayout from '@/layouts/NoHeaderLayout';
import DefaultLayout from '@/layouts/DefaultLayout';
import {PATH} from '@/constants/path';
import LoginPage from '@/pages/login/LoginPage';
import InterviewPage from '@/pages/interview/InterviewPage';
import FindIdPage from '@/pages/login/FindIdPage';
import FindPasswordPage from '@/pages/login/FindPasswordPage';

const router = createBrowserRouter([
    {index: true, element: <NoHeaderLayout content={<LoginPage/>}/>},
    {path: PATH.LOGIN, element: <NoHeaderLayout content={<LoginPage/>}/>},
    {path: PATH.FIND_ID, element: <NoHeaderLayout content={<FindIdPage/>}/>},
    {path: PATH.FIND_PASSWORD, element: <NoHeaderLayout content={<FindPasswordPage/>}/>},
    {path: PATH.INTERVIEW, element: <DefaultLayout content={<InterviewPage/>}/>},
]);

export default router;
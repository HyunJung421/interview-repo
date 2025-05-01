import {createBrowserRouter} from 'react-router-dom';
import LoginPage from '@pages/login/LoginPage';

const router = createBrowserRouter([
    { path: '/login', element: <LoginPage /> },
]);

export default router;
export const API_URL = {
    BASE: '/interview-api',

    // Auth
    LOGIN: '/auth/login',
    LOGOUT: '/auth/logout',
    REFRESH: '/auth/refresh',

    // User
    SIGNUP: '/user/signup',
    FIND_ID: '/user/find-id',
    FIND_PASSWORD: '/user/find-password',

    // Interview
    INTERVIEW_LIST: '/interview',
    INTERVIEW_DETAIL: (key: string) => `/interview/${key}`,
}
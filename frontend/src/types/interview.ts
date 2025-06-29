import { PageInfo } from "@/types/pageInfo";

// 면접 질문 조회 Request
export type InterviewListParams = {
    title?: string;
    category?: 'FRONTEND' | 'BACKEND';
    bookmarked?: boolean;
    currentPage: number;
    perPage: number;

};

// 면접 질문 조회 Response > list
export type InterviewListItem = {
    key: string;
    category: 'FRONTEND' | 'BACKEND';
    title: string;
    updated: string;
}

// 면접 질문 조회 Response
export type InterviewList = {
    list: InterviewListItem[];
    pageInfo: PageInfo;
};

export type InterviewDetail = {
    key: string;
    category: 'FRONTEND' | 'BACKEND';
    title: string;
    content: string;
    bookmarked: boolean;
}
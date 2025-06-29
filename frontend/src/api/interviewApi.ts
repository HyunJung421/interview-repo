import axiosInstance from "@/api/axiosInstance";
import { API_URL } from "@/constants/api";
import { ApiResponse } from "@/types/response";
import {
    InterviewListParams,
    InterviewList,
    InterviewDetail
} from "@/types/interview";

export const getInterviewList = async (interviewListRequest: InterviewListParams) => {
    const response = await axiosInstance.get<ApiResponse<InterviewList>>(
        API_URL.INTERVIEW_LIST,
        { params: interviewListRequest }
    );

    return response.data;
}

export const getInterviewDetail = async (key: string) => {
    const response = await axiosInstance.get<ApiResponse<InterviewDetail>>(
        `${API_URL.INTERVIEW_DETAIL(key)}`
    );

    return response.data;
}
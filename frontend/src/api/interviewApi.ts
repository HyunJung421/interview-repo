import axiosInstance from "@/api/axiosInstance";
import { API_URL } from "@/constants/api";
import { ApiResponse } from "@/types/response";
import {
    InterviewListParams,
    InterviewList,
} from "@/types/interview";

export const getInterviewList = async (interviewListRequest: InterviewListParams) => {
    const response = await axiosInstance.get<ApiResponse<InterviewList>>(
        API_URL.INTERVIEW_LIST,
        { params: interviewListRequest }
    );

    return response.data;
}
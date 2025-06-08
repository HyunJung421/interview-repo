import axios from "axios";
import { API_URL } from "@/constants/api";
import { ApiResponse } from "@/types/response";
import { LoginParams, TokenInfo } from "@/types/login";

export const login = async (loginParams: LoginParams) => {
    const response = await axios.post<ApiResponse<TokenInfo>>(
        API_URL.BASE + API_URL.LOGIN,
        loginParams,
        {
            headers: {
                'Content-Type': 'application/json',
            }
        }
    );
    
    return response.data;
}

interface FindIdParams {
    name: string;
    email: string;
}

interface FindIdResponse {
    id: string;
    createdAt: string;
}

export const findId = async (params: FindIdParams) => {
    const response = await axios.post<ApiResponse<FindIdResponse>>(
        API_URL.BASE + API_URL.FIND_ID,
        params,
        {
            headers: {
                'Content-Type': 'application/json',
            }
        }
    );

    return response.data;
}

interface FindPasswordRequest {
    id: string;
    email: string;
}

interface FindPasswordResponse {
    message: string;
}

export const findPassword = async (params: FindPasswordRequest) => {
    const response = await axios.post<ApiResponse<FindPasswordResponse>>(
        API_URL.BASE + API_URL.FIND_PASSWORD,
        params,
        {
            headers: {
                'Content-Type': 'application/json',
            }
        }
    );

    return response.data;
};
export type ApiResponse<T> = {
    code: string,
    success: boolean,
    data: T;
}
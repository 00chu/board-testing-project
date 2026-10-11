import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// 게시글 목록 조회
export const listPosts = async () => (await api.get("/posts")).data;

// 게시글 상세 조회
export const getPost = async (id) => (await api.get(`/posts/${id}`)).data;

// 게시글 등록
export const createPost = async (data) => (await api.post("/posts", data)).data;

// 게시글 수정
export const updatePost = async (id, data) =>
  (await api.put(`/posts/${id}`, data)).data;

// 게시글 삭제
export const deletePost = async (id) => (await api.delete(`/posts/${id}`)).data;

// API 오류 메시지 추출
export const errorMessage = (error) => {
  return (
    error.response?.data?.message ||
    error.message ||
    "요청 처리 중 오류가 발생했습니다."
  );
};

import { useCallback, useEffect, useState } from "react";
import {
  listPosts,
  getPost,
  createPost,
  updatePost,
  deletePost,
  errorMessage,
} from "./api/posts";
import PostForm from "./components/PostForm";
export default function App() {
  const [posts, setPosts] = useState([]);
  const [selected, setSelected] = useState(null);
  const [page, setPage] = useState("list");
  const [busy, setBusy] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const load = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      setPosts(await listPosts());
    } catch (e) {
      setError(errorMessage(e));
    } finally {
      setLoading(false);
    }
  }, []);
  useEffect(() => {
    load();
  }, [load]);
  async function detail(id) {
    setLoading(true);
    setError("");
    try {
      setSelected(await getPost(id));
      setPage("detail");
    } catch (e) {
      setError(errorMessage(e));
    } finally {
      setLoading(false);
    }
  }
  async function save(data) {
    setBusy(true);
    setError("");
    try {
      const result =
        page === "edit"
          ? await updatePost(selected.id, data)
          : await createPost(data);
      await load();
      setSelected(result);
      setPage("detail");
    } catch (e) {
      setError(errorMessage(e));
    } finally {
      setBusy(false);
    }
  }
  async function remove() {
    if (!window.confirm("게시글을 삭제할까요?")) return;
    setBusy(true);
    setError("");
    try {
      await deletePost(selected.id);
      setSelected(null);
      setPage("list");
      await load();
    } catch (e) {
      setError(errorMessage(e));
    } finally {
      setBusy(false);
    }
  }
  function goList() {
    setPage("list");
    setSelected(null);
    setError("");
    load();
  }
  return (
    <div className="layout">
      <header>
        <div
          className="brand"
          onClick={goList}
          role="button"
          tabIndex={0}
          onKeyDown={(e) => e.key === "Enter" && goList()}
        >
          <span className="brandIcon">B</span>
          <span>
            Board Lab<small>CLASSIST × MOCKIST</small>
          </span>
        </div>
        <button className="secondary" onClick={goList}>
          전체 게시글
        </button>
      </header>
      <main className="panel">
        {error && (
          <div className="alert" role="alert">
            {error}
            <button className="textBtn" onClick={() => setError("")}>
              닫기
            </button>
          </div>
        )}
        {page === "list" && (
          <>
            <div className="heading">
              <div>
                <p className="eyebrow">BOARD</p>
                <h1>게시판</h1>
                <p className="description">
                  Spring Boot + React 테스트 연습 프로젝트
                </p>
              </div>
              <button
                onClick={() => {
                  setError("");
                  setPage("create");
                }}
              >
                + 글 작성
              </button>
            </div>
            {loading ? (
              <p className="empty">불러오는 중...</p>
            ) : posts.length === 0 ? (
              <div className="empty">
                아직 작성된 게시글이 없습니다.
                <br />첫 글을 작성해보세요.
              </div>
            ) : (
              <div className="tableWrap">
                <table>
                  <thead>
                    <tr>
                      <th className="idCol">번호</th>
                      <th>제목</th>
                      <th>작성자</th>
                      <th>작성일</th>
                    </tr>
                  </thead>
                  <tbody>
                    {posts.map((post) => (
                      <tr key={post.id} onClick={() => detail(post.id)}>
                        <td>{post.id}</td>
                        <td className="postTitle">{post.title}</td>
                        <td>{post.author}</td>
                        <td>{formatDate(post.createdAt)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
            <div className="footerCount">전체 {posts.length}개 게시글</div>
          </>
        )}
        {(page === "create" || page === "edit") && (
          <>
            <div className="heading">
              <div>
                <p className="eyebrow">EDITOR</p>
                <h1>{page === "create" ? "게시글 작성" : "게시글 수정"}</h1>
              </div>
            </div>
            <PostForm
              initial={page === "edit" ? selected : null}
              onSave={save}
              onCancel={() => (page === "edit" ? setPage("detail") : goList())}
              busy={busy}
            />
          </>
        )}
        {page === "detail" && selected && (
          <>
            <button className="back" onClick={goList}>
              ← 목록으로
            </button>
            <div className="detailHead">
              <h1>{selected.title}</h1>
              <div className="meta">
                작성자 {selected.author} · {formatDate(selected.createdAt)}
                {selected.updatedAt !== selected.createdAt ? " · 수정됨" : ""}
              </div>
            </div>
            <div className="bodyContent">{selected.content}</div>
            <div className="actions">
              <button className="secondary" onClick={() => setPage("edit")}>
                수정
              </button>
              <button className="danger" disabled={busy} onClick={remove}>
                삭제
              </button>
            </div>
          </>
        )}
      </main>
    </div>
  );
}

function formatDate(value) {
  if (!value) return "-";
  return value.replace("T", " ").slice(0, 16);
}

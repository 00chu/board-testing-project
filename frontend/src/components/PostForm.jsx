import { useEffect, useState } from "react";

export default function PostForm({ initial, onSave, onCancel, busy }) {
  const [title, setTitle] = useState(initial?.title || "");
  const [author, setAuthor] = useState(initial?.author || "");
  const [content, setContent] = useState(initial?.content || "");

  useEffect(() => {
    setTitle(initial?.title || "");
    setAuthor(initial?.author || "");
    setContent(initial?.content || "");
  }, [initial]);

  const submit = (e) => {
    e.preventDefault();
    onSave({
      title: title.trim(),
      author: author.trim(),
      content: content.trim(),
    });
  };

  return (
    <form className="form" onSubmit={submit}>
      <label>
        제목
        <input
          maxLength={100}
          required
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="제목을 입력하세요"
        />
      </label>
      <label>
        작성자
        <input
          maxLength={30}
          required
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          placeholder="작성자"
        />
      </label>
      <label>
        내용
        <textarea
          required
          rows={11}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="내용을 입력하세요"
        />
      </label>

      <div className="actions">
        <button className="secondary" type="button" onClick={onCancel}>
          취소
        </button>
        <button disabled={busy} type="submit">
          {busy ? "저장 중" : "저장"}
        </button>
      </div>
    </form>
  );
}

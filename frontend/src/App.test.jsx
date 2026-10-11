import {afterEach,expect,it,vi} from 'vitest';
import {cleanup,render,screen} from '@testing-library/react';
import App from './App';
vi.mock('./api/posts',()=>({listPosts:vi.fn(async()=>[]),getPost:vi.fn(),createPost:vi.fn(),updatePost:vi.fn(),deletePost:vi.fn(),errorMessage:vi.fn()}));
afterEach(()=>cleanup());
it('게시판 제목과 글 작성 버튼을 보여준다',async()=>{
  render(<App/>);
  expect(await screen.findByRole('heading',{name:'게시판'})).toBeTruthy();
  expect(screen.getByRole('button',{name:'+ 글 작성'})).toBeTruthy();
});

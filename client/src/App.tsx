import { BrowserRouter, Route, Routes } from 'react-router';

import './App.css';
import RootLayout from './layouts/RootLayout';
import AuthorPage from './pages/AuthorPage';
import AuthorsPage from './pages/AuthorsPage';
import GenrePage from './pages/GenrePage';
import GenresPage from './pages/GenresPage';
import NotFoundPage from './pages/NotFoundPage';
import BooksPage from './pages/BooksPage';
import BookPage from './pages/BookPage';

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RootLayout />}>
            <Route path="/genres">
              <Route index element={<GenresPage />}></Route>
              <Route path=":id" element={<GenrePage />}></Route>
            </Route>
            <Route path="/authors">
              <Route index element={<AuthorsPage />}></Route>
              <Route path=":id" element={<AuthorPage />}></Route>
            </Route>
            <Route path="/books">
              <Route index element={<BooksPage />}></Route>
              <Route path=":id" element={<BookPage />}></Route>
            </Route>
          </Route>

          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;

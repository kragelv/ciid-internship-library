import { useEffect, useState } from 'react';
import { useParams, Link, useSearchParams } from 'react-router';

import {
  AuthorAsyncPaginate,
  AuthorOptionType,
  mapAuthorToOption,
} from '../components/AuthorAsyncPaginate';
import {
  GenreMultiAsyncPaginate,
  GenreOptionType,
  mapGenreToOption,
} from '../components/GenreMultiAsyncPaginate';
import Loader from '../components/Loader';
import StateFromLink from '../components/StateFromLink';

import { AuthorDto } from '../dtos/author/AuthorDto';
import { BookDto } from '../dtos/book/BookDto';
import { BookRequestDto } from '../dtos/book/BookRequestDto';
import { useReactRouterStateFrom } from '../hooks';
import BookService from '../services/BookService';
import { authorToString } from '../utils';

interface BookFormValues {
  title: string;
  author: AuthorOptionType | null;
  publishedYear: number;
  availableCopies: number;
  genres: GenreOptionType[];
}

const mapFormValuesToDto = (values: BookFormValues): BookRequestDto => {
  return {
    title: values.title,
    authorId: values.author?.value || '',
    publishedYear: isNaN(values.publishedYear) ? null : values.publishedYear,
    availableCopies: values.availableCopies,
    genreIds: values.genres.map((genreOption) => genreOption.value),
  };
};

const BookPage = () => {
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const isEditMode = searchParams.get('edit') !== null;
  const from = useReactRouterStateFrom('/books');
  const [book, setBook] = useState<BookDto>({
    id: '',
    title: '',
    author: {} as AuthorDto,
    publishedYear: 0,
    availableCopies: 0,
    genres: [],
  });
  const [bookFormValues, setBookFormValues] = useState<BookFormValues>({} as BookFormValues);

  const fetchBook = (id: string) => {
    setLoading(true);
    setError(false);

    BookService.getById(id)
      .then((response) => {
        const bookData = response.data;
        setBook(bookData);
        setBookFormValues({
          title: bookData.title,
          author: bookData.author ? mapAuthorToOption(bookData.author) : null,
          publishedYear: bookData.publishedYear,
          availableCopies: bookData.availableCopies,
          genres: bookData.genres.map(mapGenreToOption),
        });
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    console.log('id:', id);
    if (id) {
      fetchBook(id);
    }
  }, [id]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;
    try {
      const response = await BookService.update(id, mapFormValuesToDto(bookFormValues));
      fetchBook(response.data.id);
      searchParams.delete('edit');
      setSearchParams(searchParams);
    } catch {
      setError(true);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center mt-5">
        <Loader />
      </div>
    );
  }

  return (
    <div className="container max-w-5xl mx-auto p-6">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold text-gray-700">Информация о книге</h2>
        {error ? (
          <div className="flex bg-red-100 text-red-500 font-semibold p-4 rounded-lg">
            Ошибка загрузки данных
          </div>
        ) : (
          <>
            {!isEditMode ? (
              <div className="mt-4">
                <p>
                  <strong>Название:</strong> {book.title}
                </p>
                <p>
                  <strong>Автор:</strong> {authorToString(book.author)}
                </p>
                <p>
                  <strong>Год публикации:</strong> {book.publishedYear || '–'}
                </p>
                <p>
                  <strong>Количество копий:</strong> {book.availableCopies}
                </p>
                <p>
                  <strong>Жанры:</strong> {book.genres.map((genre) => genre.name).join(', ') || '–'}
                </p>
                <div className="mt-6 flex gap-4">
                  <StateFromLink
                    to={`?edit`}
                    className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
                  >
                    Редактировать
                  </StateFromLink>
                  <Link
                    to={from}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700"
                  >
                    Назад
                  </Link>
                </div>
              </div>
            ) : (
              <form onSubmit={handleSave} className="mt-4">
                <div className="mb-4">
                  <label className="block text-gray-700">Название</label>
                  <input
                    type="text"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={bookFormValues.title}
                    onChange={(e) =>
                      setBookFormValues({ ...bookFormValues, title: e.target.value })
                    }
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700">Автор</label>
                  <AuthorAsyncPaginate
                    value={bookFormValues.author}
                    onChange={(author) => setBookFormValues({ ...bookFormValues, author })}
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700">Год публикации</label>
                  <input
                    type="number"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={bookFormValues.publishedYear || ''}
                    onChange={(e) =>
                      setBookFormValues({
                        ...bookFormValues,
                        publishedYear: Number(e.target.value),
                      })
                    }
                    min="0"
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700">Количество копий</label>
                  <input
                    type="number"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={bookFormValues.availableCopies}
                    onChange={(e) =>
                      setBookFormValues({
                        ...bookFormValues,
                        availableCopies: Number(e.target.value),
                      })
                    }
                    min="0"
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700">Жанры</label>
                  <GenreMultiAsyncPaginate
                    value={bookFormValues.genres}
                    onChange={(newValues) =>
                      setBookFormValues({ ...bookFormValues, genres: [...newValues] })
                    }
                  />
                </div>

                <div className="flex gap-4">
                  <button
                    type="submit"
                    className="bg-sky-400 text-white px-4 py-2 rounded hover:bg-sky-500"
                  >
                    Сохранить
                  </button>
                  <Link
                    to={from}
                    className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700"
                  >
                    Отмена
                  </Link>
                </div>
              </form>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default BookPage;

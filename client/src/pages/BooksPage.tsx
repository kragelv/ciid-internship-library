import { FormEvent, MouseEvent, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router';

import { AuthorAsyncPaginate, AuthorOptionType } from '../components/AuthorAsyncPaginate';
import Loader from '../components/Loader';
import Pagination from '../components/Pagination';
import { BookDto } from '../dtos/book/BookDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import BookService from '../services/BookService';
import { authorToString, calcTotalPages } from '../utils';
import StateFromLink from '../components/StateFromLink';
import { GenreMultiAsyncPaginate, GenreOptionType } from '../components/GenreMultiAsyncPaginate';
import { BookRequestDto } from '../dtos/book/BookRequestDto';

const extractQueryParams = (searchParams: URLSearchParams): PageQueryParams => {
  const queryParams: PageQueryParams = {
    limit: Number(searchParams.get('limit')) || BookService.DEFAULT_LIMIT,
    page: Number(searchParams.get('page')) || 1,
  };
  return queryParams;
};

interface BookFormValues {
  title: string;
  author: AuthorOptionType | null;
  publishedYear: number;
  availableCopies: number;
  genres: GenreOptionType[];
}

const initialValues: BookFormValues = {
  title: '',
  author: null,
  publishedYear: NaN,
  availableCopies: NaN,
  genres: [],
};

const mapFormValuesToDto = (values: BookFormValues): BookRequestDto => {
  return {
    title: values.title,
    authorId: values.author?.value || '',
    publishedYear: isNaN(values.publishedYear) ? null : values.publishedYear,
    availableCopies: values.availableCopies,
    genreIds: values.genres.map((genreOption) => genreOption.value),
  };
};

const BooksPage = () => {
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const [books, setBooks] = useState<BookDto[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [bookFormValues, setBookFormValues] = useState(initialValues);

  const [searchParams, setSearchParams] = useSearchParams();
  const [queryParams, setQueryParams] = useState<PageQueryParams>({
    page: 1,
    limit: BookService.DEFAULT_LIMIT,
  });

  useEffect(() => {
    setQueryParams(extractQueryParams(searchParams));
  }, [searchParams]);

  useEffect(() => {
    fetchPage(queryParams);
  }, [queryParams]);

  const updateSearchParams = (newPageParams: PageQueryParams) => {
    const searchParams = new URLSearchParams();
    if (newPageParams.page) {
      searchParams.set('page', newPageParams.page.toString());
    }
    if (newPageParams.limit && newPageParams.limit !== BookService.DEFAULT_LIMIT) {
      searchParams.set('limit', newPageParams.limit.toString());
    }
    setSearchParams(searchParams);
  };

  const handleCreateBook = async (e: FormEvent) => {
    e.preventDefault();
    await BookService.create(mapFormValuesToDto(bookFormValues));
    setBookFormValues(initialValues);
    setIsFormVisible(false);
    fetchPage(queryParams);
  };

  const fetchPage = (queryParams: PageQueryParams) => {
    setError(false);
    setLoading(true);
    BookService.getPage(queryParams)
      .then((response) => {
        setBooks(response.data.data);
        setTotalCount(response.data.total);
        setTotalPages(
          calcTotalPages(response.data.total, queryParams.limit, BookService.DEFAULT_LIMIT),
        );
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleDeleteBook = async (e: MouseEvent<HTMLButtonElement>) => {
    const { id } = e.currentTarget.dataset;
    if (id === undefined) return;
    if (window.confirm('Вы уверены, что хотите удалить книгу?')) {
      await BookService.delete(id);
      fetchPage(queryParams);
    }
  };

  const handlePageChange = (page: number) => {
    updateSearchParams({ ...queryParams, page });
  };

  return (
    <div className="max-w-9/10 mx-auto">
      {loading ? (
        <div className="flex justify-center mt-5">
          <Loader />
        </div>
      ) : (
        <>
          {isFormVisible && (
            <div className="max-w-5xl mx-auto p-6 rounded-lg shadow-md mb-6">
              <h3 className="text-xl font-semibold text-gray-800 mb-8">Добавить Книгу</h3>
              <form onSubmit={handleCreateBook}>
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Название</label>
                  <input
                    type="text"
                    value={bookFormValues.title}
                    onChange={(e) =>
                      setBookFormValues({ ...bookFormValues, title: e.target.value })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Автор</label>
                  <AuthorAsyncPaginate
                    value={bookFormValues.author}
                    onChange={(newValue: AuthorOptionType | null) => {
                      setBookFormValues({
                        ...bookFormValues,
                        author: newValue,
                      });
                    }}
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Год публикации</label>
                  <input
                    type="number"
                    value={bookFormValues.publishedYear}
                    onChange={(e) =>
                      setBookFormValues({
                        ...bookFormValues,
                        publishedYear: Number(e.target.value),
                      })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Количество копий</label>
                  <input
                    type="number"
                    value={bookFormValues.availableCopies}
                    onChange={(e) =>
                      setBookFormValues({
                        ...bookFormValues,
                        availableCopies: Number(e.target.value),
                      })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    required
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Жанры</label>
                  <GenreMultiAsyncPaginate
                    value={bookFormValues.genres}
                    onChange={(newValues) => {
                      setBookFormValues({
                        ...bookFormValues,
                        genres: [...newValues],
                      });
                    }}
                  />
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    type="submit"
                    className="bg-green-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-green-700"
                  >
                    Создать
                  </button>
                  <button
                    type="button"
                    onClick={() => setIsFormVisible(false)}
                    className="bg-gray-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-gray-700"
                  >
                    Отмена
                  </button>
                </div>
              </form>
            </div>
          )}

          <div className="p-6 rounded-lg shadow-lg mt-3">
            <div className="flex mb-4">
              <h2 className="text-2xl font-bold text-gray-800">
                {!error &&
                  (totalCount > 0 ? (
                    <>Книги ({totalCount})</>
                  ) : (
                    <>Пусто, необходимо добавить книги</>
                  ))}
              </h2>
              {!isFormVisible && (
                <button
                  onClick={() => setIsFormVisible(true)}
                  className="bg-blue-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-blue-700 ms-auto"
                >
                  Добавить
                </button>
              )}
            </div>
            {error ? (
              <>
                <div className="flex bg-red-100 text-red-500 font-semibold p-4 rounded-lg">
                  Ошибка загрузки данных
                </div>
              </>
            ) : (
              <>
                <table className="w-full border-collapse border border-gray-200 rounded-lg overflow-hidden">
                  <thead>
                    <tr className="bg-gray-200 text-gray-700 uppercase text-sm">
                      <th className="py-3 px-4 border border-gray-300 w-[30%]">Автор</th>
                      <th className="py-3 px-4 border border-gray-300 w-[40%]">Название</th>
                      <th className="py-3 px-4 border border-gray-300 w-">Год публикации</th>
                      <th className="py-3 px-4 border border-gray-300 w-">Кол-во копий</th>
                      <th className="py-3 px-4 border border-gray-300 w-[10%]">Жанры</th>
                      <th className="py-3 px-4 border border-gray-300 w-[10%]">Действия</th>
                    </tr>
                  </thead>
                  <tbody>
                    {books
                      .map((book) => ({
                        ...book,
                        auhtorString: authorToString(book.author),
                        genresLine: book.genres.map((genre) => genre.name).join(', '),
                      }))
                      .map((book) => (
                        <tr
                          className="border border-gray-300 bg-white hover:bg-gray-100 transition"
                          key={book.id}
                        >
                          <td className="py-3 px-4 border border-gray-300">{book.auhtorString}</td>
                          <td className="py-3 px-4 border border-gray-300">
                            <StateFromLink
                              to={`/books/${book.id}`}
                              className="block w-full h-ful hover:text-blue-700"
                            >
                              <span className="text-blue-500">↗</span>
                              <span>{book.title}</span>
                            </StateFromLink>
                          </td>
                          <td className="py-3 px-4 border border-gray-300">{book.publishedYear}</td>
                          <td className="py-3 px-4 border border-gray-300">
                            {book.availableCopies}
                          </td>
                          <td className="py-3 px-4 border border-gray-300">
                            <span className="line-clamp-2" title={book.genresLine}>
                              {book.genresLine}
                            </span>
                          </td>
                          <td className="py-3 px-4 border border-gray-300">
                            <div className="flex gap-1">
                              <StateFromLink
                                to={`/books/${book.id}?edit`}
                                className="border-blue-500 border-2 text-white font-semibold py-2 px-3 rounded-lg hover:bg-blue-500 transition duration-300 ease-in-out"
                              >
                                ✏️
                              </StateFromLink>
                              <button
                                onClick={handleDeleteBook}
                                data-id={book.id}
                                className="border-red-500 border-2 text-white font-semibold py-2 px-3 rounded-lg hover:bg-red-500 transition duration-300 ease-in-out"
                              >
                                🗑️
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>
                {totalCount > 0 && (
                  <Pagination
                    currentPage={queryParams.page}
                    totalPages={totalPages}
                    onPageChange={(newPage) => handlePageChange(newPage)}
                  />
                )}
              </>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default BooksPage;

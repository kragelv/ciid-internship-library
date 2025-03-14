import { FormEvent, MouseEvent, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router';

import Loader from '../components/Loader';
import Pagination from '../components/Pagination';
import StateFromLink from '../components/StateFromLink';
import { AuthorDto } from '../dtos/author/AuthorDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import AuthorService from '../services/AuthorService';
import { calcTotalPages } from '../utils';

const extractQueryParams = (searchParams: URLSearchParams): PageQueryParams => {
  const queryParams: PageQueryParams = {
    page: Number(searchParams.get('page')) || 1,
    limit: Number(searchParams.get('limit')) || AuthorService.DEFAULT_LIMIT,
  };
  return queryParams;
};

const AuthorsPage = () => {
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const [authors, setAuthors] = useState<AuthorDto[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [authorFormValues, setAuthorFormValues] = useState({
    firstName: '',
    middleName: '',
    lastName: '',
    birthYear: NaN,
  });
  const [searchParams, setSearchParams] = useSearchParams();
  const [queryParams, setQueryParams] = useState<PageQueryParams>(extractQueryParams(searchParams));

  useEffect(() => {
    setQueryParams((prev) => {
      const queryParams = extractQueryParams(searchParams);
      return { ...prev, ...queryParams };
    });
  }, [searchParams]);

  useEffect(() => {
    fetchPage({ ...queryParams });
  }, [queryParams]);

  const updateSearchParams = (newPageParams: PageQueryParams) => {
    const searchParams = new URLSearchParams();

    if (newPageParams.page) {
      searchParams.set('page', newPageParams.page.toString());
    }

    if (newPageParams.limit && newPageParams.limit !== AuthorService.DEFAULT_LIMIT) {
      searchParams.set('limit', newPageParams.limit.toString());
    }

    setSearchParams(searchParams);
  };

  const handleCreateAuthor = async (e: FormEvent) => {
    e.preventDefault();
    await AuthorService.create(authorFormValues);
    setAuthorFormValues({
      firstName: '',
      middleName: '',
      lastName: '',
      birthYear: 0,
    });
    setIsFormVisible(false);
    fetchPage(queryParams);
  };

  const fetchPage = (queryParams: PageQueryParams) => {
    setError(false);
    setLoading(true);
    AuthorService.getPage(queryParams)
      .then((response) => {
        setAuthors(response.data.data);
        setTotalCount(response.data.total);
        setTotalPages(
          calcTotalPages(response.data.total, queryParams.limit, AuthorService.DEFAULT_LIMIT),
        );
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleDeleteAuthor = async (e: MouseEvent<HTMLButtonElement>) => {
    const { id } = e.currentTarget.dataset;
    if (id === undefined) return;
    if (window.confirm('Вы уверены, что хотите удалить автора?')) {
      await AuthorService.delete(id);
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
              <h3 className="text-xl font-semibold text-gray-800 mb-8">Добавить Автора</h3>
              <form onSubmit={handleCreateAuthor}>
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Фамилия</label>
                  <input
                    type="text"
                    value={authorFormValues.lastName}
                    onChange={(e) =>
                      setAuthorFormValues({ ...authorFormValues, lastName: e.target.value })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Имя</label>
                  <input
                    type="text"
                    value={authorFormValues.firstName}
                    onChange={(e) =>
                      setAuthorFormValues({ ...authorFormValues, firstName: e.target.value })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Отчество</label>
                  <input
                    type="text"
                    value={authorFormValues.middleName}
                    onChange={(e) =>
                      setAuthorFormValues({ ...authorFormValues, middleName: e.target.value })
                    }
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                  />
                </div>

                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Год рождения</label>
                  <input
                    type="number"
                    value={authorFormValues.birthYear}
                    onChange={(e) =>
                      setAuthorFormValues({
                        ...authorFormValues,
                        birthYear: Number(e.target.value),
                      })
                    }
                    min="0"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    required
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
                    <>Авторы ({totalCount})</>
                  ) : (
                    <>Пусто, необходимо добавить авторов</>
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
                      <th className="py-3 px-4 border border-gray-300">Фамилия</th>
                      <th className="py-3 px-4 border border-gray-300">Имя</th>
                      <th className="py-3 px-4 border border-gray-300">Отчество</th>
                      <th className="py-3 px-4 border border-gray-300 w-[10%]">Год рождения</th>
                      <th className="py-3 px-4 border border-gray-300 w-[10%]">Действия</th>
                    </tr>
                  </thead>
                  <tbody>
                    {authors.map((author) => (
                      <tr
                        className="border border-gray-300 bg-white hover:bg-gray-100 transition"
                        key={author.id}
                      >
                        <td className="py-3 px-4 border border-gray-300">
                          <StateFromLink
                            to={`/authors/${author.id}`}
                            className="block w-full h-ful hover:text-blue-700"
                          >
                            <span className="text-blue-500">↗</span>
                            <span>{author.lastName}</span>
                          </StateFromLink>
                        </td>
                        <td className="py-3 px-4 border border-gray-300">{author.firstName}</td>
                        <td className="py-3 px-4 border border-gray-300">{author.middleName}</td>
                        <td className="py-3 px-4 border border-gray-300">{author.birthYear}</td>
                        <td className="py-3 px-4 border border-gray-300">
                          <div className="flex gap-1">
                            <StateFromLink
                              to={`/authors/${author.id}?edit`}
                              className="border-blue-500 border-2 text-white font-semibold py-2 px-3 rounded-lg hover:bg-blue-500 transition duration-300 ease-in-out"
                            >
                              ✏️
                            </StateFromLink>
                            <button
                              onClick={handleDeleteAuthor}
                              data-id={author.id}
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

export default AuthorsPage;

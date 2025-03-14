import { FormEvent, MouseEvent, useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router';

import Loader from '../components/Loader';
import Pagination from '../components/Pagination';
import StateFromLink from '../components/StateFromLink';
import { GenreDto } from '../dtos/genre/GenreDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import GenreService from '../services/GenreService';
import { calcTotalPages } from '../utils';

const extractQueryParams = (searchParams: URLSearchParams): PageQueryParams => {
  const queryParams: PageQueryParams = {
    page: Number(searchParams.get('page')) || 1,
    limit: Number(searchParams.get('limit')) || GenreService.DEFAULT_LIMIT,
  };
  return queryParams;
};

const GenresPage = () => {
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const [genres, setGenres] = useState<GenreDto[]>([]);
  const [totalCount, setTotalCount] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isFormVisible, setIsFormVisible] = useState(false);
  const [genreName, setGenreName] = useState('');
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

    if (newPageParams.limit && newPageParams.limit !== GenreService.DEFAULT_LIMIT) {
      searchParams.set('limit', newPageParams.limit.toString());
    }

    setSearchParams(searchParams);
  };

  const handleCreateGenre = async (e: FormEvent) => {
    e.preventDefault();
    await GenreService.create({ name: genreName });
    setGenreName('');
    setIsFormVisible(false);
    fetchPage(queryParams);
  };

  const fetchPage = (queryParams: PageQueryParams) => {
    setError(false);
    setLoading(true);
    GenreService.getPage(queryParams)
      .then((response) => {
        setGenres(response.data.data);
        setTotalCount(response.data.total);
        setTotalPages(
          calcTotalPages(response.data.total, queryParams.limit, GenreService.DEFAULT_LIMIT),
        );
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleDeleteGenre = async (e: MouseEvent<HTMLButtonElement>) => {
    const { id } = e.currentTarget.dataset;
    if (id === undefined) return;
    if (window.confirm('Вы уверены, что хотите удалить жанр?')) {
      await GenreService.delete(id);
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
              <h3 className="text-xl font-semibold text-gray-800 mb-8">Добавить Жанр</h3>
              <form onSubmit={handleCreateGenre}>
                <div className="mb-4">
                  <label className="block text-gray-700 font-medium">Название</label>
                  <input
                    type="text"
                    value={genreName}
                    onChange={(e) => setGenreName(e.target.value)}
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
                    <>Жанры ({totalCount})</>
                  ) : (
                    <>Пусто, необходимо добавить жанры</>
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
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                  {genres.map((genre) => (
                    <div
                      key={genre.id}
                      className="bg-gray-100 p-2 rounded-lg shadow-md flex flex-col gap-1"
                    >
                      <StateFromLink
                        to={`/genres/${genre.id}`}
                        className="block text-gray-800 bg-gray-200 rounded-lg p-2 hover:text-blue-700"
                      >
                        <h3 className="text-lg font-semibold">{genre.name}</h3>
                      </StateFromLink>
                      <div className="flex align-items-baseline">
                        <div className="flex gap-1 ml-auto">
                          <StateFromLink
                            to={`/genres/${genre.id}?edit`}
                            className="block border-blue-500 border-2 text-white font-semibold py-1 px-2 rounded-md hover:bg-blue-500"
                          >
                            ✏️
                          </StateFromLink>
                          <button
                            onClick={handleDeleteGenre}
                            data-id={genre.id}
                            className="border-red-500 border-2 text-white font-semibold py-1 px-2 rounded-md hover:bg-red-500"
                          >
                            🗑️
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
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

export default GenresPage;

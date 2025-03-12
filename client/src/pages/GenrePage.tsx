import { useEffect, useState } from 'react';
import { useSearchParams, useParams, Link } from 'react-router';

import Loader from '../components/Loader';
import StateFromLink from '../components/StateFromLink';
import { GenreDto } from '../dtos/genre/GenreDto';
import { useReactRouterStateFrom } from '../hooks';
import GenreService from '../services/GenreService';

const GenrePage = () => {
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const from = useReactRouterStateFrom('/genres');
  const isEditMode = searchParams.get('edit') !== null;

  const [genre, setGenre] = useState<GenreDto>({} as GenreDto);

  const [name, setName] = useState('');

  const fetchGenre = (id: string) => {
    setError(false);
    setLoading(true);
    GenreService.getById(id)
      .then((response) => {
        setGenre(response.data);
        setName(response.data.name);
      })
      .catch(() => {
        setError(true);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    if (id) {
      fetchGenre(id);
    }
  }, [id]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!genre) return;

    const response = await GenreService.update(genre.id, { name });
    setGenre(response.data);
    setName(response.data.name);
    searchParams.delete('edit');
    setSearchParams(searchParams);
  };

  if (loading)
    <div className="flex justify-center mt-5">
      <Loader />
    </div>;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold text-gray-700">Информация о жанре</h2>
        {error ? (
          <div className="flex bg-red-100 text-red-500 font-semibold p-4 rounded-lg">
            Ошибка загрузки данных
          </div>
        ) : (
          <>
            {!isEditMode ? (
              <div className="mt-4">
                <p>
                  <strong>Название:</strong> {genre.name}
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
                  <label htmlFor="name" className="block text-gray-700">
                    Название
                  </label>
                  <input
                    type="text"
                    id="name"
                    name="name"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
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

export default GenrePage;

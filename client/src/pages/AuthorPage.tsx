import { useEffect, useState } from 'react';
import { useSearchParams, useParams, Link } from 'react-router';

import Loader from '../components/Loader';
import StateFromLink from '../components/StateFromLink';
import { AuthorDto } from '../dtos/author/AuthorDto';
import { useReactRouterStateFrom } from '../hooks';
import AuthorService from '../services/AuthorService';

const AuthorPage = () => {
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(false);
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const from = useReactRouterStateFrom('/authors');
  const isEditMode = searchParams.get('edit') !== null;

  const [author, setAuthor] = useState<AuthorDto>({} as AuthorDto);
  const [firstName, setFirstName] = useState('');
  const [middleName, setMiddleName] = useState('');
  const [lastName, setLastName] = useState('');
  const [birthYear, setBirthYear] = useState(0);

  const fetchAuthor = (id: string) => {
    setError(false);
    setLoading(true);
    AuthorService.getById(id)
      .then((response) => {
        setAuthor(response.data);
        setFirstName(response.data.firstName);
        setMiddleName(response.data.middleName);
        setLastName(response.data.lastName);
        setBirthYear(response.data.birthYear);
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
      fetchAuthor(id);
    }
  }, [id]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!author) return;

    const updatedAuthor = {
      ...author,
      firstName,
      middleName,
      lastName,
      birthYear,
    };

    const response = await AuthorService.update(author.id, updatedAuthor);
    setAuthor(response.data);
    setFirstName(response.data.firstName);
    setMiddleName(response.data.middleName);
    setLastName(response.data.lastName);
    setBirthYear(response.data.birthYear);
    searchParams.delete('edit');
    setSearchParams(searchParams);
  };

  if (loading) {
    return (
      <div className="flex justify-center mt-5">
        <Loader />
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto p-6">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold text-gray-700">Информация о авторе</h2>
        {error ? (
          <div className="flex bg-red-100 text-red-500 font-semibold p-4 rounded-lg">
            Ошибка загрузки данных
          </div>
        ) : (
          <>
            {!isEditMode ? (
              <div className="mt-4">
                <p>
                  <strong>Фамилия:</strong> {author.lastName}
                </p>
                <p>
                  <strong>Имя:</strong> {author.firstName || '–'}
                </p>
                <p>
                  <strong>Отчество:</strong> {author.middleName || '–'}
                </p>
                <p>
                  <strong>Год рождения:</strong> {author.birthYear}
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
                  <label htmlFor="lastName" className="block text-gray-700">
                    Фамилия
                  </label>
                  <input
                    type="text"
                    id="lastName"
                    name="lastName"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                  />
                </div>
                <div className="mb-4">
                  <label htmlFor="firstName" className="block text-gray-700">
                    Имя
                  </label>
                  <input
                    type="text"
                    id="firstName"
                    name="firstName"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-4">
                  <label htmlFor="middleName" className="block text-gray-700">
                    Отчество
                  </label>
                  <input
                    type="text"
                    id="middleName"
                    name="middleName"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={middleName}
                    onChange={(e) => setMiddleName(e.target.value)}
                  />
                </div>
                <div className="mb-4">
                  <label htmlFor="birthYear" className="block text-gray-700">
                    Год рождения
                  </label>
                  <input
                    type="number"
                    id="birthYear"
                    name="birthYear"
                    className="mt-2 p-2 border border-gray-300 rounded-lg w-full"
                    value={birthYear}
                    onChange={(e) => setBirthYear(Number(e.target.value))}
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

export default AuthorPage;

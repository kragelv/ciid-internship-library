import { NavLink } from 'react-router';

const Navbar: React.FC = () => {
  return (
    <header className="bg-blue-600 px-4">
      <div className="max-w-9/10 flex gap-7">
        <div className="my-5">
          <h2 className="text-white text-2xl font-bold">Library</h2>
        </div>
        <div className="flex mr-auto items-stretch">
          <NavLink
            to={'/books'}
            end
            className={({ isActive }) =>
              `${isActive ? 'bg-blue-700 text-white pointer-events-none' : ''} text-white text-lg inline-flex justify-center px-5 items-center font-semibold hover:text-blue-200 transition duration-300 `
            }
          >
            Книги
          </NavLink>

          <NavLink
            to={'/authors'}
            end
            className={({ isActive }) =>
              `${isActive ? 'bg-blue-700 text-white pointer-events-none' : ''} text-white text-lg inline-flex justify-center px-5 items-center font-semibold hover:text-blue-200 transition duration-300 `
            }
          >
            Авторы
          </NavLink>

          <NavLink
            to={'/genres'}
            end
            className={({ isActive }) =>
              `${isActive ? 'bg-blue-700 text-white pointer-events-none' : ''} text-white text-lg inline-flex justify-center px-5 items-center font-semibold hover:text-blue-200 transition duration-300 `
            }
          >
            Жанры
          </NavLink>
        </div>
      </div>
    </header>
  );
};

export default Navbar;

import { useTitle } from '../hooks';

const NotFoundPage = () => {
  useTitle('Страница не найдена');
  return (
    <div>
      <h1>404</h1>
      <p>Страница не найдена</p>
    </div>
  );
};

export default NotFoundPage;

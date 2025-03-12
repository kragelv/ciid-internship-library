import { AsyncPaginate } from 'react-select-async-paginate';
import AuthorService from '../services/AuthorService';
import { authorToString } from '../utils';

export interface AuthorOptionInfo {
  id: string;
  firstName: string;
  middleName: string;
  lastName: string;
}

export type AuthorOptionType = {
  label: string;
  value: string;
};

export const authorToLabel = (author: AuthorOptionInfo) => authorToString(author);

export const mapAuthorToOption = (author: AuthorOptionInfo): AuthorOptionType => ({
  label: authorToLabel(author),
  value: author.id,
});

export type AuthorAsyncPaginateAdditional = {
  page: number;
};

const loadOptions = async (
  _search: string,
  _prevOptions: unknown,
  additional?: AuthorAsyncPaginateAdditional,
) => {
  const page = additional?.page || 1;

  const response = await AuthorService.getPage({ page });
  const options = response.data.data.map(mapAuthorToOption);

  return {
    options,
    hasMore: response.data.total > page * AuthorService.DEFAULT_LIMIT,
    additional: {
      page: page + 1,
    },
  };
};

export type AuthorAsyncPaginateProps = {
  value: AuthorOptionType | null;
  onChange: (newValue: AuthorOptionType | null) => void;
};

export const AuthorAsyncPaginate = ({ value, onChange }: AuthorAsyncPaginateProps) => {
  return (
    <AsyncPaginate
      value={value}
      onChange={onChange}
      loadOptions={loadOptions}
      additional={{ page: 1 }}
    />
  );
};

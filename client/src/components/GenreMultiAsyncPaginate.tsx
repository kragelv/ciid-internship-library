import { ActionMeta, MultiValue } from 'react-select';
import { AsyncPaginate } from 'react-select-async-paginate';
import GenreService from '../services/GenreService';

export interface GenreOptionInfo {
  id: string;
  name: string;
}

export type GenreOptionType = {
  label: string;
  value: string;
};

const genreToLabel = (genre: { name: string }) => {
  return genre.name;
};

export const mapGenreToOption = (genre: GenreOptionInfo): GenreOptionType => ({
  label: genreToLabel(genre),
  value: genre.id,
});

export type GenreAsyncPaginateAdditional = {
  page: number;
};

const loadOptions = async (
  _search: string,
  _prevOptions: unknown,
  additional?: GenreAsyncPaginateAdditional,
) => {
  const page = additional?.page || 1;

  const response = await GenreService.getPage({ page });
  const options = response.data.data.map(mapGenreToOption);

  return {
    options,
    hasMore: response.data.total > page * GenreService.DEFAULT_LIMIT,
    additional: {
      page: page + 1,
    },
  };
};

export type GenreAsyncPaginateProps = {
  value: GenreOptionType[];
  onChange: (
    newValue: MultiValue<GenreOptionType>,
    actionMeta: ActionMeta<GenreOptionType>,
  ) => void;
};

export const GenreMultiAsyncPaginate = ({ value, onChange }: GenreAsyncPaginateProps) => {
  return (
    <AsyncPaginate
      isMulti={true}
      value={value}
      onChange={onChange}
      loadOptions={loadOptions}
      additional={{ page: 1 }}
    />
  );
};

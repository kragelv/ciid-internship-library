import { AxiosResponse } from 'axios';

import { GenreDto } from '../dtos/genre/GenreDto';
import { GenreRequestDto } from '../dtos/genre/GenreRequestDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import { PagedResponse } from '../dtos/page/PageResponse';
import $api from '../http';

class GenreService {
  static readonly DEFAULT_LIMIT = 20;

  static async getPage(params: PageQueryParams): Promise<AxiosResponse<PagedResponse<GenreDto>>> {
    const limit = params.limit === this.DEFAULT_LIMIT ? undefined : params.limit;
    return $api.get('/v1/genres', {
      params: { page: params.page, limit },
    });
  }

  static async getById(id: string): Promise<AxiosResponse<GenreDto>> {
    return $api.get(`/v1/genres/${id}`);
  }

  static async create(createGenreDto: GenreRequestDto): Promise<AxiosResponse<string>> {
    return $api.post('/v1/genres', createGenreDto);
  }

  static async update(
    id: string,
    updateGenreDto: GenreRequestDto,
  ): Promise<AxiosResponse<GenreDto>> {
    return $api.put(`/v1/genres/${id}`, updateGenreDto);
  }

  static async delete(id: string): Promise<AxiosResponse<void>> {
    return $api.delete(`/v1/genres/${id}`);
  }
}

export default GenreService;

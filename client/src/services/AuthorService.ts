import { AxiosResponse } from 'axios';

import { AuthorDto } from '../dtos/author/AuthorDto';
import { AuthorRequestDto } from '../dtos/author/AuthorRequestDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import { PagedResponse } from '../dtos/page/PageResponse';
import $api from '../http';

class AuthorService {
  static readonly DEFAULT_LIMIT = 15;

  static async getPage(params: PageQueryParams): Promise<AxiosResponse<PagedResponse<AuthorDto>>> {
    const limit = params.limit === this.DEFAULT_LIMIT ? undefined : params.limit;
    return $api.get('/v1/authors', {
      params: { page: params.page, limit },
    });
  }

  static async getById(id: string): Promise<AxiosResponse<AuthorDto>> {
    return $api.get(`/v1/authors/${id}`);
  }

  static async create(createAuthorDto: AuthorRequestDto): Promise<AxiosResponse<string>> {
    return $api.post('/v1/authors', createAuthorDto);
  }

  static async update(
    id: string,
    updateAuthorDto: AuthorRequestDto,
  ): Promise<AxiosResponse<AuthorDto>> {
    return $api.put(`/v1/authors/${id}`, updateAuthorDto);
  }

  static async delete(id: string): Promise<AxiosResponse<void>> {
    return $api.delete(`/v1/authors/${id}`);
  }
}

export default AuthorService;

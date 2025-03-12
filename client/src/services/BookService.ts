import { AxiosResponse } from 'axios';

import { BookDto } from '../dtos/book/BookDto';
import { BookRequestDto } from '../dtos/book/BookRequestDto';
import { PageQueryParams } from '../dtos/page/PageQueryParams';
import { PagedResponse } from '../dtos/page/PageResponse';
import $api from '../http';

class BookService {
  static readonly DEFAULT_LIMIT = 15;

  static async getPage(params: PageQueryParams): Promise<AxiosResponse<PagedResponse<BookDto>>> {
    const limit = params.limit === this.DEFAULT_LIMIT ? undefined : params.limit;
    return $api.get('/v1/books', {
      params: { page: params.page, limit },
    });
  }

  static async getById(id: string): Promise<AxiosResponse<BookDto>> {
    return $api.get(`/v1/books/${id}`);
  }

  static async create(createBookDto: BookRequestDto): Promise<AxiosResponse<string>> {
    return $api.post('/v1/books', createBookDto);
  }

  static async update(id: string, updateBookDto: BookRequestDto): Promise<AxiosResponse<BookDto>> {
    return $api.put(`/v1/books/${id}`, updateBookDto);
  }

  static async delete(id: string): Promise<AxiosResponse<void>> {
    return $api.delete(`/v1/books/${id}`);
  }
}

export default BookService;

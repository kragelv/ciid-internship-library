import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageQueryParams } from '../../../shared/models/page/page-query-params.model';
import { PageResponse } from '../../../shared/models/page/page-response.model';
import { Book, BookRequest } from '../models/book.model';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly apiPath = `/v1/books`;
  static readonly DEFAULT_LIMIT = 20;

  constructor(private http: HttpClient) {}

  getPage(params: PageQueryParams): Observable<PageResponse<Book>> {
    let httpParams = new HttpParams().set('page', params.page);

    if (params.limit && params.limit !== BookService.DEFAULT_LIMIT) {
      httpParams = httpParams.set('limit', params.limit);
    }

    return this.http.get<PageResponse<Book>>(this.apiPath, {
      params: httpParams,
    });
  }

  getById(id: string): Observable<Book> {
    return this.http.get<Book>(`${this.apiPath}/${id}`);
  }

  create(createBookDto: BookRequest): Observable<string> {
    return this.http.post<string>(this.apiPath, createBookDto);
  }

  update(id: string, updateBookDto: BookRequest): Observable<Book> {
    return this.http.put<Book>(`${this.apiPath}/${id}`, updateBookDto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiPath}/${id}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageQueryParams } from '../../../shared/models/page/page-query-params.model';
import { PageResponse } from '../../../shared/models/page/page-response.model';
import { Author, AuthorRequest } from '../models/author.model';

@Injectable({
  providedIn: 'root',
})
export class AuthorService {
  private readonly apiPath = `/v1/authors`;
  static readonly DEFAULT_LIMIT = 15;

  constructor(private http: HttpClient) {}

  getPage(params: PageQueryParams): Observable<PageResponse<Author>> {
    let httpParams = new HttpParams().set('page', params.page);

    if (params.limit && params.limit !== AuthorService.DEFAULT_LIMIT) {
      httpParams = httpParams.set('limit', params.limit);
    }

    return this.http.get<PageResponse<Author>>(this.apiPath, {
      params: httpParams,
    });
  }

  getById(id: string): Observable<Author> {
    return this.http.get<Author>(`${this.apiPath}/${id}`);
  }

  create(createAuthorDto: AuthorRequest): Observable<string> {
    return this.http.post<string>(this.apiPath, createAuthorDto);
  }

  update(id: string, updateAuthorDto: AuthorRequest): Observable<Author> {
    return this.http.put<Author>(`${this.apiPath}/${id}`, updateAuthorDto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiPath}/${id}`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageQueryParams } from '../../../shared/models/page/page-query-params.model';
import { PageResponse } from '../../../shared/models/page/page-response.model';
import { Genre, GenreRequest } from '../models/genre.model';

@Injectable({
  providedIn: 'root',
})
export class GenreService {
  private readonly apiPath = `/v1/genres`;
  static readonly DEFAULT_LIMIT = 20;

  constructor(private http: HttpClient) {}

  getPage(params: PageQueryParams): Observable<PageResponse<Genre>> {
    let httpParams = new HttpParams().set('page', params.page);

    if (params.limit && params.limit !== GenreService.DEFAULT_LIMIT) {
      httpParams = httpParams.set('limit', params.limit);
    }

    return this.http.get<PageResponse<Genre>>(this.apiPath, {
      params: httpParams,
    });
  }

  getById(id: string): Observable<Genre> {
    return this.http.get<Genre>(`${this.apiPath}/${id}`);
  }

  create(createGenreDto: GenreRequest): Observable<string> {
    return this.http.post<string>(this.apiPath, createGenreDto);
  }

  update(id: string, updateGenreDto: GenreRequest): Observable<Genre> {
    return this.http.put<Genre>(`${this.apiPath}/${id}`, updateGenreDto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiPath}/${id}`);
  }
}

import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageQueryParams } from '../../shared/models/page/page-query-params.model';
import { PageResponse } from '../../shared/models/page/page-response.model';

export class CrudService<
  T,
  ID = string,
  CT = T,
  UT = CT,
  PQP extends PageQueryParams = PageQueryParams
> {
  constructor(
    private http: HttpClient,
    private apiPath: string,
    private defaultLimit: number = 15
  ) {}

  getApiPath(): string {
    return this.apiPath;
  }

  getDefaultLimit(): number {
    return this.defaultLimit;
  }

  getPage(params: PQP): Observable<PageResponse<T>> {
    let httpParams = new HttpParams().set('page', params.page);

    if (params.limit && params.limit !== this.defaultLimit) {
      httpParams = httpParams.set('limit', params.limit);
    }

    return this.http.get<PageResponse<T>>(this.apiPath, {
      params: httpParams,
    });
  }

  getById(id: ID): Observable<T> {
    return this.http.get<T>(`${this.apiPath}/${id}`);
  }

  create(createDto: CT): Observable<ID> {
    return this.http.post<ID>(this.apiPath, createDto);
  }

  update(id: ID, updateDto: UT): Observable<T> {
    return this.http.put<T>(`${this.apiPath}/${id}`, updateDto);
  }

  delete(id: ID): Observable<void> {
    return this.http.delete<void>(`${this.apiPath}/${id}`);
  }
}

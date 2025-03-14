import { BehaviorSubject, catchError, Observable } from 'rxjs';
import { AuthorService } from '../../authors/services/author.service';
import { PageResponse } from '../../../shared/models/page/page-response.model';
import { Author } from '../../authors/models/author.model';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthorsAsyncSelectService {
  private pageSubject = new BehaviorSubject<number>(1);
  public currentPage$ = this.pageSubject.asObservable();

  constructor(private authorService: AuthorService) {}

  loadData(page: number): Observable<PageResponse<Author>> {
    return this.authorService.getPage({ page }).pipe(
      catchError((error) => {
        console.error('Error fetching data', error);
        throw error;
      })
    );
  }

  setPage(page: number): void {
    this.pageSubject.next(page);
  }
}

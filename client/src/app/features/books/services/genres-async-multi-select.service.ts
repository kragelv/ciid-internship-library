import { BehaviorSubject, catchError, Observable } from 'rxjs';
import { PageResponse } from '../../../shared/models/page/page-response.model';
import { Injectable } from '@angular/core';
import { GenreService } from '../../genre/services/genre.service';
import { Genre } from '../../genre/models/genre.model';

@Injectable({
  providedIn: 'root',
})
export class GenresAsyncMultiSelectService {
  private pageSubject = new BehaviorSubject<number>(1);
  public currentPage$ = this.pageSubject.asObservable();

  constructor(private genreService: GenreService) {}

  loadData(page: number): Observable<PageResponse<Genre>> {
    return this.genreService.getPage({ page }).pipe(
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

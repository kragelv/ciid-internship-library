import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { catchError, map, Observable, switchMap } from 'rxjs';
import { authorToString } from '../../../../shared/utils/data.utils';
import { Author } from '../../../authors/models/author.model';
import { AuthorsAsyncSelectService } from '../../services/authors-async-select.service';

@Component({
  selector: 'app-authors-async-select',
  imports: [NgIf, NgFor, AsyncPipe],
  templateUrl: './authors-async-select.component.html',
})
export class AuthorsAsyncSelectComponent {
  data$: Observable<Author[]>;
  totalCount$: Observable<number>;
  loading: boolean = false;
  error: string | null = null;
  currentPage: number = 1;

  @Output() changeSelect: EventEmitter<string | null> = new EventEmitter<
    string | null
  >();

  constructor(private asyncSelectService: AuthorsAsyncSelectService) {
    this.data$ = this.asyncSelectService.currentPage$.pipe(
      switchMap((page) => this.asyncSelectService.loadData(page)),
      map((response) => response.data),
      catchError((error) => {
        this.error = 'Ошибка загрузки данных';
        this.loading = false;
        throw error;
      })
    );

    this.totalCount$ = this.asyncSelectService.currentPage$.pipe(
      switchMap((page) => this.asyncSelectService.loadData(page)),
      map((response) => response.total)
    );
  }

  onScroll(event: any) {
    const bottom =
      event.target.scrollHeight ===
      event.target.scrollTop + event.target.clientHeight;
    if (bottom && !this.loading) {
      this.loading = true;
      this.currentPage += 1;
      this.asyncSelectService.setPage(this.currentPage);
    }
  }

  onSelectionChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    if (!select || select.selectedIndex === -1)
      return this.changeSelect.emit(null);

    const selectedAuthorId = select.value;
    this.changeSelect.emit(selectedAuthorId);
  }

  authorToString(item: Author) {
    return authorToString(item);
  }
}

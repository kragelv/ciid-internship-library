import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { catchError, map, Observable, switchMap } from 'rxjs';
import { AuthorsAsyncSelectService } from '../../services/authors-async-select.service';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { authorToString } from '../../../../shared/utils/data.utils';

@Component({
  selector: 'app-authors-async-select',
  imports: [NgIf, NgFor, AsyncPipe],
  templateUrl: './authors-async-select.component.html',
})
export class AuthorsAsyncSelectComponent implements OnInit {
  data$: Observable<any[]>;
  totalCount$: Observable<number>;
  loading: boolean = false;
  error: string | null = null;
  currentPage: number = 1;

  @Output() onChange: EventEmitter<string> = new EventEmitter<string>();

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

  ngOnInit(): void {}

  onScroll(event: any): void {
    const bottom =
      event.target.scrollHeight ===
      event.target.scrollTop + event.target.clientHeight;
    if (bottom && !this.loading) {
      this.loading = true;
      this.currentPage += 1;
      this.asyncSelectService.setPage(this.currentPage);
    }
  }

  onSelectionChange(event: any): void {
    this.onChange.emit(event.target.value);
  }

  authorToString(item: any) {
    return authorToString(item);
  }
}

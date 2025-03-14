import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { catchError, map, Observable, switchMap } from 'rxjs';
import { GenresAsyncMultiSelectService } from '../../services/genres-async-multi-select.service';

@Component({
  selector: 'app-genres-async-multi-select',
  imports: [NgIf, NgFor, AsyncPipe],
  templateUrl: './genres-async-multi-select.component.html',
})
export class GenresAsyncMultiSelectComponent {
  data$: Observable<any[]>;
  totalCount$: Observable<number>;
  loading: boolean = false;
  error: string | null = null;
  currentPage: number = 1;

  @Output() changeSelect: EventEmitter<string[]> = new EventEmitter<string[]>();

  constructor(private asyncSelectService: GenresAsyncMultiSelectService) {
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
    if (!select) return this.changeSelect.emit([]);

    this.changeSelect.emit(
      Array.from(select.selectedOptions, (option) => option.value)
    );
  }
}

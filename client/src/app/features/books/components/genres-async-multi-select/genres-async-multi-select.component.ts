import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { catchError, map, Observable, switchMap } from 'rxjs';
import { GenresAsyncMultiSelectService } from '../../services/genres-async-multi-select.service';

@Component({
  selector: 'app-genres-async-multi-select',
  imports: [NgIf, NgFor, AsyncPipe],
  templateUrl: './genres-async-multi-select.component.html',
})
export class GenresAsyncMultiSelectComponent implements OnInit {
  data$: Observable<any[]>;
  totalCount$: Observable<number>;
  loading: boolean = false;
  error: string | null = null;
  currentPage: number = 1;

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
    console.log('Selected:', event.target.value);
  }
}

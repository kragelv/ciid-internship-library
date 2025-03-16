import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { NgSelectComponent } from '@ng-select/ng-select';
import { PageResponse } from '../../../../shared/models/page/page-response.model';
import { Genre } from '../../../genre/models/genre.model';
import { GenreService } from '../../../genre/services/genre.service';

@Component({
  selector: 'app-genres-async-multi-select',
  imports: [NgSelectComponent],
  templateUrl: './genres-async-multi-select.component.html',
})
export class GenresAsyncMultiSelectComponent implements OnInit {
  readonly numberOfItemsFromEndBeforeFetchingMore = 5;

  genres: Genre[] = [];
  totalCount: number = 0;
  currentPage: number = 1;
  hasMore: boolean = true;
  loading: boolean = false;
  error: string | null = null;

  @Output() selectChange = new EventEmitter<string[]>();

  constructor(private genreService: GenreService) {}

  ngOnInit(): void {
    this.loadMore();
  }

  loadMore(): void {
    if (this.loading || !this.hasMore) return;
    this.loading = true;

    this.genreService.getPage({ page: this.currentPage }).subscribe({
      next: (response: PageResponse<Genre>) => {
        this.genres = [...this.genres, ...response.data];
        this.totalCount = response.total;
        this.hasMore = this.genres.length < this.totalCount;
        this.currentPage++;
      },
      error: () => {
        this.error = 'Ошибка загрузки данных';
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  onScroll({ end }: any): void {
    if (
      end + this.numberOfItemsFromEndBeforeFetchingMore >=
      this.genres.length
    ) {
      this.loadMore();
    }
  }

  onScrollToEnd(): void {
    this.loadMore();
  }

  onSelectChange(event: any): void {
    this.selectChange.emit(event.map((item: any) => item.id));
  }
}

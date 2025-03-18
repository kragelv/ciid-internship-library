import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {
  NgLabelTemplateDirective,
  NgOptionTemplateDirective,
  NgSelectComponent,
} from '@ng-select/ng-select';
import { PageResponse } from '../../../../shared/models/page/page-response.model';
import { authorToString } from '../../../../shared/utils/data.utils';
import { Author } from '../../../authors/models/author.model';
import { AuthorService } from '../../../authors/services/author.service';

@Component({
  selector: 'app-authors-async-select',
  imports: [
    NgSelectComponent,
    NgLabelTemplateDirective,
    NgOptionTemplateDirective,
  ],
  templateUrl: './authors-async-select.component.html',
})
export class AuthorsAsyncSelectComponent implements OnInit {
  readonly numberOfItemsFromEndBeforeFetchingMore = 5;

  authors: Author[] = [];
  totalCount: number = 0;
  currentPage: number = 1;
  hasMore: boolean = true;
  loading: boolean = false;
  error: string | null = null;

  @Output() selectChange = new EventEmitter<string | null>();

  constructor(private authorService: AuthorService) {}

  ngOnInit() {
    this.loadMore();
  }

  loadMore() {
    if (this.loading || !this.hasMore) return;
    this.loading = true;

    this.authorService.getPage({ page: this.currentPage }).subscribe({
      next: (response: PageResponse<Author>) => {
        this.authors = [...this.authors, ...response.data];
        this.totalCount = response.total;
        this.hasMore = this.authors.length < this.totalCount;
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

  onScroll({ end }: any) {
    if (
      end + this.numberOfItemsFromEndBeforeFetchingMore >=
      this.authors.length
    ) {
      this.loadMore();
    }
  }

  onScrollToEnd() {
    this.loadMore();
  }

  onSelectChange(event: any) {
    this.selectChange.emit(event.id);
  }

  authorToString(item: Author): string {
    return authorToString(item);
  }
}

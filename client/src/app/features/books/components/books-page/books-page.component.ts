import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Params, Router, RouterLink } from '@angular/router';
import { RouterStateFromType } from '../../../../core/models/router-state-from.model';
import { RouterStateService } from '../../../../core/services/route-state.service';
import { LoaderComponent } from '../../../../shared/components/loader/loader.component';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';
import { PageQueryParams } from '../../../../shared/models/page/page-query-params.model';
import { PageResponse } from '../../../../shared/models/page/page-response.model';
import { calcTotalPages } from '../../../../shared/utils/data.utils';
import { Book } from '../../models/book.model';
import { BookService } from '../../services/book.service';
import { AuthorsAsyncSelectComponent } from '../authors-async-select/authors-async-select.component';
import { GenresAsyncMultiSelectComponent } from '../genres-async-multi-select/genres-async-multi-select.component';

@Component({
  selector: 'app-books-page',
  templateUrl: './books-page.component.html',
  imports: [
    FormsModule,
    RouterLink,
    NgIf,
    NgFor,
    LoaderComponent,
    PaginationComponent,
    AuthorsAsyncSelectComponent,
    GenresAsyncMultiSelectComponent,
  ],
})
export class BooksPageComponent implements OnInit {
  readonly initialValues = {
    title: '',
    authorId: '',
    publishedYear: null,
    availableCopies: 0,
    genreIds: [],
  };

  error = false;
  loading = false;
  books: Book[] = [];
  totalCount = 0;
  totalPages = 1;
  isFormVisible = false;
  formValues = { ...this.initialValues };
  queryParams: PageQueryParams = {
    page: 1,
    limit: BookService.DEFAULT_LIMIT,
  };
  routerLinkState: {
    from: RouterStateFromType;
  };

  constructor(
    private bookService: BookService,
    private titleService: Title,
    private route: ActivatedRoute,
    private router: Router,
    private routerStateService: RouterStateService
  ) {
    this.routerLinkState = { from: { path: router.url } };
  }

  ngOnInit() {
    this.titleService.setTitle('Авторы');
    this.route.queryParams.subscribe((params) => {
      this.queryParams.page = Number(params['page']) || 1;
      this.queryParams.limit =
        Number(params['limit']) || BookService.DEFAULT_LIMIT;
      this.loadPage();
    });

    this.routerStateService.routerState.subscribe((state) => {
      if (state) {
        this.routerLinkState.from = state;
      }
    });
  }

  loadPage() {
    this.error = false;
    this.loading = true;
    this.bookService.getPage(this.queryParams).subscribe({
      next: (response: PageResponse<Book>) => {
        this.books = response.data;
        this.totalCount = response.total;
        this.totalPages = calcTotalPages(
          response.total,
          this.queryParams.limit,
          BookService.DEFAULT_LIMIT
        );
      },
      error: () => {
        this.error = true;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  handleCreateBook() {
    this.bookService.create(this.formValues).subscribe(() => {
      this.formValues = { ...this.initialValues };
      this.isFormVisible = false;
      this.loadPage();
    });
  }

  handleDeleteBook(id: string) {
    if (window.confirm('Вы уверены, что хотите удалить автора?')) {
      this.bookService.delete(id).subscribe(() => {
        this.loadPage();
      });
    }
  }

  handlePageChange(page: number) {
    this.queryParams.page = page;
    this.updateSearchParams(this.queryParams);
  }

  updateSearchParams(newPageParams: PageQueryParams) {
    const queryParams: Params = {};
    if (newPageParams.page) {
      queryParams['page'] = newPageParams.page;
    }
    if (
      newPageParams.limit &&
      newPageParams.limit !== BookService.DEFAULT_LIMIT
    ) {
      queryParams['limit'] = newPageParams.limit;
    }

    this.router.navigate([], {
      queryParams: queryParams,
      queryParamsHandling: 'merge',
    });
  }

  changeAuthor(authorId: any) {
    this.formValues.authorId = authorId;
  }
}

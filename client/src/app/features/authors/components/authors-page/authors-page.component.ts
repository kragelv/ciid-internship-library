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
import { Author } from '../../models/author.model';
import { AuthorService } from '../../services/author.service';

@Component({
  selector: 'app-authors-page',
  templateUrl: './authors-page.component.html',
  imports: [
    FormsModule,
    RouterLink,
    NgIf,
    NgFor,
    LoaderComponent,
    PaginationComponent,
  ],
})
export class AuthorsPageComponent implements OnInit {
  readonly initialValues = {
    firstName: '',
    middleName: '',
    lastName: '',
    birthYear: 0,
  };

  error = false;
  loading = false;
  authors: Author[] = [];
  totalCount = 0;
  totalPages = 1;
  isFormVisible = false;
  formValues = { ...this.initialValues };
  queryParams: PageQueryParams = {
    page: 1,
    limit: AuthorService.DEFAULT_LIMIT,
  };
  routerLinkState: {
    from: RouterStateFromType;
  };

  constructor(
    private authorService: AuthorService,
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
        Number(params['limit']) || AuthorService.DEFAULT_LIMIT;
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
    this.authorService.getPage(this.queryParams).subscribe({
      next: (response: PageResponse<Author>) => {
        this.authors = response.data;
        this.totalCount = response.total;
        this.totalPages = calcTotalPages(
          response.total,
          this.queryParams.limit,
          AuthorService.DEFAULT_LIMIT
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

  handleCreateAuthor() {
    this.authorService.create(this.formValues).subscribe(() => {
      this.formValues = { ...this.initialValues };
      this.isFormVisible = false;
      this.loadPage();
    });
  }

  handleDeleteAuthor(id: string) {
    if (window.confirm('Вы уверены, что хотите удалить автора?')) {
      this.authorService.delete(id).subscribe(() => {
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
      newPageParams.limit !== AuthorService.DEFAULT_LIMIT
    ) {
      queryParams['limit'] = newPageParams.limit;
    }

    this.router.navigate([], {
      queryParams: queryParams,
      queryParamsHandling: 'merge',
    });
  }
}

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
import { Genre } from '../../models/genre.model';
import { GenreService } from '../../services/genre.service';

@Component({
  selector: 'app-genres-page',
  templateUrl: './genres-page.component.html',
  imports: [
    FormsModule,
    RouterLink,
    NgIf,
    NgFor,
    LoaderComponent,
    PaginationComponent,
  ],
})
export class GenresPageComponent implements OnInit {
  readonly DEFAULT_BACK_URL = '/genres';

  error = false;
  loading = false;
  genres: Genre[] = [];
  totalCount = 0;
  totalPages = 1;
  isFormVisible = false;
  formValues = {
    name: '',
  };
  queryParams: PageQueryParams;
  routerLinkState: {
    from: RouterStateFromType;
  };

  constructor(
    private genreService: GenreService,
    private titleService: Title,
    private route: ActivatedRoute,
    private router: Router,
    private routerStateService: RouterStateService
  ) {
    this.routerLinkState = { from: { path: this.DEFAULT_BACK_URL } };
    this.queryParams = { page: 1, limit: this.genreService.getDefaultLimit() };
  }

  ngOnInit() {
    this.titleService.setTitle('Жанры');
    this.route.queryParams.subscribe((params) => {
      this.queryParams.page = Number(params['page']) || 1;
      this.queryParams.limit =
        Number(params['limit']) || this.genreService.getDefaultLimit();
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
    this.genreService.getPage(this.queryParams).subscribe({
      next: (response: PageResponse<Genre>) => {
        this.genres = response.data;
        this.totalCount = response.total;
        this.totalPages = calcTotalPages(
          response.total,
          this.queryParams.limit,
          this.genreService.getDefaultLimit()
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

  handleCreateGenre() {
    this.genreService.create(this.formValues).subscribe(() => {
      this.formValues.name = '';
      this.isFormVisible = false;
      this.loadPage();
    });
  }

  handleDeleteGenre(id: string) {
    if (window.confirm('Вы уверены, что хотите удалить жанр?')) {
      this.genreService.delete(id).subscribe(() => {
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
      newPageParams.limit !== this.genreService.getDefaultLimit()
    ) {
      queryParams['limit'] = newPageParams.limit;
    }

    this.router.navigate([], {
      queryParams: queryParams,
      queryParamsHandling: 'merge',
    });
  }
}

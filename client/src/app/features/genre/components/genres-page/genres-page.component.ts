import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router, RouterLink } from '@angular/router';
import { Genre } from '../../models/genre.model';
import { PageQueryParams } from '../../../../shared/models/page/page-query-params.model';
import { GenreService } from '../../services/genre.service';
import { combineLatest, switchMap } from 'rxjs';
import { PageResponse } from '../../../../shared/models/page/page-response.model';
import { calcTotalPages } from '../../../../shared/utils/data.utils';
import { LoaderComponent } from '../../../../shared/components/loader/loader.component';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { RouterStateFromType } from '../../../../core/models/router-state-from.model';
import { RouterStateService } from '../../../../core/services/route-state.service';

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
  error = false;
  loading = false;
  genres: Genre[] = [];
  totalCount = 0;
  totalPages = 1;
  isFormVisible = false;
  formValues = {
    name: '',
  };
  queryParams: PageQueryParams = {
    page: 1,
    limit: GenreService.DEFAULT_LIMIT,
  };
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
    this.routerLinkState = { from: { path: router.url } };
  }

  ngOnInit() {
    this.titleService.setTitle('Жанры');
    this.route.queryParams.subscribe((params) => {
      this.queryParams.page = Number(params['page']) || 1;
      this.queryParams.limit =
        Number(params['limit']) || GenreService.DEFAULT_LIMIT;
      this.loadPage();
    });

    this.routerStateService.routerState.subscribe((state) => {
      if (state) {
        console.log(state);
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
          GenreService.DEFAULT_LIMIT
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
      newPageParams.limit !== GenreService.DEFAULT_LIMIT
    ) {
      queryParams['limit'] = newPageParams.limit;
    }

    this.router.navigate([], {
      queryParams: queryParams,
      queryParamsHandling: 'merge',
    });
  }
}

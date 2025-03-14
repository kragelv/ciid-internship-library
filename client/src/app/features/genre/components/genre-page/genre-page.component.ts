import { Component, TemplateRef, ViewChild } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Params,
  Router,
  RouterLink,
  RouterLinkWithHref,
} from '@angular/router';
import { GenreService } from '../../services/genre.service';
import { Genre } from '../../models/genre.model';
import { NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import { LoaderComponent } from '../../../../shared/components/loader/loader.component';
import { FormsModule } from '@angular/forms';
import { filter } from 'rxjs';
import { RouterStateFromType } from '../../../../core/models/router-state-from.model';
import { RouterStateService } from '../../../../core/services/route-state.service';

@Component({
  selector: 'app-genre-page',
  imports: [NgIf, LoaderComponent, FormsModule, NgTemplateOutlet, RouterLink],
  templateUrl: './genre-page.component.html',
})
export class GenrePageComponent {
  readonly DEFAULT_BACK_URL = '/genres';

  genre: Genre = {} as Genre;
  name: string = '';
  error: boolean = false;
  loading: boolean = false;
  isEditMode: boolean = false;
  routerEditLinkState: {
    from: RouterStateFromType;
  };
  backUrl: RouterStateFromType;

  @ViewChild('viewTemplate', { static: true })
  viewTemplate!: TemplateRef<any>;

  @ViewChild('editTemplate', { static: true })
  editTemplate!: TemplateRef<any>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private genreService: GenreService,
    private routerStateService: RouterStateService
  ) {
    this.routerEditLinkState = { from: { path: router.url } };
    this.backUrl = history.state?.from || { path: this.DEFAULT_BACK_URL };
    console.log(this.backUrl);
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) this.loadGenre(id);
    });

    this.route.queryParamMap.subscribe((queryParams) => {
      this.isEditMode = queryParams.has('edit');
    });

    this.routerStateService.routerState.subscribe((state) => {
      if (state) {
        this.routerEditLinkState.from = state;
      }
    });

    this.router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe(() => {
        this.backUrl = history.state?.from || { path: this.DEFAULT_BACK_URL };
      });
  }

  loadGenre(id: string) {
    this.error = false;
    this.loading = true;
    this.genreService.getById(id).subscribe({
      next: (response) => {
        this.genre = response;
        this.name = response.name;
      },
      error: () => {
        this.error = true;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  saveGenre() {
    if (!this.genre) return;

    this.genreService
      .update(this.genre.id, { name: this.name })
      .subscribe((response) => {
        this.genre = response;
        this.name = response.name;
        this.router.navigate([], { queryParams: {} });
      });
  }

  getCurrentTemplate() {
    return this.isEditMode ? this.editTemplate : this.viewTemplate;
  }
}

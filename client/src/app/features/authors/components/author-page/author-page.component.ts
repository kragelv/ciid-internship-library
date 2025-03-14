import { Component, TemplateRef, ViewChild } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Params,
  Router,
  RouterLink,
  RouterLinkWithHref,
} from '@angular/router';
import { AuthorService } from '../../services/author.service';
import { Author } from '../../models/author.model';
import { NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import { LoaderComponent } from '../../../../shared/components/loader/loader.component';
import { FormsModule } from '@angular/forms';
import { filter } from 'rxjs';
import { RouterStateFromType } from '../../../../core/models/router-state-from.model';
import { RouterStateService } from '../../../../core/services/route-state.service';

@Component({
  selector: 'app-author-page',
  imports: [NgIf, LoaderComponent, FormsModule, NgTemplateOutlet, RouterLink],
  templateUrl: './author-page.component.html',
})
export class AuthorPageComponent {
  readonly DEFAULT_BACK_URL = '/authors';
  readonly initialValues = {
    firstName: '',
    middleName: '',
    lastName: '',
    birthYear: 0,
  };

  author: Author = {} as Author;
  formValues = { ...this.initialValues };
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
    private authorService: AuthorService,
    private routerStateService: RouterStateService
  ) {
    this.routerEditLinkState = { from: { path: router.url } };
    this.backUrl = history.state?.from || { path: this.DEFAULT_BACK_URL };
    console.log(this.backUrl);
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) this.loadAuthor(id);
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

  loadAuthor(id: string) {
    this.error = false;
    this.loading = true;
    this.authorService.getById(id).subscribe({
      next: (response) => {
        this.author = response;
        this.formValues = { ...response };
      },
      error: () => {
        this.error = true;
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  saveAuthor() {
    if (!this.author) return;

    this.authorService
      .update(this.author.id, { ...this.formValues })
      .subscribe((response) => {
        this.author = response;
        this.formValues = { ...response };
        this.router.navigate([], { queryParams: {} });
      });
  }

  getCurrentTemplate() {
    return this.isEditMode ? this.editTemplate : this.viewTemplate;
  }
}

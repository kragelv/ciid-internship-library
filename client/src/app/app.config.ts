import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { baseUrlProvider } from './core/config/base-url.config';
import { BaseUrlInterceptor } from './core/interceptors/base-url.interceptor';
import { RouterStateService } from './core/services/route-state.service';
import { AuthorService } from './features/authors/services/author.service';
import { AuthorsAsyncSelectService } from './features/books/services/authors-async-select.service';
import { GenresAsyncMultiSelectService } from './features/books/services/genres-async-multi-select.service';
import { GenreService } from './features/genre/services/genre.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(withInterceptors([BaseUrlInterceptor])),
    provideRouter(routes),
    RouterStateService,
    baseUrlProvider,
    GenreService,
    AuthorService,
    AuthorsAsyncSelectService,
    GenresAsyncMultiSelectService,
  ],
};

import { Routes } from '@angular/router';
import { NotFoundComponent } from './shared/components/not-found/not-found.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { GenresPageComponent } from './features/genre/components/genres-page/genres-page.component';
import { GenrePageComponent } from './features/genre/components/genre-page/genre-page.component';
import { AuthorsPageComponent } from './features/authors/components/authors-page/authors-page.component';
import { AuthorPageComponent } from './features/authors/components/author-page/author-page.component';
import { BooksPageComponent } from './features/books/components/books-page/books-page.component';

export const routes: Routes = [
  { path: '', redirectTo: '/books', pathMatch: 'full' },
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'genres', component: GenresPageComponent },
      { path: 'genres/:id', component: GenrePageComponent },
      { path: 'authors', component: AuthorsPageComponent },
      { path: 'authors/:id', component: AuthorPageComponent },
      { path: 'books', component: BooksPageComponent },
    ],
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

import { AuthorDto } from '../author/AuthorDto';
import { GenreDto } from '../genre/GenreDto';

export interface BookDto {
  id: string;
  title: string;
  author: AuthorDto;
  publishedYear: number;
  availableCopies: number;
  genres: GenreDto[];
}

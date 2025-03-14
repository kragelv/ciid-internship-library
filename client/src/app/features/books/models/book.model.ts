export interface BookAuthor {
  id: string;
  firstName: string;
  middleName: string;
  lastName: string;
  birthYear: number;
}

export interface BookGenre {
  id: string;
  name: string;
}

export interface Book {
  id: string;
  title: string;
  author: BookAuthor;
  publishedYear: number;
  availableCopies: number;
  genres: BookGenre[];
}

export interface BookRequest {
  title: string;
  authorId: string;
  publishedYear: number | null;
  availableCopies: number;
  genreIds: string[];
}

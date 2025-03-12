export interface BookRequestDto {
  title: string;
  authorId: string;
  publishedYear: number | null;
  availableCopies: number;
  genreIds: string[];
}

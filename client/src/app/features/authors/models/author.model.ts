export interface Author {
  id: string;
  firstName: string;
  middleName: string;
  lastName: string;
  birthYear: number;
}

export interface AuthorRequest {
  firstName: string;
  middleName: string;
  lastName: string;
  birthYear: number;
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from '../../../core/services/crud.service';
import { Book, BookRequest } from '../models/book.model';

@Injectable({
  providedIn: 'root',
})
export class BookService extends CrudService<Book, string, BookRequest> {
  constructor(http: HttpClient) {
    super(http, '/v1/books');
  }
}

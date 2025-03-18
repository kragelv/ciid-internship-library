import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from '../../../core/services/crud.service';
import { Author, AuthorRequest } from '../models/author.model';

@Injectable({
  providedIn: 'root',
})
export class AuthorService extends CrudService<Author, string, AuthorRequest> {
  constructor(http: HttpClient) {
    super(http, '/v1/authors');
  }
}

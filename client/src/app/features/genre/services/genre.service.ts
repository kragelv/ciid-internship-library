import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CrudService } from '../../../core/services/crud.service';
import { Genre, GenreRequest } from '../models/genre.model';

@Injectable({
  providedIn: 'root',
})
export class GenreService extends CrudService<Genre, string, GenreRequest> {
  constructor(http: HttpClient) {
    super(http, '/v1/genres', 20);
  }
}

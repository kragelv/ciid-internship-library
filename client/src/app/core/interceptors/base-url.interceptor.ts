import { HttpEvent, HttpHandlerFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { BASE_API_URL } from '../config/base-url.config';

export function BaseUrlInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  const baseUrl = inject(BASE_API_URL);

  const apiReq = req.clone({ url: `${baseUrl}${req.url}` });
  return next(apiReq);
}

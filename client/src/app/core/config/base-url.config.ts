import { InjectionToken } from '@angular/core';

export const BASE_API_URL = new InjectionToken<string>('BASE_API_URL');

export const baseUrlProvider = {
  provide: BASE_API_URL,
  useValue: 'http://localhost:8080/api',
};

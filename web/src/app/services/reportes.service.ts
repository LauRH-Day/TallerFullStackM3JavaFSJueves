
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_URL } from '../core/api';
import { Resumen } from '../pages/reportes/reportes';

@Injectable({
  providedIn: 'root'
})
export class ReportesService {

  private http = inject(HttpClient);

  resumen(): Observable<Resumen> {
    return this.http.get<Resumen>(
      `${API_URL}/reportes/resumen`
    );
  }
}

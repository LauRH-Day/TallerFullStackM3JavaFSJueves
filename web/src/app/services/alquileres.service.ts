
import { Injectable, inject, signal } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api';
import { mensajeDeError } from '../core/errores';

export type EstadoAlquiler =
  | 'RESERVADO'
  | 'ENTREGADO'
  | 'DEVUELTO'
  | 'CANCELADO';

export interface AlquilerRequest {
  herramientaId: number;
  fechaInicio: string;
  fechaFin: string;
}

export interface Alquiler {
  id: number;
  herramientaId: number;
  herramientaNombre: string;
  clienteId: number;
  clienteNombre: string;
  fechaInicio: string;
  fechaFin: string;
  dias: number;
  valorTotal: number;
  deposito: number;
  estado: EstadoAlquiler;
  creadoEn: string;
}

@Injectable({ providedIn: 'root' })
export class AlquileresService {
  private http = inject(HttpClient);
  private apiUrl = `${API_URL}/alquileres`;

  private lista = signal<Alquiler[]>([]);
  private cargandoSignal = signal(false);
  private errorSignal = signal<string | null>(null);

  alquileres = this.lista.asReadonly();
  cargando = this.cargandoSignal.asReadonly();
  error = this.errorSignal.asReadonly();

  // POST /api/alquileres
  crear(datos: AlquilerRequest): Observable<Alquiler> {
    this.errorSignal.set(null);

    return this.http.post<Alquiler>(this.apiUrl, datos);
  }

  // GET /api/alquileres/mios
  cargarMios(): void {
    this.cargandoSignal.set(true);
    this.errorSignal.set(null);

    this.http.get<Alquiler[]>(`${this.apiUrl}/mios`).subscribe({
      next: (datos) => {
        this.lista.set(datos);
        this.cargandoSignal.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.errorSignal.set(mensajeDeError(error));
        this.cargandoSignal.set(false);
      }
    });
  }


  // GET /api/alquileres
cargarTodos(estado?: EstadoAlquiler): void {
  this.cargandoSignal.set(true);
  this.errorSignal.set(null);

  let params = new HttpParams();

  if (estado) {
    params = params.set('estado', estado);
  }

  this.http.get<Alquiler[]>(this.apiUrl, { params }).subscribe({
    next: (datos) => {
      this.lista.set(datos);
      this.cargandoSignal.set(false);
    },
    error: (error: HttpErrorResponse) => {
      this.errorSignal.set(mensajeDeError(error));
      this.cargandoSignal.set(false);
    }
  });
}

// PATCH /api/alquileres/{id}/entregar
entregar(alquiler: Alquiler): void {
  this.errorSignal.set(null);

  this.http.patch<Alquiler>(
    `${this.apiUrl}/${alquiler.id}/entregar`,
    {}
  ).subscribe({
    next: (actualizado) => {
      this.lista.update(actuales =>
        actuales.map(a =>
          a.id === actualizado.id ? actualizado : a
        )
      );
    },
    error: (error: HttpErrorResponse) => {
      this.errorSignal.set(mensajeDeError(error));
    }
  });
}

// PATCH /api/alquileres/{id}/devolver
devolver(alquiler: Alquiler): void {
  this.errorSignal.set(null);

  this.http.patch<Alquiler>(
    `${this.apiUrl}/${alquiler.id}/devolver`,
    {}
  ).subscribe({
    next: (actualizado) => {
      this.lista.update(actuales =>
        actuales.map(a =>
          a.id === actualizado.id ? actualizado : a
        )
      );
    },
    error: (error: HttpErrorResponse) => {
      this.errorSignal.set(mensajeDeError(error));
    }
  });
}
  // PATCH /api/alquileres/{id}/cancelar
  cancelar(alquiler: Alquiler): void {
    this.errorSignal.set(null);

    this.http.patch<Alquiler>(
      `${this.apiUrl}/${alquiler.id}/cancelar`,
      {}
    ).subscribe({
      next: (actualizado) => {
        this.lista.update(actuales =>
          actuales.map(a =>
            a.id === actualizado.id ? actualizado : a
          )
        );
      },
      error: (error: HttpErrorResponse) => {
        this.errorSignal.set(mensajeDeError(error));
      }
    });
  }
}

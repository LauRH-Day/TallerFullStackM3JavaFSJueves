import { Injectable, inject, signal } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../core/api';
import { mensajeDeError } from '../core/errores';
import { pesos } from '../core/formato';
export type Categoria = 'ELECTRICA'|'CONSTRUCCION' | 'JARDIN' |'MEDICION';

export interface Herramienta {
    id: number;
    nombre: string;
    categoria: Categoria;
    descripcion: string | null;
    precioDia: number;
    deposito: number;
    unidades: number;
    imagenUrl: string | null;
    activa: boolean;
  }
  export interface HerramientaRequest {
    nombre: string;
    categoria: Categoria;
    descripcion: string | null;
    precioDia: number;
    deposito: number;
    unidades: number;
    imagenUrl: string | null;
  }
  export const NOMBRE_CATEGORIA: Record<Categoria, string> = {
    ELECTRICA: 'Herramienta eléctrica',
    CONSTRUCCION: 'Herramienta de construcción',
    JARDIN: 'Herramienta de jardín',
    MEDICION: 'Medición'
  };
  
  @Injectable({ providedIn: 'root' })
  export class HerramientasService {
    private http = inject(HttpClient);
    private apiUrl = `${API_URL}/herramientas`;
  
    private lista = signal<Herramienta[]>([]);
    private cargandoSignal = signal(false);
    private errorSignal = signal<string | null>(null);
  
    herramientas = this.lista.asReadonly();
    cargando = this.cargandoSignal.asReadonly();
    error = this.errorSignal.asReadonly();
  

    crear(datos: HerramientaRequest): Observable<Herramienta> {
      this.errorSignal.set(null);
      return this.http.post<Herramienta>(this.apiUrl, datos);
    }
    
    actualizar(id: number, datos: HerramientaRequest): Observable<Herramienta> {
      this.errorSignal.set(null);
      return this.http.put<Herramienta>(`${this.apiUrl}/${id}`, datos);
    }
    
    // GET /api/herramientas: el backend filtra si es cliente o admin
    cargar() {
      this.cargandoSignal.set(true);
      this.errorSignal.set(null);
  
      this.http.get<Herramienta[]>(this.apiUrl).subscribe({
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
  
    // GET /api/herramientas/{id}
    obtener(id: number): Observable<Herramienta> {
      return this.http.get<Herramienta>(`${this.apiUrl}/${id}`);
    }
  
    // PATCH /api/herramientas/{id}/estado (exige HERRAMIENTAS_GESTIONAR)
    cambiarEstado(herramienta: Herramienta, activa: boolean) {
      this.errorSignal.set(null);
  
      this.http.patch<Herramienta>(`${this.apiUrl}/${herramienta.id}/estado`, { activa }).subscribe({
        next: (actualizada) => {
          this.lista.update(actuales =>
            actuales.map(h => (h.id === actualizada.id ? actualizada : h))
          );
        },
        error: (error: HttpErrorResponse) => this.errorSignal.set(mensajeDeError(error))
      });
    }
  }
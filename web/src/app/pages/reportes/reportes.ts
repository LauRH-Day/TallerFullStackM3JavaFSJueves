
import { Component, inject, signal } from '@angular/core';
import { ReportesService } from '../../services/reportes.service';

  
  export interface HerramientaAlquilada {
    herramienta: string;
    alquileres: number;
  }
  
  export interface AlquilerVencido {
    id: number;
    herramientaNombre: string;
    clienteNombre: string;
    fechaInicio: string;
    fechaFin: string;
    estado: string;
  }
  
  export interface Resumen {
    ingresosMes: number;
    alquileresActivos: number;
    vencidos: AlquilerVencido[];
    masAlquiladas: HerramientaAlquilada[];
  }
@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [],
  templateUrl: './reportes.html',
  styleUrl: './reportes.css'
})
export class Reportes {
  private reportesService = inject(ReportesService);

  resumen = signal<Resumen | null>(null);
  cargando = signal(true);
  error = signal<string | null>(null);

  constructor() {
    this.cargarResumen();
  }

  cargarResumen(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.reportesService.resumen().subscribe({
      next: (datos) => {
        this.resumen.set(datos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.mensaje || 'No fue posible cargar los reportes.');
        this.cargando.set(false);
      }
    });
  }

  pesos(valor: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0
    }).format(valor);
  }
}

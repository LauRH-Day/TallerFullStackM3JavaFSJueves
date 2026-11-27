import { Component, inject, signal } from '@angular/core';
import { ReportesService } from '../../services/reportes.service';
import { Resumen } from '../reportes/reportes';
import { pesos } from '../../core/formato';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [],
  templateUrl: './panel.html',
  styleUrl: './panel.css'
})
export class Panel {
  private reportesService = inject(ReportesService);

  resumen = signal<Resumen | null>(null);
  cargando = signal(true);
  error = signal<string | null>(null);

  constructor() {
    this.reportesService.resumen().subscribe({
      next: datos => {
        this.resumen.set(datos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No fue posible cargar el panel.');
        this.cargando.set(false);
      }
    });
  }
  pesos(valor: number): string {
    return pesos(valor);
  }
}
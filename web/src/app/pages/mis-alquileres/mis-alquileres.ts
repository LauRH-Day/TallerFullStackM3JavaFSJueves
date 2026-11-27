
import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';

import {
  Alquiler,
  AlquileresService
} from '../../services/alquileres.service';

import { pesos } from '../../core/formato';

@Component({
  selector: 'app-mis-alquileres',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-alquileres.html',
  styleUrl: './mis-alquileres.css'
})
export class MisAlquileres implements OnInit {
  private alquileresService = inject(AlquileresService);

  alquileres = this.alquileresService.alquileres;
  cargando = this.alquileresService.cargando;
  error = this.alquileresService.error;

  confirmarCancelacion = signal<number | null>(null);

  pesos = pesos;

  ngOnInit(): void {
    this.alquileresService.cargarMios();
  }

  puedeCancelar(alquiler: Alquiler): boolean {
    return alquiler.estado === 'RESERVADO'
      && alquiler.fechaInicio > this.obtenerHoy();
  }

  solicitarCancelacion(alquiler: Alquiler): void {
    this.confirmarCancelacion.set(alquiler.id);
  }

  cancelar(alquiler: Alquiler): void {
    if (this.confirmarCancelacion() !== alquiler.id) {
      return;
    }

    this.alquileresService.cancelar(alquiler);
    this.confirmarCancelacion.set(null);
  }

  cerrarConfirmacion(): void {
    this.confirmarCancelacion.set(null);
  }

  private obtenerHoy(): string {
    const hoy = new Date();
    const anio = hoy.getFullYear();
    const mes = String(hoy.getMonth() + 1).padStart(2, '0');
    const dia = String(hoy.getDate()).padStart(2, '0');

    return `${anio}-${mes}-${dia}`;
  }
}

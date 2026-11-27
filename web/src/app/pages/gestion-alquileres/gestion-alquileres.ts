
import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Alquiler,
  AlquileresService,
  EstadoAlquiler
} from '../../services/alquileres.service';
import { pesos } from '../../core/formato';

@Component({
  selector: 'app-gestion-alquileres',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gestion-alquileres.html',
  styleUrl: './gestion-alquileres.css'
})
export class GestionAlquileres {
  private service = inject(AlquileresService);

  alquileres = this.service.alquileres;
  cargando = this.service.cargando;
  error = this.service.error;

  pesos = pesos;

  estados: { valor: EstadoAlquiler | ''; texto: string }[] = [
    { valor: '', texto: 'Todos' },
    { valor: 'RESERVADO', texto: 'Reservados' },
    { valor: 'ENTREGADO', texto: 'Entregados' },
    { valor: 'DEVUELTO', texto: 'Devueltos' },
    { valor: 'CANCELADO', texto: 'Cancelados' }
  ];

  filtro: EstadoAlquiler | '' = '';

  constructor() {
    this.service.cargarTodos();
  }

  filtrar(estado: EstadoAlquiler | ''): void {
    this.filtro = estado;
    this.service.cargarTodos(estado || undefined);
  }

  entregar(alquiler: Alquiler): void {
    if (alquiler.estado === 'RESERVADO') {
      this.service.entregar(alquiler);
    }
  }

  devolver(alquiler: Alquiler): void {
    if (alquiler.estado === 'ENTREGADO') {
      this.service.devolver(alquiler);
    }
  }
}

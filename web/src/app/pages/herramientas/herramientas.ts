import { Component, computed, inject, signal } from '@angular/core';
import { pesos } from '../../core/formato';
import { RouterLink } from '@angular/router';
import localeEsCo from '@angular/common/locales/es-CO';import { SiPuede } from '../../shared/si-puede';
import { AuthService } from '../../core/auth.service';
import {
  Categoria,
  Herramienta,
  HerramientasService,
  NOMBRE_CATEGORIA
} from '../../services/herramientas.service';
type Filtro = Categoria | 'TODOS';
@Component({
  imports: [SiPuede, RouterLink],
  selector: 'app-herramientas',
  styleUrl: './herramientas.css',
  templateUrl: './herramientas.html',
})
export class Herramientas {
  private herramientasService = inject(HerramientasService);
  auth = inject(AuthService);

  herramientas = this.herramientasService.herramientas;
  cargando = this.herramientasService.cargando;
  error = this.herramientasService.error;

  nombreCategoria = NOMBRE_CATEGORIA;
  pesos = pesos;

  gestiona = computed(() => this.auth.puede('HERRAMIENTAS_GESTIONAR'));
  inactivos = computed(() => this.herramientas().filter(h => !h.activa).length);

  filtros: { valor: Filtro; texto: string }[] = [
    { valor: 'TODOS', texto: 'Todas' },
    { valor: 'ELECTRICA', texto: 'Eléctricas' },
    { valor: 'CONSTRUCCION', texto: 'Construccion' },
    { valor: 'JARDIN', texto: 'Jardin' },
    { valor: 'MEDICION', texto: 'Medición' }
  ];
  filtro = signal<Filtro>('TODOS');

  herramientasFiltradas = computed(() =>
    this.filtro() === 'TODOS'
      ? this.herramientas()
      : this.herramientas().filter(h => h.categoria === this.filtro())
  );

  constructor() {
    this.herramientasService.cargar();
  }

  alternarEstado(herramienta: Herramienta) {
    this.herramientasService.cambiarEstado(herramienta, !herramienta.activa);
  }
}
import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, input, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { Herramienta, HerramientasService } from '../../services/herramientas.service';
import { AlquileresService } from '../../services/alquileres.service';
import { calcularDias, validarAlquiler } from '../../core/validaciones';
import { mensajeDeError } from '../../core/errores';
import { pesos } from '../../core/formato';

@Component({
  selector: 'app-alquilar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alquileres.html',
  styleUrl: './alquileres.css'
})
export class Alquileres implements OnInit {
  id = input<string | undefined>();

  private herramientasService = inject(HerramientasService);
  private alquileresService = inject(AlquileresService);
  private router = inject(Router);

  herramienta = signal<Herramienta | null>(null);
  cargando = signal(true);
  erroresFormulario = signal<string[]>([]);
  errorAlquiler = signal<string | null>(null);
  resumenValor = signal('');
  pesos = pesos;

  ngOnInit(): void {
    const id = this.id();

if (!id) {
  this.errorAlquiler.set('No se recibió el identificador de la herramienta.');
  this.cargando.set(false);
  return;
}

const herramientaId = Number(id);

    this.herramientasService.obtener(herramientaId).subscribe({
      next: (herramienta) => {
        this.herramienta.set(herramienta);
        this.cargando.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.errorAlquiler.set(mensajeDeError(error));
        this.cargando.set(false);
      }
    });
  }

  actualizarResumen(event: Event): void {
    const formulario = event.currentTarget as HTMLFormElement;
    const datos = new FormData(formulario);

    const fechaInicio = this.leerTexto(datos, 'fechaInicio');
    const fechaFin = this.leerTexto(datos, 'fechaFin');

    const dias = calcularDias(fechaInicio, fechaFin);
    const herramienta = this.herramienta();

    if (!herramienta || dias === 0) {
      this.resumenValor.set('');
      return;
    }

    const total = dias * herramienta.precioDia;

    this.resumenValor.set(
      `${dias} ${dias === 1 ? 'día' : 'días'} · ${pesos(total)}`
    );
  }

  alquilar(event: Event): void {
    event.preventDefault();

    this.erroresFormulario.set([]);
    this.errorAlquiler.set(null);

    const formulario = event.currentTarget as HTMLFormElement;
    const datos = new FormData(formulario);

    const fechaInicio = this.leerTexto(datos, 'fechaInicio');
    const fechaFin = this.leerTexto(datos, 'fechaFin');

    const errores = validarAlquiler(
      { fechaInicio, fechaFin },
      this.obtenerHoy()
    );

    if (errores.length > 0) {
      this.erroresFormulario.set(errores);
      return;
    }

    const herramienta = this.herramienta();

    if (!herramienta) {
      this.errorAlquiler.set('No se pudo cargar la herramienta.');
      return;
    }

    this.alquileresService.crear({
      herramientaId: Number(this.id()),
      fechaInicio,
      fechaFin
    }).subscribe({
      next: () => {
        this.router.navigate(['/mis-alquileres']);
      },
      error: (error: HttpErrorResponse) => {
        this.errorAlquiler.set(mensajeDeError(error));
      }
    });
  }

  private leerTexto(datos: FormData, campo: string): string {
    const valor = datos.get(campo);
    return typeof valor === 'string' ? valor : '';
  }

  private obtenerHoy(): string {
    const hoy = new Date();
    const anio = hoy.getFullYear();
    const mes = String(hoy.getMonth() + 1).padStart(2, '0');
    const dia = String(hoy.getDate()).padStart(2, '0');

    return `${anio}-${mes}-${dia}`;
  }
}
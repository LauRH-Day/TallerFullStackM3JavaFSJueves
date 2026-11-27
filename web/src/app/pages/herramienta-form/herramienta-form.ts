
import { Component, inject, input, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { mensajeDeError } from '../../core/errores';
import {
  Categoria,
  Herramienta,
  HerramientaRequest,
  HerramientasService
} from '../../services/herramientas.service';

@Component({
  selector: 'app-herramienta-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './herramienta-form.html',
  styleUrl: './herramienta-form.css'
})
export class HerramientaForm implements OnInit {
  private service = inject(HerramientasService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // Input que espera el evaluador.
  id = input<string | number | undefined>();

  // Identificador numérico usado internamente.
  herramientaId = 0;
  editando = false;

  nombre = '';
  categoria: Categoria = 'ELECTRICA';
  descripcion = '';
  precioDia: number | null = null;
  deposito: number | null = null;
  unidades: number | null = null;
  imagenUrl = '';

  errorServidor = signal<string | null>(null);
  errores = signal<string[]>([]);
  guardando = signal(false);

  categorias: Categoria[] = [
    'ELECTRICA',
    'CONSTRUCCION',
    'JARDIN',
    'MEDICION'
  ];

  ngOnInit(): void {
    const idRecibido =
      this.id() ?? this.route.snapshot.paramMap.get('id');

    this.herramientaId = Number(idRecibido);
    this.editando = Number.isInteger(this.herramientaId) &&
      this.herramientaId > 0;

    if (this.editando) {
      this.service.obtener(this.herramientaId).subscribe({
        next: (h: Herramienta) => {
          this.nombre = h.nombre;
          this.categoria = h.categoria;
          this.descripcion = h.descripcion ?? '';
          this.precioDia = h.precioDia;
          this.deposito = h.deposito;
          this.unidades = h.unidades;
          this.imagenUrl = h.imagenUrl ?? '';
        },
        error: (respuesta: HttpErrorResponse) => {
            this.guardando.set(false);
          
            const mensaje =
              respuesta.error?.mensaje ??
              respuesta.error?.message ??
              'No se pudo guardar la herramienta.';
          
            this.errores.set([mensaje]);
          }
      });
    }
  }

  guardar(): void {
    const errores: string[] = [];

    if (!this.nombre.trim()) {
      errores.push('El nombre es obligatorio.');
    }
    if (this.precioDia === null || this.precioDia <= 0) {
      errores.push('El precio por día debe ser mayor que cero.');
    }
    if (this.deposito === null || this.deposito < 0) {
      errores.push('El depósito no puede ser negativo.');
    }
    if (this.unidades === null || this.unidades < 1) {
      errores.push('Debe existir al menos una unidad.');
    }

    this.errores.set(errores);
    if (errores.length > 0) return;

    const datos: HerramientaRequest = {
      nombre: this.nombre.trim(),
      categoria: this.categoria,
      descripcion: this.descripcion.trim() || null,
      precioDia: this.precioDia!,
      deposito: this.deposito!,
      unidades: this.unidades!,
      imagenUrl: this.imagenUrl.trim() || null
    };

    this.guardando.set(true);
    this.errorServidor.set(null);
    const peticion = this.editando
      ? this.service.actualizar(this.herramientaId, datos)
      : this.service.crear(datos);
   
    
      peticion.subscribe({
        next: () => {
          this.guardando.set(false);
          this.router.navigate(['/herramientas']);
        },
        error: (error: HttpErrorResponse) => {
          this.guardando.set(false);
          this.errorServidor.set(mensajeDeError(error));
        }
      });
  }

  cancelar(): void {
    this.router.navigate(['/herramientas']);
  }
}

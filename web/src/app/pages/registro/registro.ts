
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { validarCuenta } from '../../core/validaciones';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css'
})
export class Registro {
  private auth = inject(AuthService);

  nombre = '';
  email = '';
  password = '';
  confirmarPassword = '';

  errores = signal<string[]>([]);

  registrarse(): void {
    const datos = {
      nombre: this.nombre,
      email: this.email,
      password: this.password
    };

    const errores = validarCuenta(datos, this.confirmarPassword);
    this.errores.set(errores);

    if (errores.length > 0) {
      return;
    }

    this.auth.registrarse(datos);
  }
}


export function calcularDias(
  fechaInicio: string,
  fechaFin: string
): number {
  if (!fechaInicio || !fechaFin) {
    return 0;
  }

  const inicio = convertirFechaADias(fechaInicio);
  const fin = convertirFechaADias(fechaFin);

  if (inicio === null || fin === null || fin < inicio) {
    return 0;
  }

  return fin - inicio + 1;
}

export function validarAlquiler(
  fechas: {
    fechaInicio: string;
    fechaFin: string;
  },
  hoy: string
): string[] {
  const errores: string[] = [];

  const { fechaInicio, fechaFin } = fechas;

  if (!fechaInicio) {
    errores.push('La fecha de inicio es obligatoria.');
  }

  if (!fechaFin) {
    errores.push('La fecha de devolución es obligatoria.');
  }

  if (errores.length > 0) {
    return errores;
  }

  const inicio = convertirFechaADias(fechaInicio);
  const fin = convertirFechaADias(fechaFin);
  const diaActual = convertirFechaADias(hoy);

  if (inicio === null || fin === null || diaActual === null) {
    return ['Las fechas ingresadas no son válidas.'];
  }

  if (fin < inicio) {
    errores.push(
      'La fecha de devolución no puede ser anterior a la fecha de inicio.'
    );
    return errores;
  }

  if (inicio < diaActual) {
    errores.push(
      'La fecha de inicio no puede ser anterior a hoy.'
    );
    return errores;
  }

  const dias = fin - inicio + 1;

  if (dias > 15) {
    errores.push('El alquiler no puede superar los 15 días.');
  }

  return errores;
}

function convertirFechaADias(fecha: string): number | null {
  const coincidencia = /^(\d{4})-(\d{2})-(\d{2})$/.exec(fecha);

  if (!coincidencia) {
    return null;
  }

  const anio = Number(coincidencia[1]);
  const mes = Number(coincidencia[2]);
  const dia = Number(coincidencia[3]);

  const milisegundos = Date.UTC(anio, mes - 1, dia);
  const fechaUTC = new Date(milisegundos);

  if (
    fechaUTC.getUTCFullYear() !== anio ||
    fechaUTC.getUTCMonth() !== mes - 1 ||
    fechaUTC.getUTCDate() !== dia
  ) {
    return null;
  }

  return Math.floor(milisegundos / 86400000);
}

export function validarCuenta(
  datos: {
    nombre: string;
    email: string;
    password: string;
  },
  confirmarPassword: string
): string[] {
  const errores: string[] = [];

  if (!datos.nombre?.trim()) {
    errores.push('El nombre es obligatorio.');
  }

  if (!datos.email?.trim()) {
    errores.push('El correo electrónico es obligatorio.');
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(datos.email)) {
    errores.push('El correo electrónico no es válido.');
  }

  if (!datos.password) {
    errores.push('La contraseña es obligatoria.');
  } else if (datos.password.length < 8) {
    errores.push('La contraseña debe tener mínimo 8 caracteres.');
  }

  if (datos.password !== confirmarPassword) {
    errores.push('Las contraseñas no coinciden.');
  }

  return errores;
}

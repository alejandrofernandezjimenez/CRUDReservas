import React, { useState } from 'react';
import axios from 'axios';

function ReservaForm() {
  const [formData, setFormData] = useState({
    checkIn: "2027-03-25",
    checkOut: "2027-03-26",
    idCliente: 1,
    idHabitacion: 2
  });

  const [error, setError] = useState(null); // Estado para manejar errores

  const handleSubmit = async () => {
    setError(null); // Reiniciar el error antes de intentar
    try {
      const response = await axios.post('http://localhost:8080/api/reservas/crear', formData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      console.log('Respuesta del backend:', response.data);
      alert('Reserva creada con éxito: ' + response.data);
    } catch (error) {
      const errorMessage = error.response
        ? `Error ${error.response.status}: ${error.response.data}`
        : error.message;
      console.error('Error al crear la reserva:', errorMessage);
      setError(errorMessage); // Guardar el error para mostrarlo en la UI
    }
  };

  return (
    <div>
      <h2>Crear Reserva</h2>
      <button onClick={handleSubmit}>Enviar Reserva</button>
      {error && <p style={{ color: 'red' }}>{error}</p>} {/* Mostrar error en la interfaz */}
    </div>
  );
}

export default ReservaForm;
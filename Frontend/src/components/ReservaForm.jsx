import React, { useState } from 'react';
import axios from 'axios';
import CrearReservaForm from './CrearReservaForm';
import ListaReservas from './ListaReservas';

function ReservaForm() {
  const [formData, setFormData] = useState({
    checkIn: '',
    checkOut: '',
    idCliente: '',
    idHabitacion: ''
  });

  const [reservas, setReservas] = useState([]);
  const [error, setError] = useState(null);

  const handleListarReservas = async () => {
    setError(null);
    try {
      const response = await axios.get('http://localhost:8080/api/reservas/listar');
      console.log('Datos recibidos del backend:', response.data);
      setReservas(response.data);
    } catch (error) {
      const errorMessage = error.response
        ? `Error ${error.response.status}: ${error.response.data}`
        : error.message;
      console.error('Error al listar reservas:', errorMessage);
      setError(errorMessage);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
      <CrearReservaForm
        formData={formData}
        setFormData={setFormData}
        error={error}
        setError={setError}
      />
      <button
        type="button"
        onClick={handleListarReservas}
        style={{ padding: '10px 20px', backgroundColor: '#28a745', color: 'white', border: 'none', borderRadius: '5px', marginBottom: '20px' }}
      >
        Listar Reservas
      </button>
      <ListaReservas reservas={reservas} />
    </div>
  );
}

export default ReservaForm;
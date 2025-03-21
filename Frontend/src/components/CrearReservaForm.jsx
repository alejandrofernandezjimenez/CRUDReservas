import React from 'react';
import axios from 'axios';

function CrearReservaForm({ formData, setFormData, error, setError }) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async () => {
    setError(null);
    try {
      const response = await axios.post('http://localhost:8080/api/reservas/crear', formData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      console.log('Respuesta del backend (crear):', response.data);
      alert('Reserva creada con éxito: ' + response.data);
      setFormData({ checkIn: '', checkOut: '', idCliente: '', idHabitacion: '' });
    } catch (error) {
      const errorMessage = error.response
        ? `Error ${error.response.status}: ${error.response.data}`
        : error.message;
      console.error('Error al crear la reserva:', errorMessage);
      setError(errorMessage);
    }
  };

  return (
    <div>
      <h2>Crear Reserva</h2>
      <form onSubmit={(e) => e.preventDefault()} style={{ marginBottom: '30px' }}>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="checkIn">Check-In: </label>
          <input
            type="date"
            id="checkIn"
            name="checkIn"
            value={formData.checkIn}
            onChange={handleChange}
            required
            style={{ padding: '5px', width: '200px' }}
          />
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="checkOut">Check-Out: </label>
          <input
            type="date"
            id="checkOut"
            name="checkOut"
            value={formData.checkOut}
            onChange={handleChange}
            required
            style={{ padding: '5px', width: '200px' }}
          />
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="idCliente">ID Cliente: </label>
          <input
            type="number"
            id="idCliente"
            name="idCliente"
            value={formData.idCliente}
            onChange={handleChange}
            required
            min="1"
            style={{ padding: '5px', width: '200px' }}
          />
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="idHabitacion">ID Habitación: </label>
          <input
            type="number"
            id="idHabitacion"
            name="idHabitacion"
            value={formData.idHabitacion}
            onChange={handleChange}
            required
            min="1"
            style={{ padding: '5px', width: '200px' }}
          />
        </div>
        <button
          type="button"
          onClick={handleSubmit}
          style={{ padding: '10px 20px', backgroundColor: '#007bff', color: 'white', border: 'none', borderRadius: '5px', marginRight: '10px' }}
        >
          Enviar Reserva
        </button>
      </form>
      {error && <p style={{ color: 'red', marginBottom: '20px' }}>{error}</p>}
    </div>
  );
}

export default CrearReservaForm;
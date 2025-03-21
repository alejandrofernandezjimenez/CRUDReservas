import React from 'react';

function ListaReservas({ reservas }) {
  return (
    <div>
      {reservas.length > 0 && (
        <div>
          <h3>Lista de Reservas</h3>
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
            <thead>
              <tr style={{ backgroundColor: '#f2f2f2' }}>
                <th style={{ padding: '10px', border: '1px solid #ddd' }}>ID Reserva</th>
                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Check-In</th>
                <th style={{ padding: '10px', border: '1px solid #ddd' }}>Check-Out</th>
                <th style={{ padding: '10px', border: '1px solid #ddd' }}>ID Cliente</th>
                <th style={{ padding: '10px', border: '1px solid #ddd' }}>ID Habitación</th>
              </tr>
            </thead>
            <tbody>
              {reservas.map((reserva, index) => (
                <tr key={reserva.id_reserva || index} style={{ textAlign: 'center' }}>
                  <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                    {reserva.idReserva || 'N/A'}
                  </td>
                  <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                    {reserva.checkIn || 'N/A'}
                  </td>
                  <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                    {reserva.checkOut || 'N/A'}
                  </td>
                  <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                    {reserva.cliente.idCliente || 'N/A'}
                  </td>
                  <td style={{ padding: '10px', border: '1px solid #ddd' }}>
                    {reserva.habitacion.idHabitacion || 'N/A'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default ListaReservas;
import React from 'react';
import ReservaForm from './components/ReservaForm'; // Asegúrate de que la ruta sea correcta
import './App.css'; // Opcional, para estilos

function App() {
  return (
    <div className="App">
      <h1>Gestión de Reservas</h1>
      <ReservaForm />
    </div>
  );
}

export default App;
import { Navigate } from 'react-router-dom';

export default function EventCreatorRoute({ children }) {
  const role = localStorage.getItem('role');
  return role === 'event_creator' ? children : <Navigate to="/" />;
}
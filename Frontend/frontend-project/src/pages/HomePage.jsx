import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // if you use React-Router
import _axios from "../axiosInstance";
import "../pages_css/HomePageCss.css";

export default function HomePage() {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();      // only if you navigate elsewhere

  useEffect(() => {
    _axios.get("/events/latest")
      .then(res => setEvents(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="loading">Učitavanje...</p>;

  return (
    <div className="event-grid">
      {events.map(ev => (
        <div
          key={ev.id}
          className="event-card"
          onClick={() => navigate(`/events/${ev.id}`)} // or any route you have
        >
          <h3>{ev.title}</h3>
          <p><strong>Lokacija: {ev.location}</strong></p>
          <p><strong>Opis: {ev.description.length > 200 ? `${ev.description.slice(0, 200)}…` : ev.description}</strong></p>
          <p><strong>Kategorija: {ev.category?.categoryName}</strong></p>
          <p>Datum objave: {new Date(ev.createdAt).toLocaleDateString("sr-RS")}</p>
        </div>
      ))}
    </div>
  );
}
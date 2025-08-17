import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import _axios from "../axiosInstance";
import "../pages_css/HomePageCss.css";

export default function CategoryPage() {
  const { id } = useParams();            // category id from /category/:id
  const navigate = useNavigate();
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);

  //console.log("CategoryPage rendered, id =", id);

  useEffect(() => {
    if (!id) return;

    // 1️⃣  fetch events that belong to this category
    _axios.get(`/events/all/${id}`)
      .then(res => setEvents(res.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <p className="loading">Učitavanje...</p>;

  return (
    <div className="event-grid">
      {events.length === 0 && <p>Nema događaja u ovoj kategoriji.</p>}
      {events.map(ev => (
        <div
          key={ev.id}
          className="event-card"
          onClick={() => navigate(`/events/${ev.id}`)}
        >
          <h3>{ev.title}</h3>
          <p><strong>Lokacija:</strong> {ev.location}</p>
          <p><strong>Opis:</strong> {ev.description.length > 200
              ? `${ev.description.slice(0, 200)}…`
              : ev.description}
          </p>
          <p><strong>Kategorija:</strong> {ev.category?.categoryName ?? "-"}</p>
          <p><strong>Datum:</strong> {new Date(ev.eventDate).toLocaleDateString("sr-RS")}</p>
        </div>
      ))}
    </div>
  );
}
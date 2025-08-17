import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import _axios from "../axiosInstance";

export default function SearchResults() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const query = searchParams.get("q")?.trim() || "";   // ?q=rock
  const [events, setEvents]   = useState([]);
  const [loading, setLoading] = useState(true);

  console.log("Searchpage rendered, query =", query);
  useEffect(() => {
    if (!query) {
      setEvents([]);
      setLoading(false);
      return;
    }
     
    setLoading(true);
  _axios.get("/events/search", { params: { q: query } })
      .then(res => setEvents(res.data || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [query]);

  if (loading) return <p>Učitavanje...</p>;
  if (!query)  return <p>Unesite pojam za pretragu.</p>;
  if (events.length === 0)
    return <p>Nema događaja koji počinju na “{query}”.</p>;

  return (
    <div className="event-grid">
      {events.map(ev => (
        <div
          key={ev.id}
          className="event-card"
          onClick={() => navigate(`/events/${ev.id}`)}
        >
          <h3>{ev.title}</h3>
          <p><strong>Lokacija:</strong> {ev.location}</p>
          <p><strong>Opis:</strong>
            {ev.description.length > 200
              ? `${ev.description.slice(0, 200)}…`
              : ev.description}
          </p>
          <p><strong>Kategorija:</strong> {ev.category?.categoryName ?? "-"}</p>
          <p><strong>Datum:</strong>
            {new Date(ev.eventDate).toLocaleDateString("sr-RS")}
          </p>
        </div>
      ))}
    </div>
  );
}
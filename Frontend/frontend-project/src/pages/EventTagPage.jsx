import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import _axios from "../axiosInstance";

export default function EventTagPage() {
  const { tagId } = useParams();
  const [events, setEvents] = useState([]);

  useEffect(() => {
    _axios.get(`/events/tag/${tagId}`).then(res => setEvents(res.data));
  }, [tagId]);

  if (!events.length) return <p>Nema događaja sa ovim tagom.</p>;

  return (
    <div className="events-by-tag">
      <h1>Tag #{tagId}</h1>
      <ul>
        {events.map(ev => (
          <li key={ev.id} className="event-card">
            <h3>
              <Link to={`/events/${ev.id}`}>{ev.title}</Link>
            </h3>
            <p>{ev.description?.slice(0, 120)}…</p>
            <small>
              {ev.category?.categoryName} |{" "}
              {new Date(ev.eventDate).toLocaleDateString("sr-RS")}
            </small>
          </li>
        ))}
      </ul>
    </div>
  );
}
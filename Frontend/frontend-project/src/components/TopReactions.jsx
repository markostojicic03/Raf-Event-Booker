import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import _axios from "../axiosInstance";

export default function TopReactions() {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    _axios.get("/events/top-reactions").then(res => setEvents(res.data));
  }, []);

  if (!events.length) return null;

  return (
    <aside className="top-reactions">
      <h4>Najviše reakcija</h4>
      <ul>
        {events.map(ev => (
          <li key={ev.id}>
            <Link to={`/events/${ev.id}`}>{ev.title}</Link>
            <small>({ev.reactions})</small>
          </li>
        ))}
      </ul>
    </aside>
  );
}
import { useEffect, useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { Pagination } from "react-bootstrap";
import _axios from "../axiosInstance";
import "../pages_css/PopularPageCss.css";

const PAGE_SIZE = 10;

export default function PopularPage() {
  const [events, setEvents]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage]       = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    _axios.get("/events/popular")
      .then(res => setEvents(res.data || []))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const totalPages = useMemo(() => Math.ceil(events.length / PAGE_SIZE), [events]);
  const paginated  = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE;
    return events.slice(start, start + PAGE_SIZE);
  }, [events, page]);

  if (loading) return <p className="loading">Učitavanje...</p>;

  return (
    <>
      <div className="event-grid">
        {paginated.map(ev => (
          <div
            key={ev.id}
            className="event-card"
            onClick={() => navigate(`/events/${ev.id}`)}
          >
            <h3>{ev.title}</h3>
            <p><strong>Lokacija:</strong> {ev.location}</p>
            <p><strong>Opis:</strong> {ev.description.length > 200 ? `${ev.description.slice(0, 200)}…` : ev.description}</p>
            <p><strong>Kategorija:</strong> {ev.category?.categoryName}</p>
            <p><strong>Datum objave:</strong> {new Date(ev.createdAt).toLocaleDateString("sr-RS")}</p>
          </div>
        ))}
      </div>

      {totalPages > 1 && (
        <div className="d-flex justify-content-center mt-4">
          <Pagination>
            {[...Array(totalPages)].map((_, i) => (
              <Pagination.Item
                key={i + 1}
                active={i + 1 === page}
                onClick={() => setPage(i + 1)}
              >
                {i + 1}
              </Pagination.Item>
            ))}
          </Pagination>
        </div>
      )}
    </>
  );
}
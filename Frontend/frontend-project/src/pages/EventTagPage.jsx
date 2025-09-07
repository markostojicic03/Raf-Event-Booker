import { useEffect, useState, useMemo } from "react";
import { useParams, Link } from "react-router-dom";
import { Pagination } from "react-bootstrap";
import _axios from "../axiosInstance";

const PAGE_SIZE = 10;

export default function EventTagPage() {
  const { tagId } = useParams();
  const [events, setEvents] = useState([]);
  const [page, setPage] = useState(1);

  useEffect(() => {
    _axios.get(`/events/tag/${tagId}`)
      .then(res => setEvents(res.data || []));
  }, [tagId]);

  const totalPages = useMemo(() => Math.ceil(events.length / PAGE_SIZE), [events]);
  const paginated = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE;
    return events.slice(start, start + PAGE_SIZE);
  }, [events, page]);

  if (!events.length) return <p>Nema događaja sa ovim tagom.</p>;

  return (
    <>
      <div className="events-by-tag">
        <h1>Tag #{tagId}</h1>
        <ul>
          {paginated.map(ev => (
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
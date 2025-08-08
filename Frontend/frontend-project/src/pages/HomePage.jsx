import { useEffect, useState } from "react";
import _axios from "../axiosInstance"; // Axios instanca

export default function HomePage() {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        _axios.get("/events") // GET http://localhost:8080/events
            .then((response) => {
                setEvents(response.data); // response.data je ono što backend vraća
            })
            .catch((error) => {
                console.error("Greška pri dohvatanju eventova:", error);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p>Učitavanje...</p>;
    }

    return (
        <div>
            <h1>Lista događaja</h1>
            <ul>
                {events.map(event => (
                    <li key={event.id}>
                        {event.title} — {event.location}
                    </li>
                ))}
            </ul>
        </div>
    );
}

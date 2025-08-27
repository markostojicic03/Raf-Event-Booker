import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import _axios from "../axiosInstance";
import "../pages_css/EventDetailsCss.css";   // optional

export default function EventDetails() {
  const { id } = useParams();
  const [event, setEvent] = useState(null);

  console.log("EventDetails rendered, id =", id);
useEffect(() => {
  if (!id) return;

  _axios.get(`/events/${id}`)
    .then(res => setEvent(res.data))
    .catch(console.error);

  const key = `view_${id}`;
  if (!localStorage.getItem(key)) {
    _axios.post(`/events/${id}/view`).catch(() => {});
    localStorage.setItem(key, "1");
  }
}, [id]);

//// like/dislike
const [likes, setLikes]       = useState(0);
const [dislikes, setDislikes] = useState(0);

// fetch current counts
useEffect(() => {
  if (event) {
    setLikes(event.likes || 0);
    setDislikes(event.dislikes || 0);
  }
}, [event]);

// helpers
const vote = async (type) => {
  const key = `event_${type}_${id}`;
  if (localStorage.getItem(key)) return; // already voted

  await _axios.post(`/events/${id}/${type}`).catch(() => {});
  localStorage.setItem(key, "1");

  // optimistic update
  if (type === "like"){ setLikes(l => l + 1);}
  else{ setDislikes(d => d + 1);}
};

// end of like/dislike

  if (!event) return <p className="loading">Učitavanje događaja...</p>;

  const {
    title, description, eventDate, location, createdAt,
    author, category, tags
  } = event;

  return (
    <div className="event-details">
      <h1>{title}</h1>

      <p><strong>Opis:</strong> {description}</p>

      <p><strong>Datum i vreme održavanja:</strong> {new Date(eventDate).toLocaleString("sr-RS")}</p>

      <p><strong>Lokacija:</strong> {location}</p>

      <p><strong>Datum kreiranja:</strong> {new Date(createdAt).toLocaleDateString("sr-RS")}</p>

      <p><strong>Autor:</strong> {author?.fullName || "Nepoznat"}</p>

      <p><strong>Kategorija:</strong> {category?.categoryName || "-"}</p>

     <p>
  <button onClick={() => vote("like")} disabled={localStorage.getItem(`event_like_${id}`)}>
    👍 {likes}
  </button>
  <button onClick={() => vote("dislike")} disabled={localStorage.getItem(`event_dislike_${id}`)}>
    👎 {dislikes}
  </button>
</p>
    </div>
  );
}
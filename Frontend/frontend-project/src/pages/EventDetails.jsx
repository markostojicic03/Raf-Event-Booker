import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import _axios from "../axiosInstance";
import "../pages_css/EventDetailsCss.css";   // optional

export default function EventDetails() {
  const { id } = useParams();
  const [event, setEvent] = useState(null);

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState({ author: "", text: "" });
  const [related, setRelated]   = useState([]);

  // fetch comments
  useEffect(() => {
    _axios.get(`/events/${id}/comments`).then(res => setComments(res.data));
  }, [id]);

  console.log("EventDetails rendered, id =", id);
useEffect(() => {
  if (!id) return;

  // single call: mark view THEN load event + comments + related
  Promise.all([
    _axios.post(`/events/${id}/view`),          // server-side cookie decides “once”
    _axios.get(`/events/${id}`),
    _axios.get(`/events/${id}/comments`)
  ])
  .then(([, ev, cm]) => {
    setEvent(ev.data);
    setComments(cm.data);

    // fetch related events that share any tag
    if (ev.data.tags?.length) {
      const tagIds = ev.data.tags.map(t => t.id).join(",");
      _axios
        .get(`/events/related?ids=${tagIds}&exclude=${id}`)
        .then(r => setRelated(r.data.slice(0, 3)));
    }
  })
  .catch(console.error);
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

  // add comment
const handleAdd = async (e) => {
  e.preventDefault();
  if (!newComment.author.trim() || !newComment.text.trim()) return;

  const payload = {
    author: newComment.author.trim(),
    text: newComment.text.trim(),
    createdAt: new Date().toISOString() // if backend does NOT default it
  };

  await _axios.post(`/events/${id}/comments`, payload, {
    headers: { "Content-Type": "application/json" }
  });

  setNewComment({ author: "", text: "" });
  const { data } = await _axios.get(`/events/${id}/comments`);
  setComments(data);
};

// like or dislike comment

const voteComment = async (commentId, type) => {
  const key = `comment_${type}_${commentId}`;
  if (localStorage.getItem(key)) return;

  await _axios.post(`/events/comments/${commentId}/${type}`).catch(() => {});
  localStorage.setItem(key, "1");

  // optimistic update
  setComments(prev =>
    prev.map(c =>
      c.id === commentId
        ? { ...c, [type]: c[type] + 1 }
        : c
    )
  );
};



// end of lajkovi

  if (!event) return <p className="loading">Učitavanje događaja...</p>;

  const {
    title, description, eventDate, location, createdAt,
    author, category, tags
  } = event;

  const authorName = author
  ? `${author.firstName} ${author.lastName}`.trim()
  : "Nepoznat";

  return (
    <div className="event-details">
      <h1>{title}</h1>

      <p><strong>Opis:</strong> {description}</p>

      <p><strong>Datum i vreme održavanja:</strong> {new Date(eventDate).toLocaleString("sr-RS")}</p>

      <p><strong>Lokacija:</strong> {location}</p>

      <p><strong>Datum kreiranja:</strong> {new Date(createdAt).toLocaleDateString("sr-RS")}</p>

      <p><strong>Autor:</strong> {authorName}</p>

      <p><strong>Kategorija:</strong> {category?.categoryName || "-"}</p>

      <p><strong>Pregleda:</strong> {event.views}</p>

      <p>
  <strong>Tagovi:</strong>{" "}
  {event.tags?.length
    ? event.tags.map(t => (
        <Link
          key={t.id}
          to={`/events/tag/${t.id}`}
          className="tag-link"
        >
          #{t.tagName}
        </Link>
      ))
    : "-"}
</p>

     <p>
  <button onClick={() => vote("like")} disabled={localStorage.getItem(`event_like_${id}`)}>
    👍 {likes}
  </button>
  <button onClick={() => vote("dislike")} disabled={localStorage.getItem(`event_dislike_${id}`)}>
    👎 {dislikes}
  </button>
</p>

      {/* COMMENT FORM */}
      <h3>Komentari ({comments.length})</h3>
      <form onSubmit={handleAdd} style={{ display: "flex", flexDirection: "column", gap: "0.5rem" }}>
        <input placeholder="Ime" value={newComment.author} onChange={e => setNewComment({...newComment, author: e.target.value})} />
        <textarea placeholder="Tekst" rows="3" value={newComment.text} onChange={e => setNewComment({...newComment, text: e.target.value})} />
        <button type="submit">Pošalji</button>
     </form>
      {/* COMMENT LIST */}
      <div className="comments">
        {comments.map(c => (
          <div key={c.id} className="comment">
            <strong>{c.author}</strong> – {new Date(c.createdAt).toLocaleString("sr-RS")}
            <p>{c.text}</p>
            <small>
  <button
    onClick={() => voteComment(c.id, "like")}
    disabled={localStorage.getItem(`comment_like_${c.id}`)}
  >
    👍 {c.likes}
  </button>
  &nbsp;
  <button
    onClick={() => voteComment(c.id, "dislike")}
    disabled={localStorage.getItem(`comment_dislike_${c.id}`)}
  >
    👎 {c.dislikes}
  </button>
</small>
          </div>
        ))}
      </div>
        {/*  PROČITAJ JOŠ  */}
{related.length > 0 && (
  <>
    <h3>Pročitaj još...</h3>
    <div className="read-more">
      {related.slice(0, 3).map(r => (
        <div key={r.id} className="mini-card">
          <h4>
            <Link to={`/events/${r.id}`}>{r.title}</Link>
          </h4>
          <p>{r.description?.slice(0, 80)}…</p>
        </div>
      ))}
    </div>
  </>
)}


    </div>
  );
}
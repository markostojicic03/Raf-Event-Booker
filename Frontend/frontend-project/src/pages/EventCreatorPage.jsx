import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import _axios from "../axiosInstance";
import { Table, Button, Modal, Form, Pagination } from "react-bootstrap";
import "../pages_css/HomePageCss.css";

export default function EventCreatorPage() {
  const navigate = useNavigate();
  const [user] = useState({
    name: localStorage.getItem("email") || "EventCreator",
    role: localStorage.getItem("role") || "event_creator"
  });

  
  const [activeTab, setActiveTab] = useState("categories"); 
  const [data, setData] = useState([]);
  const [modal, setModal] = useState({ show: false, type: "", item: null });
  const [form, setForm] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 10;
  const [query, setQuery] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  const [categories, setCategories] = useState([]);
  const [tags, setTags] = useState([]);

  useEffect(() => {
    _axios.get("/category").then(res => setCategories(res.data));
    _axios.get("/tag").then(res => setTags(res.data));
  }, []);

  
  useEffect(() => {
    if (user.role !== "event_creator") navigate("/login");
  }, [user.role, navigate]);

 
  const load = async endpoint => {
    const res = await _axios.get(endpoint);
    setData(res.data);
  };

  useEffect(() => {
    if (activeTab === "categories") load("/category");
    else if (activeTab === "events" || activeTab === "search") {
      const url = searchTerm.trim()
        ? `/events/search?q=${encodeURIComponent(searchTerm.trim())}`
        : "/events/latest";
      load(url);
    }
  }, [activeTab, searchTerm]);

  useEffect(() => { setCurrentPage(1); setSearchTerm(""); }, [activeTab]);

  
  const open = (type, item) => {
    setModal({ show: true, type, item });
    if (!item) {
      setForm({});
    } else {
      setForm({
        ...item,
        category: item.category ? { id: item.category.id } : null,
        tagsRaw: (item.tags || []).map(t => t.tagName).join(", ")
      });
    }
  };
  const close = () => setModal({ show: false, type: "", item: null });

  const remove = async id => {
    const endpoint = activeTab === "categories" ? `/category/${id}` : `/events/${id}`;
    try {
      await _axios.delete(endpoint);
      load(activeTab === "categories" ? "/category" : "/events/latest");
    } catch (err) {
      if (err.response?.status === 409) alert(err.response.data.message);
      else alert("Greška prilikom brisanja.");
    }
  };

  const save = async () => {
    let endpoint = "";
    let method = "post";
    if (activeTab === "categories") endpoint = "/category";
    else if (activeTab === "events") endpoint = "/events";

    if (modal.item?.id) {
      endpoint += `/${modal.item.id}`;
      method = "put";
    }

    const payload = {};
    const fields = {
      categories: ["categoryName", "categoryDescription"],
      events: ["title", "description", "eventDate", "location", "maxCapacity"]
    }[activeTab];
    fields.forEach(f => payload[f] = form[f]);

    if (activeTab === "events") {
      payload.category = form.category;
      payload.tags = form.tags;
    }

    try {
      await _axios[method](endpoint, payload);
      load(activeTab === "categories" ? "/category" : "/events/latest");
      close();
    } catch (err) {
      if (err.response?.status === 409) alert(err.response.data.error || "Naziv već postoji!");
      else alert(err.response?.data?.error || "Unexpected error");
    }
  };


  const tabs = ["categories", "events", "search"];
  const paginated = data.slice((currentPage - 1) * perPage, currentPage * perPage);

  return (
    <div className="event-grid" style={{ padding: "2rem" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h1>Event Creator Panel</h1>
        <div>
          <span style={{ marginRight: "1rem" }}>{user.name}</span>
          <Button variant="outline-danger" onClick={() => { localStorage.clear(); navigate("/login"); }}>Logout</Button>
        </div>
      </div>

      <div style={{ marginBottom: "1rem" }}>
        {tabs.map(t => (
          <Button key={t} active={activeTab === t} onClick={() => setActiveTab(t)} className="me-2">
            {t.charAt(0).toUpperCase() + t.slice(1)}
          </Button>
        ))}
      </div>

      {activeTab !== "search" && (
  <Button
    variant="primary"
    onClick={() => open(activeTab, null)}
    className="mb-3"
  >
    Dodaj {activeTab === "categories" ? "kategoriju" : "događaj"}
  </Button>
)}

      {activeTab === "search" && (
        <div className="mb-3 d-flex gap-2" style={{ maxWidth: 400 }}>
          <Form.Control
            type="text"
            placeholder="Pretraži događaje..."
            value={query}
            onChange={e => setQuery(e.target.value)}
          />
          <Button variant="outline-primary" onClick={() => setSearchTerm(query)}>Search</Button>
        </div>
      )}

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            {(activeTab === "categories"
              ? ["categoryName", "categoryDescription"]
              : ["title", "author", "createdAt"]
            ).map(f => <th key={f}>{f}</th>)}
            <th>Akcije</th>
          </tr>
        </thead>
        <tbody>
          {paginated.map(item => (
            <tr key={item.id}>
              {(activeTab === "categories"
                ? ["categoryName", "categoryDescription"]
                : ["title", "author", "createdAt"]
              ).map(f => (
                <td key={f}>
                  {f === "title" && activeTab === "events" ? (
                    <a href={`/events/${item.id}`} target="_blank" rel="noreferrer">
                      {item[f]}
                    </a>
                  ) : (
                    (f === "author"
                      ? `${item.author?.firstName ?? ""} ${item.author?.lastName ?? ""}`.trim() || "-"
                      : item[f] ?? "-"
                    )
                  )}
                </td>
              ))}
              <td>
                <Button size="sm" variant="warning" className="me-1" onClick={() => open(activeTab, item)}>Izmeni</Button>
                <Button size="sm" variant="danger" onClick={() => remove(item.id)}>Obriši</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {data.length > perPage && (
        <Pagination className="justify-content-center">
          {[...Array(Math.ceil(data.length / perPage)).keys()].map(p => (
            <Pagination.Item key={p + 1} active={p + 1 === currentPage} onClick={() => setCurrentPage(p + 1)}>
              {p + 1}
            </Pagination.Item>
          ))}
        </Pagination>
      )}

      {/* ----------  MODAL  (same as admin)  ---------- */}
      <Modal show={modal.show} onHide={close}>
        <Modal.Header closeButton>
          <Modal.Title>{modal.type === "add" ? "Dodaj" : "Izmeni"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            {(activeTab === "categories"
              ? ["categoryName", "categoryDescription"]
              : ["title", "description", "eventDate", "location", "maxCapacity"]
            ).map(f => (
              <Form.Group key={f} className="mb-2">
                <Form.Label>{f}</Form.Label>
                <Form.Control
                  type={f.includes("Date") ? "datetime-local" : "text"}
                  value={form[f] ?? ""}
                  onChange={e => setForm({ ...form, [f]: e.target.value })}
                />
              </Form.Group>
            ))}

            {activeTab === "events" && (
              <>
                <Form.Group className="mb-2">
                  <Form.Label>Category</Form.Label>
                  <Form.Select
                    value={form.category?.id ?? ""}
                    onChange={e => setForm({ ...form, category: { id: Number(e.target.value) } })}
                  >
                    <option value="">-- choose --</option>
                    {categories.map(c => (
                      <option key={c.id} value={c.id}>{c.categoryName}</option>
                    ))}
                  </Form.Select>
                </Form.Group>

                 <Form.Group className="mb-2">
                    <Form.Label>Tags (multi-select)</Form.Label>
                    <Form.Select
                        multiple
                        value={form.tags?.map(t => t.id) ?? []}
                        onChange={e =>
                        setForm({
                            ...form,
                            tags: [...e.target.selectedOptions].map(o => ({ id: Number(o.value) }))
                        })
                        }
                    >
                        {tags.map(t => (
                        <option key={t.id} value={t.id}>{t.tagName}</option>
                        ))}
                    </Form.Select>
                    </Form.Group>
                </>
                )}
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={close}>Otkaži</Button>
          <Button variant="primary" onClick={save}>Sačuvaj</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import _axios from "../axiosInstance";
import { Table, Button, Modal, Form, Pagination, Alert } from "react-bootstrap";
import "../pages_css/HomePageCss.css";

export default function AdminPage() {
  const navigate = useNavigate();
  const [user] = useState({
    name: localStorage.getItem("email") || "Admin",
    role: localStorage.getItem("role") || "admin"
  });

  /* ----------------------------------------------------------
     1.  GLOBAL STATE
  ---------------------------------------------------------- */
  const [activeTab, setActiveTab] = useState("categories"); // categories | events | users
  const [data, setData] = useState([]);
  const [modal, setModal] = useState({ show: false, type: "", item: null });
  const [form, setForm] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 10;

  /* ----------------------------------------------------------
     2.  AUTH CHECK
  ---------------------------------------------------------- */
  useEffect(() => {
    if (user.role !== "admin") navigate("/login");
  }, [user.role, navigate]);

  /* ----------------------------------------------------------
     3.  DATA LOADERS
  ---------------------------------------------------------- */
  const load = async (endpoint) => {
    const res = await _axios.get(endpoint);
    setData(res.data);
  };

  useEffect(() => {
    if (activeTab === "categories") load("/category");
    if (activeTab === "events") load("/events");
    if (activeTab === "users") load("/users");
  }, [activeTab]);

  /* ----------------------------------------------------------
     4.  CRUD HELPERS
  ---------------------------------------------------------- */
  const open = (type, item) => {
    setModal({ show: true, type, item });
    setForm(item || {});
  };

  const close = () => setModal({ show: false, type: "", item: null });

  
const remove = async (id) => {
  const endpoint = `/category/${id}`;
  try {
    await _axios.delete(endpoint);
    load("/category");
  } catch (err) {
    if (err.response?.status === 409) {
      alert(err.response.data.message);   // shows the backend text
    } else {
      alert("Greška prilikom brisanja.");
    }
  }
};



  const save = async () => {
    let endpoint = "";
    let method = "post";
    if (activeTab === "categories") endpoint = "/category";
    if (activeTab === "events") endpoint = "/events";
    if (activeTab === "users") endpoint = "/users";

    if (modal.item?.id) {
      endpoint += `/${modal.item.id}`;
      method = "put";
    }

  try {
    await _axios[method](endpoint, form);
    load(
      activeTab === "categories" ? "/category"
      : activeTab === "events"   ? "/events"
      : "/users"
    );
    close();
  } catch (err) {
    if (err.response?.status === 409) {
      alert(err.response.data.message);           // or setState for nicer UI
    } else {
      alert("Unexpected error");
    }
  }
};

  /* ----------------------------------------------------------
     5.  JSX
  ---------------------------------------------------------- */
  const tabs = ["categories", "events", "users"];
  const fields = {
    categories: ["categoryName", "categoryDescription"],
    events: ["title", "description", "eventDate", "location", "maxCapacity"],
    users: ["firstName", "lastName", "email", "role"]
  };

  const paginated = data.slice((currentPage - 1) * perPage, currentPage * perPage);

  return (
    <div className="event-grid" style={{ padding: "2rem" }}>
      {/* Header */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
        <h1>Admin Panel</h1>
        <div>
          <span style={{ marginRight: "1rem" }}>{user.name}</span>
          <Button variant="outline-danger" onClick={() => { localStorage.clear(); navigate("/login"); }}>Logout</Button>
        </div>
      </div>

      {/* Tabs */}
      <div style={{ marginBottom: "1rem" }}>
        {tabs.map((t) => (
          <Button key={t} active={activeTab === t} onClick={() => setActiveTab(t)} className="me-2">
            {t.charAt(0).toUpperCase() + t.slice(1)}
          </Button>
        ))}
      </div>

      {/* Add button */}
      <Button variant="primary" onClick={() => open(activeTab, null)} className="mb-3">
        Dodaj novu kategoriju/event/usera 
      </Button>

      {/* Table */}
      <Table striped bordered hover responsive>
        <thead>
          <tr>
            {fields[activeTab].map((f) => <th key={f}>{f}</th>)}
            <th>Akcije</th>
          </tr>
        </thead>
        <tbody>
          {paginated.map((item) => (
            <tr key={item.id}>
              {fields[activeTab].map((f) => <td key={f}>{item[f] ?? "-"}</td>)}
              <td>
                <Button size="sm" variant="warning" className="me-1" onClick={() => open(activeTab, item)}>Izmeni</Button>
                <Button size="sm" variant="danger" onClick={() => remove(item.id)}>Obriši</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Pagination */}
      {data.length > perPage && (
        <Pagination className="justify-content-center">
          {[...Array(Math.ceil(data.length / perPage)).keys()].map((p) => (
            <Pagination.Item key={p + 1} active={p + 1 === currentPage} onClick={() => setCurrentPage(p + 1)}>
              {p + 1}
            </Pagination.Item>
          ))}
        </Pagination>
      )}

      {/* Modal */}
      <Modal show={modal.show} onHide={close}>
        <Modal.Header closeButton>
          <Modal.Title>{modal.type === "add" ? "Dodaj" : "Izmeni"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            {fields[activeTab].map((f) => (
              <Form.Group key={f} className="mb-2">
                <Form.Label>{f}</Form.Label>
                <Form.Control
                  type={f.includes("Date") ? "datetime-local" : "text"}
                  value={form[f] ?? ""}
                  onChange={(e) => setForm({ ...form, [f]: e.target.value })}
                />
              </Form.Group>
            ))}
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
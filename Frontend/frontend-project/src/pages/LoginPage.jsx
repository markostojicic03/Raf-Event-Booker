// src/pages/LoginPage.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import _axios from "../axiosInstance";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

const handleLogin = async (e) => {
  e.preventDefault();
  setError("");
  try {
    const { data } = await _axios.post("/users/login", {
      username: email,
      password
    });
    localStorage.setItem("jwt", data.jwt);   // backend returns { "jwt": "..." }
    localStorage.setItem('role', data.role); 
    
  // Redirect based on role
  if (data.role === "admin") {
    navigate("/admin");
  } else if (data.role === "event_creator") {
    navigate("/events");    // or any creator dashboard
  } else {
    navigate("/");
  }

if (response.status === 401) setError("Korisnik je deaktiviran.");

  

  } catch (err) {
    setError("Neispravni kredencijali");
  }
};

  return (
    <div style={{ maxWidth: 400, margin: "80px auto" }}>
      <h2>Prijava u EMS</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <form onSubmit={handleLogin}>
        <div className="mb-3">
          <label>Email</label>
          <input
            type="email"
            className="form-control"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label>Lozinka</label>
          <input
            type="password"
            className="form-control"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary w-100">
          Prijavi se
        </button>
      </form>
    </div>
  );
}
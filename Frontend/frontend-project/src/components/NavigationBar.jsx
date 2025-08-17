import { Navbar, Nav, Container, Form, FormControl, Button, NavDropdown } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import _axios from "../axiosInstance";

const NavigationBar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [categories, setCategories] = useState([]);

  // fetch categories once on mount
  useEffect(() => {
    _axios.get('/category')          // <-- your endpoint
      .then(res => setCategories(res.data))
      .catch(console.error);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
      setSearchQuery('');
    }
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand as={Link} to="/">Pregled Događaja</Navbar.Brand>
        <Navbar.Toggle aria-controls="main-navbar" />
        <Navbar.Collapse id="main-navbar">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/" className={location.pathname === "/" ? "active" : ""}>Početna</Nav.Link>
            <Nav.Link as={Link} to="/popular" className={location.pathname === "/popular" ? "active" : ""}>Najposećeniji</Nav.Link>

            {/* Dynamic category dropdown */}
            <NavDropdown title="Kategorije" id="category-dropdown">
              {categories.map(cat => (
                <NavDropdown.Item
                  key={cat.id}
                  as={Link}
                  to={`/category/${cat.id}`}
                >
                  {cat.categoryName}
                </NavDropdown.Item>
              ))}
            </NavDropdown>
          </Nav>

          <Form className="d-flex" onSubmit={handleSearch}>
            <FormControl
              type="search"
              placeholder="Pretraga događaja"
              className="me-2"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <Button type="submit" variant="outline-info">Pretraži</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavigationBar;
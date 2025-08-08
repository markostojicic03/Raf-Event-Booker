import { Navbar, Nav, Container, Form, FormControl, Button, NavDropdown } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useState } from 'react';

const NavigationBar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim() !== '') {
      navigate(`/search/${searchQuery}`);
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

            {/* Dinamička ili statička lista kategorija */}
            <NavDropdown title="Kategorije" id="category-dropdown">
              <NavDropdown.Item as={Link} to="/category/1">Kultura</NavDropdown.Item>
              <NavDropdown.Item as={Link} to="/category/2">Tehnologija</NavDropdown.Item>
              <NavDropdown.Item as={Link} to="/category/3">Muzika</NavDropdown.Item>
              <NavDropdown.Item as={Link} to="/category/4">Sport</NavDropdown.Item>
            </NavDropdown>
          </Nav>

          {/* Pretraga */}
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

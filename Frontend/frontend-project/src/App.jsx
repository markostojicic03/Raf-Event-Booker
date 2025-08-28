import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavigationBar from './components/NavigationBar';
import { lazy, Suspense, useEffect, useState } from 'react';
import { Spinner } from 'react-bootstrap';

//Lazy load stranica
// const HomePage = lazy(() => import('./pages/HomePage'));
// const PopularPage = lazy(() => import('./pages/PopularPage'));
// const CategoryPage = lazy(() => import('./pages/CategoryPage'));
// const SearchResults = lazy(() => import('./pages/SearchResults'));
// const EventDetails = lazy(() => import('./pages/EventDetails'));

import HomePage from './pages/HomePage';
import PopularPage from './pages/PopularPage';
import CategoryPage from './pages/CategoryPage';
import SearchResults from './pages/SearchResults';
import EventDetails from './pages/EventDetails';
import EventTagPage from './pages/EventTagPage';

function App() {
  const [currentDate, setCurrentDate] = useState('');

  useEffect(() => {
    const date = new Date();
    const formattedDate = `${String(date.getDate()).padStart(2, '0')}.${String(
      date.getMonth() + 1
    ).padStart(2, '0')}.${date.getFullYear()}`;
    setCurrentDate(formattedDate);
  }, []);

  return (
    <div className="App">
      <Router>
        <NavigationBar />
        <main className="main-content">
          <Suspense
            fallback={
              <div className="spinner-container">
                <Spinner animation="border" role="status" />
              </div>
            }
          >
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/popular" element={<PopularPage />} />
              <Route path="/category/:id" element={<CategoryPage />} />
              <Route path="/search" element={<SearchResults />} />
              <Route path="/events/:id" element={<EventDetails />} />
              <Route path="/events/tag/:tagId" element={<EventTagPage />} />
            </Routes>
          </Suspense>
        </main>
      </Router>
      <footer className="footer">
        <p>Računarski Fakultet</p>
        <p>{currentDate}</p>
      </footer>
    </div>
  );
}

export default App;

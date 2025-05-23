import React from 'react';
import './App.css';
import UrlShortener from "./shortener/component/UrlShortener";
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Login from "./login/component/Login";
import ProtectedRoute from "./security/component/ProtectedRoute";
import Register from "./register/component/Register";
import RememberMeRoute from "./security/component/RememberMeRoute";

function App() {
  return (
      <Router>
          <div>
              <Routes>
                  <Route path="/" element={<RememberMeRoute element={<Login />} />}/> {}
                  <Route path="/login" element={<RememberMeRoute element={<Login />} />}/> {}
                  <Route path="/register" element={<Register/>}/> {}
                  <Route path="/shortener" element={<ProtectedRoute element={<UrlShortener />} />} />
              </Routes>
          </div>
      </Router>
  );
}

export default App;
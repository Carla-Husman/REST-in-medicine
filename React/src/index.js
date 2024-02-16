import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from "react-router-dom";

// Create a root for React to render into
const root = ReactDOM.createRoot(document.getElementById('root'));

// Render the entire application inside the root using ReactDOM.createRoot
root.render(
  /* Wrap the entire application with BrowserRouter for client-side routing */
  <React.StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </React.StrictMode>
);

// Report web vitals for performance monitoring
reportWebVitals();

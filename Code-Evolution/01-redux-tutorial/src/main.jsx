import React from 'react'
import ReactDOM from 'react-dom/client';

import App from './App';

import './index.css';

import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';

const store = configureStore(() => ({
  birds: [
    { name: 'robin', views: 1 }
  ]
}));

ReactDOM.createRoot(document.getElementById('root')).render(
  <Provider store={store}>
    <React.StrictMode>
      <App />
    </React.StrictMode>
  </Provider>,
)

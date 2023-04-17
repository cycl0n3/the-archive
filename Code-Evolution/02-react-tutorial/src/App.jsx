import React from 'react';

import './App.css';

import Hero from './components/ErrorBoundary/Hero';
import HeroErrorBoundary from './components/ErrorBoundary/HeroErrorBoundary';
import ClickCounter from './components/HigherOrderComponents/ClickCounter';
import HoverCounter from './components/HigherOrderComponents/HoverCounter';
import ClickCounter2 from './components/RenderProps/ClickCounter2';
import HoverCounter2 from './components/RenderProps/HoverCounter2';
import User from './components/RenderProps/User';
import Counter from './components/RenderProps/Counter';
import ComponentC from './components/ContextApi/ComponentC';
import { UserProvider } from './components/ContextApi/UserContext';
import PostList from './components/HTTPCall/PostList';
import PostForm from './components/HTTPCall/PostForm';

function App() {
  return (
    <div className="App">
      <PostForm />
    </div>
  )
}

export default App;

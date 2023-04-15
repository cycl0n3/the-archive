import { useState } from 'react';

import './App.css';

import animals from './data';
import AnimalCard from '../AnimalCard/AnimalCard';

function App() {

  return (
    <div className="App">
      {animals.map(animal => (
        <AnimalCard diet={animal.diet}
          key={animal.name}
          name={animal.name}
          size={animal.size}
          scientificName={animal.scientificName} />
      ))}
    </div>
  )
}

export default App;

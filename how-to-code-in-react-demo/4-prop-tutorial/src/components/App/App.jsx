import './App.css'

import { nanoid } from 'nanoid';
import data from './data';
import AnimalCard from '../AnimalCard/AnimalCard';

function App() {

  return (
    <div className="App">
      <h1>Animals</h1>
      {data.map(animal => (
        <AnimalCard key={nanoid(32)} 
          name={animal.name} 
          additional={animal.additional} 
          diet={animal.diet}
          scientificName={animal.scientificName}
          size={animal.size} />
      ))}
    </div>
  )
}

export default App;

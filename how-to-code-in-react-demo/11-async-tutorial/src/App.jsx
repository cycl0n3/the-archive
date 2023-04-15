import React, { 
  useState,
  useReducer,
  lazy,
  Suspense
} from 'react';

import './App.css';

const RiverInformation = lazy(() => import('./components/RiverInformation/RiverInformation'));

function App() {
  const [river, setRiver] = useState('');
  const [show, toggle] = useReducer(state => !state, true);

  return (
    <div className="wrapper">
      <button onClick={toggle}>Toggle Details</button>
      <button onClick={() => setRiver('nile')}>Nile</button>
      <button onClick={() => setRiver('amazon')}>Amazon</button>
      <button onClick={() => setRiver('yangtze')}>Yangtze</button>
      <button onClick={() => setRiver('mississippi')}>Mississipi</button>
      <Suspense fallback={<div>Loading component.</div>}>
        {show && <RiverInformation name={river} />}
      </Suspense>
    </div>
  )
}

export default App;

import React, { 
  useState,
  useEffect,
} from 'react';

import './App.css';

import { getList, setItem } from './services/list';

function App() {
  const [alert, setAlert] = useState(false);
  const [itemInput, setItemInput] = useState('');
  const [list, setList] = useState([]);

  useEffect(() => {
    let mounted = true;

    getList().then(items => {
      if(mounted) {
        setList(items);
      }
    })

    return () => { mounted = false; }
  }, []);

  useEffect(() => {
    if(alert) {
      setTimeout(() => {
        setAlert(false);
      }, 1000);
    }
  }, [alert]);

  useEffect(() => {
    let mounted = true;

    if(list.length && !alert) {
      return;
    }

    getList().then(items => {
      if(mounted) {
        setList(items);
      }
    })

    return () => { mounted = false }
  }, [alert, list]);

  const handleSubmit = (e) => {
    e.preventDefault();

    setItem(itemInput).then(() => {
      setItemInput('');
      setAlert(true);
    })
  }

  return (
    <div className="app">
      <h1>My grocery list.</h1>
      <ul>
        {list.map(item => (
          <li key={item.item}>{item.item}</li>
        ))}
      </ul>

      {alert && <h2> Submit Successful</h2>}

      <form onSubmit={handleSubmit}>
        <label htmlFor="item">
          <p>New Item</p>
          <input type="text" name="item" id="item" value={itemInput} onChange={e => setItemInput(e.target.value)} />
          <button type="submit">Submit</button>
        </label>
      </form>
    </div>
  )
}

export default App;

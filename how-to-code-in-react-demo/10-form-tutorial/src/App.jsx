import React, { 
  useState,
  useReducer
} from 'react';

import './App.css';

const formReducer = (state, event) => {
  if(event.reset) {
    return {
      'apple': '',
      'count': 0,
      'name': '',
      'gift-wrap': false,
    }
  }

  return {
    ...state,
    [event.name]: event.value
  }
}

function App() {
  const [formData, setFormData] = useReducer(formReducer, {
    'name': '',
    'count': 10,
    'apples': '',
    'gift-wrap': false,
  });
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitting(true);

    setTimeout(() => {
      setSubmitting(false);
      setFormData({
        reset: true,
      })
    }, 3000);
  }

  const handleChange = (e) => {
    const isCheckbox = e.target.type === 'checkbox';

    setFormData({
      name: e.target.name,
      value: isCheckbox ? e.target.checked : e.target.value
    })
  }

  return (
    <div className="wrapper">
      <h1>How about them apples!</h1>
      {submitting &&
        <div>
          <div>Submitting form ...</div>
          <ul>
            {Object.entries(formData).map(([name, value]) => (
              <li key={name}><strong>{name}</strong>: {value.toString()}</li>
            ))}
          </ul>
        </div>
      }
      <form onSubmit={handleSubmit}>
        <fieldset disabled={submitting}>
          <label htmlFor="name">
            <p>Name</p>
            <input type="text" name='name' onChange={handleChange} autoComplete='false' value={formData['name'] || ''} />
          </label>
        </fieldset>
        <fieldset disabled={submitting}>
          <label htmlFor="apple">
            <p>Apples</p>
            <select name="apples" id="apples" onChange={handleChange} value={formData['apples'] || ''}>
              <option value="">--Please choose an option--</option>
              <option value="fuji">Fuji</option>
              <option value="jonathan">Jonathan</option>
              <option value="honey-crisp">Honey crisp</option>
            </select>
          </label>
          <label htmlFor="count">
            <p>Count</p>
            <input type="number" name="count" id="count" onChange={handleChange} value={formData['count'] || ''} />
          </label>
          <label htmlFor="gift-wrap">
            <p>Gift wrap</p>
            <input type="checkbox" name="gift-wrap" id="gift-wrap"
              disabled={formData['apples'] !== 'fuji'}
              onChange={handleChange} checked={formData['gift-wrap'] || false} />
          </label>
        </fieldset>
        <button type='submit' disabled={submitting}>Submit</button>
      </form>
    </div>
  )
}

export default App;

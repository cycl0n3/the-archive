import React, {
  useState,
  useEffect
} from "react";

import './FileNamer.css';

export default function FileNamer() {
  const [name, setName] = useState('');
  const [alert, setAlert] = useState(false);

  useEffect(() => {
    const handleWindowClick = () => setAlert(false);

    if(alert) {
      window.addEventListener('click', handleWindowClick);
    } else {
      window.removeEventListener('click', handleWindowClick);
    }

    return () => window.removeEventListener('click', handleWindowClick);
  }, [alert, setAlert]);

  const validate = (e) => {
    if(/\*/.test(name)) {
      e.preventDefault();
      setAlert(true);
      return;
    }

    setAlert(false);
  }

  return (
    <div className="wrapper">
      <div className="preview">
        <h2>Preview {name}.js:</h2>
      </div>
      <form>
        <label>
          <p>Name:</p>
          <input autoComplete="off" 
            name="name" 
            onChange={e => {setName(e.target.value)}} />
        </label>
        <div className="information-wrapper">
          <button className="information"
            onClick={() => {setAlert(true)}}
            type="button">
            More Information
          </button>
          {alert && 
            <div className="popup">
              <span role="img" aria-label="allowed">✅</span> Alphanumeric characters only.
              <br />
              <span role="img" aria-label="not allowed">⛔ </span>*
            </div>
          }
        </div>
        <div>
          <button onClick={validate}>Save</button>
        </div>
      </form>
    </div>
  )
}

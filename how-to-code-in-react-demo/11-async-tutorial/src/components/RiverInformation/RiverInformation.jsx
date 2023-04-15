import React, {
  useState,
  useEffect
} from "react";

import PropTypes from 'prop-types';

import { getRiverInformation } from "../../services/rivers";

export default function RiverInformation({ name }) {
  const [riverInformation, setRiverInformation] = useState({});

  useEffect(() => {
    let mounted = true;
    
    getRiverInformation(name)
      .catch(e => console.log(e))
      .then(data => {
        if(mounted) {
          setRiverInformation(data);
        }
      });

      return () => { mounted = false; }
  }, [name]);

  return (
    <div>
      <h2>River Information</h2>
      <ul>
        <li>Continent: {riverInformation?.continent}</li>
        <li>Length: {riverInformation?.length}</li>
        <li>Outflow: {riverInformation?.outflow}</li>
      </ul>
    </div>
  )
}

RiverInformation.propTypes = {
  name: PropTypes.string.isRequired
}

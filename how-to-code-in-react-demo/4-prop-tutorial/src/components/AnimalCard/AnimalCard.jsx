import './AnimalCard.css';

import PropTypes from 'prop-types';

export default function AnimalCard(props) {
  const { additional, diet, name, scientificName, size } = props;

  const showAdditional = (additional) => {
    const info = Object.entries(additional).map((add) => (
      `${add[0]}: ${add[1]}`
    )).join('\n')

    alert(info);
  }

  return (
    <div className="animal-wrapper">
      <h2>{name}</h2>
      <h3>{scientificName}</h3>
      <h4>{size} kg</h4>
      <h5>
        {additional &&
          <button onClick={() => showAdditional(additional)}>More info</button>
        }</h5>
      <div>{diet.join(', ')}</div>
    </div>
  )
}

AnimalCard.propTypes = {
  additional: PropTypes.shape({
    link: PropTypes.string,
    notes: PropTypes.string
  }),
  diet: PropTypes.arrayOf(PropTypes.string).isRequired,
  name: PropTypes.string.isRequired,
  scientificName: PropTypes.string.isRequired,
  size: PropTypes.number.isRequired,
}

AnimalCard.defaultProps = {
  additional: {
    notes: 'No Additional Information'
  }
}

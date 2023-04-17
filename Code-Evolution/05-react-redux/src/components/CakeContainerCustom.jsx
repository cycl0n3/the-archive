import React, {
  useState
} from 'react'
import { connect } from 'react-redux'

import { buyCake } from '../redux'

const CakeContainerCustom = (props) => {
  const [number, setNumber] = useState(1)

  return (
    <div>
      <h2>
        Number of cakes: {props.numberOfCakes}
      </h2>
      <p>
        <input type="number" value={number} onChange={(e) => setNumber(e.target.value)} />
      </p>
      <button onClick={() => props.buyCake(number)}>Buy cakes</button>
    </div>
  )
}

const mapStateToProps = (state) => {
  return {
    numberOfCakes: state.cake.numberOfCakes
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    buyCake: (n) => dispatch(buyCake(n))
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( CakeContainerCustom )

// export default CakeContainer

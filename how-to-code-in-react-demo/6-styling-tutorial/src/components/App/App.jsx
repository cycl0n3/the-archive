import { useState } from 'react';

import Alert from "../Alert/Alert";
import CartSuccess from '../CartSuccess/CartSuccess';

function App() {

  const wrapper = {
    padding: 20
  }

  return (
    <div style={wrapper}>
      <Alert title='Items are not added' type='error'>
        <div>
          Your items are out of stock.
        </div>
      </Alert>
      <CartSuccess />
    </div>
  )
}

export default App;

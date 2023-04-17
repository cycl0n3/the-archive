import React from 'react';

const FunctionalCounter2 = () => {
  const initialState = 0;
  const [count, setCount] = React.useState(0);

  const increment = () => setCount(prevCount => {
    return prevCount + 1;
  });
  
  const decrement = () => setCount(prevCount => {
    return prevCount - 1;
  }); 
  
  const reset = () => setCount(initialState);

  return (
    <div>
      <h1>Functional Counter 2</h1>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
      <button onClick={decrement}>Decrement</button>
      <button onClick={reset}>Reset</button>
    </div>
  )
}

export default FunctionalCounter2;

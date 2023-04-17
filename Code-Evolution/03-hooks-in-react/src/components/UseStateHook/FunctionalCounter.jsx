import React from 'react';

const FunctionalCounter = () => {
  const [count, setCount] = React.useState(0);

  // const increment = () => setCount(count + 1);
  const increment = () => setCount(prevCount => {
    return prevCount + 1;
  });
  
  // const decrement = () => setCount(count - 1);
  const decrement = () => setCount(prevCount => {
    return prevCount - 1;
  });

  const reset = () => setCount(() => {
    return 0;
  });

  return (
    <div>
      <h1>Functional Counter</h1>
      <h2>Count: {count}</h2>
      <button onClick={increment}>Increment</button>
      <button onClick={decrement}>Decrement</button>
      <button onClick={reset}>Reset</button>
    </div>
  )
}

export default FunctionalCounter;

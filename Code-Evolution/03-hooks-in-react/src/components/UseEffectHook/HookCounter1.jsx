import React, { useEffect } from 'react';

const HookCounter1 = () => {
  const [count, setCount] = React.useState(0);

  const increment = () => setCount(count + 1);
  const decrement = () => setCount(count - 1);
  const reset = () => setCount(0);

  const [name, setName] = React.useState("")

  useEffect(() => {
    console.log('Updating document title');
    document.title = `You clicked ${count} times`;
  }, [count])

  return (
    <div>
      <button onClick={increment}>Increment</button>
      <button onClick={decrement}>Decrement</button>
      <button onClick={reset}>Reset</button>
      <p>Count: {count}</p>    

      <input type="text" value={name} onChange={e => setName(e.target.value)} />
    </div>
  )
}

export default HookCounter1;

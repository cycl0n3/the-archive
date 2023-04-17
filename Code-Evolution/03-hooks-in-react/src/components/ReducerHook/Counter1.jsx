import React, {
  useReducer
} from 'react';

const initialCountValue = 5;

const countReducer = (state, action) => {
  switch (action.type) {
    case 'INCREMENT':
      return state + 1;
    case 'DECREMENT':
      return state - 1;
    case 'RESET':
        return initialCountValue;
    default:
      return state;
  }
}

const Counter1 = () => {
  const [count, countDispatch] = useReducer(countReducer, initialCountValue);

  return (
    <div>
      <h2>Count: {count}</h2>
      <button onClick={() => countDispatch({ type: 'INCREMENT' })}>Increment</button>
      <button onClick={() => countDispatch({ type: 'DECREMENT' })}>Decrement</button>
      <button onClick={() => countDispatch({ type: 'RESET' })}>Reset</button>
    </div>
  )
}

export default Counter1;

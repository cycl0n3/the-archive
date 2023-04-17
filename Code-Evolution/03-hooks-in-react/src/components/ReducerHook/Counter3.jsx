import React, {
  useReducer
} from 'react';

const initialCountValue = 2;
const initialCount2Value = 3;

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

const Counter3 = () => {
  const [count, countDispatch] = useReducer(countReducer, initialCountValue);
  const [count2, count2Dispatch] = useReducer(countReducer, initialCount2Value);

  return (
    <div>
      <h2>Count: {count}</h2>
      <h2>Count 2: {count2}</h2>

      <div>
        <button onClick={() => countDispatch({ type: 'INCREMENT' })}>Increment</button>
        <button onClick={() => countDispatch({ type: 'DECREMENT' })}>Decrement</button>
      </div>
      <div>
        <button onClick={() => count2Dispatch({ type: 'INCREMENT' })}>Increment2</button>
        <button onClick={() => count2Dispatch({ type: 'DECREMENT' })}>Decrement2</button>
      </div>
      <button onClick={() => {
        countDispatch({ type: 'RESET' });
        count2Dispatch({ type: 'RESET' });
      }}>Reset</button>
    </div>
  )
}

export default Counter3;

import React, {
  useReducer
} from 'react';

const initialState = {
  firstCounter: 3,
  secondCounter: 10,
};

const countReducer = (state, action) => {
  switch (action.type) {
    case 'INCREMENT':
      return {
        ...state,
        firstCounter: state.firstCounter + action.value
      };
    case 'DECREMENT':
      return {
        ...state,
        firstCounter: state.firstCounter - action.value
      };
    case 'INCREMENT 2':
      return {
        ...state,
        secondCounter: state.secondCounter + action.value
      };
    case 'DECREMENT 2':
      return {
        ...state,
        secondCounter: state.secondCounter - action.value
      };
    case 'RESET':
      return initialState;
    default:
      return state;
  }
}

const Counter2 = () => {
  const [count, countDispatch] = useReducer(countReducer, initialState);

  return (
    <div>
      <h2>Count 1: {count.firstCounter}</h2>
      <h2>Count 2: {count.secondCounter}</h2>

      <p>
        <button onClick={() => countDispatch({ type: 'INCREMENT', value: 1 })}>Increment 1</button>
        <button onClick={() => countDispatch({ type: 'DECREMENT', value: 1 })}>Decrement 1</button>

        <button onClick={() => countDispatch({ type: 'INCREMENT', value: 5 })}>Increment 5</button>
        <button onClick={() => countDispatch({ type: 'DECREMENT', value: 5 })}>Decrement 5</button>

        <button onClick={() => countDispatch({ type: 'RESET' })}>Reset</button>
      </p>

      <p>
        <button onClick={() => countDispatch({ type: 'INCREMENT 2', value: 1 })}>Increment 2</button>
        <button onClick={() => countDispatch({ type: 'DECREMENT 2', value: 1 })}>Decrement 2</button>

        <button onClick={() => countDispatch({ type: 'RESET' })}>Reset 2</button>
      </p>
    </div>
  )
}

export default Counter2;

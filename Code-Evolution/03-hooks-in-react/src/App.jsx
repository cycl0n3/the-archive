import React, {
  useReducer,
  createContext
} from 'react';

import './App.css';

import Counter1 from './components/ReducerHook/Counter1';
import Counter2 from './components/ReducerHook/Counter2';
import Counter3 from './components/ReducerHook/Counter3';
import ComponentA from './components/ReducerHook/ComponentA';
import ComponentB from './components/ReducerHook/ComponentB';
import ComponentC from './components/ReducerHook/ComponentC';

const initialState = 0
const reducer = (state, action) => {
  switch (action.type) {
    case 'INCREMENT':
      return state + 1;
    case 'DECREMENT':
      return state - 1;
    case 'RESET':
      return initialState;
    default:
      return state;
  }
}

export const CountContext = createContext()

function App() {

  const [count, dispatch] = useReducer(reducer, initialState);
  
  return (
    <div className="App">
      {/* <Counter1 /> */}
      {/* <Counter2 /> */}
      {/* <Counter3 /> */}

      <CountContext.Provider value={{countState: count, countDispatch: dispatch}}>
        Count: {count}

        <ComponentA />
        <ComponentB />
        <ComponentC />
      </CountContext.Provider>
    </div>
  )
}

export default App;

import React from 'react';

import './App.css';

import DataFetching1 from './components/ReducerHook2/DataFetching1';
import DataFetching2 from './components/ReducerHook2/DataFetching2';
import ParentComponent from './components/CallbackHook/ParentComponent';
import Counter from './components/MemoHook/Counter';

document.title = 'Memo Hook';

function App2() {

  return (
    <div className="App">
      {/* <DataFetching1 /> */}
      {/* <DataFetching2 /> */}
      {/* <ParentComponent /> */}
      {/* <Counter /> */}
      app 2
    </div>
  )
}

export default App2;

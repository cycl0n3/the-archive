import React, { useState } from "react";

import "./App.css";

import BasicTable from "./components/BasicTable";
import SortingTable from "./components/SortingTable";

function App() {

  return (
    <div className="App">
      <h1>React Tables</h1>

      {/* <BasicTable /> */}
      <SortingTable />
    </div>
  );
}

export default App;

import React, { useState } from "react";

import "./App.css";
import MuiTypography from "./components/MuiTypography";
import MuiButton from "./components/MuiButton";

const App: React.FC = () => {
  return <div className="app">
    <h1>React MUI Demo</h1>
    <hr />
    {/* <MuiTypography /> */}
    <MuiButton />
  </div>;
};

export default App;

import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import VisibleLogin from "./containers/VisibleLogin";
import Login from "./components/Login/Login";
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";
import App from "./App";
// import "./index.css";
import configureStore from "./store/configureStore";

const store = configureStore();

render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById("app")
);

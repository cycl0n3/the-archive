import React, { Component } from "react";
import VisibleLogin from "./containers/VisibleLogin";
import { BrowserRouter as Router, Route, Link, Switch } from "react-router-dom";
import Login from "./components/Login/Login";
import logo from "./logo.svg";
import Register from "./components/Register/Register";
import SimpleForm from "./components/SimpleForm/SimpleForm";
import Home from "./components/Home/Home";
import Displaymessage from "./components/Displaymessage/Displaymessage";

// import "./App.css";

class App extends Component {
    render() {
        return (
            <div className="App">
                <Router>
                    <Switch>
                        <Route exact path="/" component={VisibleLogin} />
                        <Route exact path="/Register" component={Register} />
                        <Route exact path="/home" component={Home} />
                        <Route exact path="/display" component={Displaymessage} />
                        <Route exact path="/SimpleForm" component={SimpleForm} />
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;

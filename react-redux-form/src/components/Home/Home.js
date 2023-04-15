import React, { Component } from "react";

class Home extends Component {
    constructor(props) {
        super(props);
        console.log("home props", this.props);
        console.log("home state", this.state);
    }
    render() {
        console.log("home", this.props.location.state["from"]);
        return <div> Login Successfully... {this.props.location.state["from"]}</div>;
    }
}

export default Home;

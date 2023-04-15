import React, { Component } from "react";

class Displaymessage extends Component {
    constructor(props) {
        super(props);
        // console.log(this.props);
    }
    render() {
        console.log("home", this.props.location.state["fname"]);
        return <div> register Successfully... {this.props.location.state["fname"]}</div>;
    }
}

export default Displaymessage;

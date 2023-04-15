import React, { Component } from "react";

import './Instructions.css';

import emoji from '../../assets/emoji.svg';

const Instructions = () => {
  return (
    <div className="instructions">
      <img src={emoji} alt="laughing crying emoji" />
      <p>Click on an emoji to view the emoji short name</p>
    </div>
  )
}

export default Instructions;

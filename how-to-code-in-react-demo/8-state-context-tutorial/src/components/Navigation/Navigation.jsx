import React, { useContext } from "react";

import { createUseStyles } from 'react-jss';

import UserContext from "../User/User";

const styles = createUseStyles({
  wrapper: {
    borderBottom: 'black solid 2px',
    padding: [10, 15],
    textAlign: 'right'
  }
});

export default function Navigation() {
  const user = useContext(UserContext);
  const classes = styles();

  return (
    <div className={classes.wrapper}>
      Welcome, {user.name}
    </div>
  )
}

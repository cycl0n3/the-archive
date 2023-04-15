import React, { useContext } from "react";

import PropTypes from 'prop-types';
import { createUseStyles } from "react-jss";

import UserContext from "../User/User";
import { SaladContext } from "../SaladMaker/SaladMaker";

import { nanoid } from 'nanoid';

const styles = createUseStyles({
  add: {
    background: 'none',
    border: 'none',
    cursor: 'pointer'
  },
  favorite: {
    fontSize: 20,
    position: 'absolute',
    top: 10,
    right: 10
  },
  image: {
    fontSize: 80
  },
  wrapper: {
    border: 'lightgrey solid 2px',
    margin: 20,
    padding: 25,
    position: 'relative',
    textAlign: 'center',
    textTransform: 'capitalize',
    width: 200
  }
});

export default function SaladItem({ image, name }) {
  const user = useContext(UserContext);
  const favorite = user.favorites.includes(name);

  const classes = styles();
  const { setSalad } = useContext(SaladContext);

  const update = () => {
    setSalad({
      id: nanoid(10),
      name,
    })
  }

  return (
    <div className={classes.wrapper}>
      <h3>
        {name}
      </h3>
      <span className={classes.favorite} aria-label={favorite ? 'Favorite' : 'Not Favorite'}>
        {favorite ? '😋' : ''}
      </span>
      <button className={classes.add} onClick={update}>
        <span className={classes.image} role="img" aria-label={name}>{image}</span>
      </button>
    </div>
  )
}

SaladItem.propTypes = {
  image: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
}

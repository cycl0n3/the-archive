import React from "react";
import PropTypes from 'prop-types';

import './Alert.css';

export default function Alert({ children, title, type }) {
  const colors = {
    success: '#6da06f',
    error: '#f56260',
  };

  const style = {
    heading: {
      color: colors[type],
      margin: '0 0 10px 0',
    },
    wrapper: {
      border: `${colors[type]} solid 1px`,
      marginBottom: 15,
      padding: 15,
      position: 'relative',
    }
  }
  return (
    <div style={style.wrapper}>
      <h2 style={style.heading}>{title}</h2>
      {children}
    </div>
  )
}

Alert.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.arrayOf(PropTypes.element),
    PropTypes.element.isRequired
  ]),
  title: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
}

import React, { useReducer, useState } from "react";

import { nanoid } from 'nanoid';

import './Product.css';

import products from './data';

const currencyOptions = {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
}

const cartReducer = (state, action) => {
  switch(action.type) {
    case 'add':
      let id = () => nanoid(32);
      return [...state, { id: id(), ...action.product }];
    case 'remove':
      let xstate = [...state];
      let idx = xstate.findIndex(item => item.name === action.product.name);

      if(idx < 0) {
        return state;
      }

      xstate.splice(idx, 1);
      return xstate;
    default:
      return state;
  }
}

export default function Product() {
  const [cart, setCart] = useReducer(cartReducer, []);

  const add = (product) => {
    setCart({ product, type: 'add' });
  }

  const remove = (product) => {
    setCart({ product, type: 'remove' });
  }

  const getTotal = () => {
    return cart.reduce((sum, item) => sum + item.price, 0).toLocaleString(undefined, currencyOptions);
  }

  return (
    <div className="wrapper">
      <div>
        #2 Shopping Cart: {cart.length} total items.
      </div>
      <div>
        Total: {getTotal()}
      </div>
      <div>
        {products.map(product => (
          <div className="product" key={product.name}>
            <b>(${product.price})</b>
            <span role="img" aria-label={product.name}>{product.emoji}</span>
            <button onClick={() => add(product)}>Add</button> 
            <button onClick={() => remove(product)}>Remove</button>
            (#<b>{cart.filter(item => item.name === product.name).length}</b> Items)
          </div>
        ))}
      </div>
    </div>
  )
}
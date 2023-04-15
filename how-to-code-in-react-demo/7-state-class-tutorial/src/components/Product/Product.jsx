import React from "react";
import { nanoid } from 'nanoid';

import './Product.css';

import products from './data';

export default class Product extends React.Component {
  state = {
    cart: [],
  };

  currencyOptions = {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }

  getTotal = () => {
    let total = this.state.cart.reduce((totalCost, item) => totalCost + item.price, 0);
    return total.toLocaleString(undefined, this.currencyOptions);
  }

  add = (product) => {
    let id = () => nanoid(32);

    this.setState({
      cart: [...this.state.cart, {id: id(), ...product}],
    })
  }

  remove = (product) => {
    let cart = [...this.state.cart];
    let idx = cart.findIndex((item) => item.name === product.name);
  
    if(idx !== -1) {
      cart.splice(idx, 1);
      this.setState(state => {
        return ({
          cart,
        })
      });
    }
  }

  render() {
    return (
      <div className="wrapper">
        <div>
          Shopping Cart: {this.state.cart.length} total items.
        </div>
        <div>
          Total: {this.getTotal()}
        </div>
        <div>
          {products.map(product => (
            <div className="product" key={product.name}>
              <b>(${product.price})</b>
              <span role="img" aria-label={product.name}>{product.emoji}</span>
              <button onClick={() => this.add(product)}>Add</button> 
              <button onClick={() => this.remove(product)}>Remove</button>
              (#<b>{this.state.cart.filter(item => item.name === product.name).length}</b> Items)
            </div>
          ))}
        </div>
      </div>
    )
  }
}
import React from 'react'

const FunctionalCounter4 = () => {
  const [items, setItems] = React.useState([]);

  const addItem = () => {
    setItems([...items, {
      id: items.length + 1, 
      value: Math.floor(Math.random() * 10) + 1
    }]);
  }

  return (
    <div>
      <button onClick={addItem}>Add Item</button>
      <ul>
        {items.map(item => (
          <li key={item.id}> {`=>`} {item.value}</li>
        ))}
      </ul>
    </div>
  )
}

export default FunctionalCounter4;

import React from 'react'

type ListProps<T> = {
  items: T[],
  onClick: (item: T) => void
}

const List = <T extends {id: number}>( { items, onClick }: ListProps<T> ) => {
  return (
    <div>
      <h2>List of items</h2>
      {items.map(item => (
        <div key={item.id} 
          onClick={() => onClick(item)} 
          style={{border: '2px solid black', padding: '10px'}}>
          {JSON.stringify( item )}
        </div>
      ))}
    </div>
  )
}

export default List

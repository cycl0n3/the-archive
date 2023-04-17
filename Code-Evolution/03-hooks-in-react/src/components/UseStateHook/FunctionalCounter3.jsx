import React, {
  useState
} from 'react'

const FunctionalCounter3 = () => {
  const [name, setName] = useState({ firstName: 'John', lastName: 'Smith' });

  return (
    <div>
      <h1>Functional Counter 3</h1>
      <p>Name: {name.firstName} {name.lastName}</p>
      <form>
        <input
          type="text"
          value={name.firstName}
          onChange={e => setName({...name, firstName: e.target.value })}
        />
        <input
          type="text"
          value={name.lastName}
          onChange={e => setName({...name, lastName: e.target.value })}
        />
      </form>
    </div>
  )
}

export default FunctionalCounter3;

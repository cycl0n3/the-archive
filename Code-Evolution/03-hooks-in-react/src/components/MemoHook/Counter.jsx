import React, {
  useState,
  useMemo
} from 'react';

const Counter = () => {
  const [counterOne, setCounterOne] = useState(0);
  const [counterTwo, setCounterTwo] = useState(1);

  const incrementOne = () => {
    setCounterOne(counter => counter + 1);
  }

  const incrementTwo = () => {
    setCounterTwo(counter => counter + 1);
  }

  const isEven = useMemo(() => {
    let i = 0;
    while(i < 200000000) i++;

    return counterOne % 2 === 0;
  }, [counterOne])

  return (
    <div>
      <p>
        <button onClick={incrementOne}>Count one - {counterOne}</button>
        <span>{isEven ? 'Even': 'Odd'}</span>
      </p>
      <p>
        <button onClick={incrementTwo}>Count two - {counterTwo}</button>
      </p>
    </div>
  )
}

export default Counter;

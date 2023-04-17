import React from 'react';

const IntervalHookCounter = () => {
  const [count, setCount] = React.useState(0);
  
  React.useEffect(() => {
    console.log('setting up interval');

    const interval = setInterval(() => {
      setCount(prevCount => {
        return prevCount + 1;
      });
    }, 1000);

    return () => {
      console.log('clearing interval');
      clearInterval(interval);
    };
  }, [])

  return (
    <div>
      <h1>Interval Hook Counter</h1>
      <h2>Count: {count}</h2>
    </div>
  )
}

export default IntervalHookCounter;

import React from 'react'

const Title = () => {
  console.log(`Rendering title`);

  return (
    <div>
      <p>
        Callback hooks
      </p>
    </div>
  )
}

export default React.memo(Title);

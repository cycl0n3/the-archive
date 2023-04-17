import React from 'react'

import Tippy from '@tippyjs/react'

import 'tippy.js/dist/tippy.css'

const ToolTips = () => {
  return (<>
    <h2>ToolTips</h2>
    <div>
      <Tippy content='basic tooltip'>
        <button>Hover</button>
      </Tippy>
    </div>
    </>
  )
}

export default ToolTips

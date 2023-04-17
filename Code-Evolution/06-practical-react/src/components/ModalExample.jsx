import React, { useState } from 'react'
import ReactModal from 'react-modal'

ReactModal.setAppElement('#root')

const ModalExample = () => {
  const [showModal, setShowModal] = useState(false)

  const handleOpenModal = () => {
    setShowModal(true)
  }

  const handleCloseModal = () => {
    setShowModal(false)
  }

  return (
    <div>
      <button onClick={handleOpenModal}>Trigger Modal</button>
      <ReactModal 
        style={{
          overlay: {
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(255, 255, 255, 0.75)'
          },
          content: {
            position: 'absolute',
            top: '80px',
            left: '80px',
            right: '80px',
            bottom: '80px',
            border: '1px solid #ccc',
            background: '#fff',
            overflow: 'auto',
            WebkitOverflowScrolling: 'touch',
            borderRadius: '4px',
            outline: 'none',
            padding: '40px'
          }
        }}
        isOpen={showModal}
        contentLabel="Minimal Modal Example"
      >
        <button onClick={handleCloseModal}>Close Modal</button>
      </ReactModal>
    </div>
  )
}

export default ModalExample

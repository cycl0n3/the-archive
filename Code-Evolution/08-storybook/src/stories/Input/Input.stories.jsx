import React from 'react'

import { Input } from './Input'

export default {
  title: 'Input',
  component: Input
}

export const Small = () => <Input size="small" placeholder="small size" />
export const Large = () => <Input size="large" placeholder="large size" />
export const Medium = () => <Input size="medium" placeholder="medium size" />

import React from 'react'

export default function SearchFilter({ value, onChange, placeholder="Search..." }) {
  return <input placeholder={placeholder} value={value} onChange={e => onChange(e.target.value)} />
}

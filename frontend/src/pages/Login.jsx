import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const { login } = useAuth()
  const [email, setEmail] = useState('admin@local')
  const [password, setPassword] = useState('admin123')
  const [error, setError] = useState(null)
  const nav = useNavigate()

  const submit = async e => {
    e.preventDefault()
    try {
      await login(email, password)
      nav('/')
    } catch (e) {
      setError('Invalid credentials')
    }
  }

  return (
    <div className="container">
      <div className="card">
        <h2>Login</h2>
        <form onSubmit={submit} className="row">
          <div className="col">
            <label>Email</label>
            <input value={email} onChange={e=>setEmail(e.target.value)} />
          </div>
          <div className="col">
            <label>Password</label>
            <input type="password" value={password} onChange={e=>setPassword(e.target.value)} />
          </div>
          <div className="col">
            <button type="submit">Sign in</button>
          </div>
          {error && <div style={{color:'tomato'}}>{error}</div>}
        </form>
      </div>
    </div>
  )
}

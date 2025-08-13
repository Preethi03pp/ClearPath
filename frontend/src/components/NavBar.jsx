import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api'

export default function NavBar() {
  const { user, logout } = useAuth()
  const [unread, setUnread] = useState(0)
  const navigate = useNavigate()

  useEffect(() => {
    if (!user) return
    const load = () => api.get('/api/notifications/unread-count').then(r => setUnread(r.data.count || 0)).catch(()=>{})
    load()
    const iv = setInterval(load, 15000)
    return () => clearInterval(iv)
  }, [user])

  return (
    <div className="nav">
      <Link to="/">Dashboard</Link>
      <Link to="/projects">Projects</Link>
      <Link to="/tasks">Tasks</Link>
      <Link to="/teams">Teams</Link>
      {user?.role === 'ADMIN' && <Link to="/users">Users</Link>}
      <Link to="/notifications">Notifications {unread ? <span className="badge">{unread}</span> : null}</Link>
      <div className="spacer"></div>
      {user ? (
        <>
          <span className="badge">{user.role}</span>
          <span>{user.name}</span>
          <button onClick={() => { logout(); navigate('/login') }}>Logout</button>
        </>
      ) : <Link to="/login">Login</Link>}
    </div>
  )
}

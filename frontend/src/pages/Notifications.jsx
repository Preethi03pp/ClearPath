import React, { useEffect, useState } from 'react'
import api from '../api'
import { useAuth } from '../context/AuthContext'

export default function Notifications() {
  const { user, loading } = useAuth()
  const [list, setList] = useState([])
  const [pageLoading, setPageLoading] = useState(true)

  useEffect(() => {
    if (loading || !user) return
    setPageLoading(true)
    api.get('/api/notifications')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }, [loading, user])

  const markRead = async id => {
    await api.post(`/api/notifications/${id}/read`)
    setPageLoading(true)
    api.get('/api/notifications')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }

  if (loading || pageLoading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Loading notifications...</div>
  }

  return (
    <div className="container">
      <h2>Notifications</h2>
      <div className="card">
        <table className="table">
          <thead>
            <tr><th>Type</th><th>Message</th><th>When</th><th>Status</th><th></th></tr>
          </thead>
          <tbody>
            {list.map(n => (
              <tr key={n.id}>
                <td>{n.type}</td>
                <td>{n.link ? <a href={n.link}>{n.message}</a> : n.message}</td>
                <td>{new Date(n.createdAt).toLocaleString()}</td>
                <td>{n.read ? 'Read' : 'Unread'}</td>
                <td>{!n.read && <button onClick={() => markRead(n.id)}>Mark read</button>}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

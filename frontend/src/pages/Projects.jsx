import React, { useEffect, useState } from 'react'
import api from '../api'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import SearchFilter from '../components/SearchFilter'

export default function Projects() {
  const { user, loading } = useAuth()
  const [list, setList] = useState([])
  const [q, setQ] = useState('')
  const [pageLoading, setPageLoading] = useState(true)

  useEffect(() => {
    if (loading || !user) return // wait until auth is ready
    setPageLoading(true)
    api.get('/api/projects')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }, [loading, user])

  if (loading || pageLoading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Loading projects...</div>
  }

  const filtered = list.filter(p => !q || (p.name?.toLowerCase().includes(q.toLowerCase()) || p.description?.toLowerCase().includes(q.toLowerCase())))

  return (
    <div className="container">
      <div className="row">
        <h2 style={{ flex: 1 }}>Projects</h2>
        {(user?.role === 'ADMIN' || user?.role === 'MANAGER') && <Link className="badge" to="/projects/new">+ New Project</Link>}
        <SearchFilter value={q} onChange={setQ} />
      </div>
      <div className="card">
        <table className="table">
          <thead><tr><th>Name</th><th>Status</th><th>Manager</th><th>Team</th><th></th></tr></thead>
          <tbody>
            {filtered.map(p => (
              <tr key={p.id}>
                <td>{p.name}</td>
                <td><span className="badge">{p.status}</span></td>
                <td>{p.managerId}</td>
                <td>{p.teamId}</td>
                <td>
                  {(user?.role !== 'MEMBER') && <Link to={`/projects/${p.id}`}>Edit</Link>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
